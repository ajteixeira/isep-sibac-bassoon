# Bassoon Engine — Pipeline de Recomendação

> Documento de apoio à defesa. Explica o fluxo completo do motor de inferência,
> todas as classes envolvidas, o funcionamento interno do fuzzy, dos factores de
> confiança, e das regras Drools.

---

## 1. Visao geral da arquitetura

O professor submete o formulario (UI React) -> `POST /recommend` ->
`RecommendationController` -> `DroolsService` -> `RecommendationEngine` ->
`JustificationService` (LLM) -> resposta JSON.

## 2. Pipeline — passo a passo detalhado

### 2.1 Entrada do professor (JSON → DTO)

O formulário (React, 6 passos) submete um `POST /recommend` com este corpo:

```json
{
  "studentLevel": "INTERMEDIATE",
  "motivation": "HIGH",
  "skills": [
    { "skill": "LEGATO", "cf": 0.9 },
    { "skill": "STACCATO", "cf": 0.7 }
  ],
  "accompaniments": [
    { "type": "ORCHESTRA", "cf": 0.8 }
  ],
  "lastEra": "BAROQUE"
}
```

O `RecommendationController` recebe este JSON e chama dois serviços:

1. **`DroolsService.recommend(request)`** — corre o motor de inferência
2. **`JustificationService.fillJustifications(works, request)`** — gera justificações via LLM

### 2.2 STEP 1 — O input do professor (não é fuzzy)

**Classe:** `org.sibac.bassoon.fuzzy.StudentLevelMapper`

O professor escolhe o nível do aluno (3 opções: BEGINNER, INTERMEDIATE, ADVANCED)
e a motivação (3 opções: LOW, NEUTRAL, HIGH). O `StudentLevelMapper` converte
estas duas escolhas num único número, usando um ajuste simples:

```
Nível base:
  BEGINNER     → 1.5
  INTERMEDIATE → 3.5
  ADVANCED     → 5.5

Motivação (shift):
  HIGH   → +1.0
  LOW    → -1.0
  NEUTRAL → 0

Resultado = base + shift, limitado a [0.5, 6.5]
```

Exemplo: `INTERMEDIATE` + `HIGH` = 3.5 + 1.0 = **4.5**.

**Isto não é fuzzy.** É uma conversão determinística. O valor resultante
(0.5..6.5) será uma das duas entradas do sistema fuzzy (a outra é a
dificuldade da obra).

### 2.3 STEP 2 — A UNICA parte fuzzy do sistema

**Classe:** `org.sibac.bassoon.fuzzy.FuzzySuitabilityService`
**Ficheiro FCL:** `resources/fuzzy/suitability.fcl`

> TL;DR: O fuzzy pega em dois numeros (nivel do aluno + dificuldade da obra),
> aplica 9 regras definidas pelo perito, e devolve um valor de 0 a 1 que indica
> quao adequada a obra e para o aluno. **So isto.** Tudo o resto do motor
> (skills, epoca, acompanhamento, pre-requisitos) NAO e fuzzy.

#### 2.3.1 As duas entradas e a saida

```
studentLevel           workDifficulty
(0.5 .. 6.5)           (1 .. 6)
     |                      |
     +----------+-----------+
                |
                v
         +-------------+
         |  9 REGRAS   |  <- Sistema Mamdani (FCL)
         |  (fuzzy)    |
         +-------------+
                |
                v
          suitability
          (0 .. 1)
```

- **studentLevel**: o numero que saiu do `StudentLevelMapper` (nivel + motivacao)
- **workDifficulty**: a dificuldade de cada obra (1 a 6, esta no Excel)
- **suitability**: quao adequada e esta obra para este aluno. 0 = pessimo, 1 = ideal

#### 2.3.2 Porque logica difusa aqui?

A pergunta "esta obra tem a dificuldade certa para este aluno?" nao tem uma
fronteira nitida. Nao se pode dizer "dificuldade <= 3 passa, > 3 nao passa".
A logica difusa modela esta transicao de forma gradual.

Alem disso, o perito especificou uma **assimetria**: para um aluno de nivel X,
uma obra um nivel **abaixo** e mais aceitavel do que uma obra um nivel **acima**.
(Ex: para um aluno intermedio, tocar uma obra facil e mais aceitavel do que
tocar uma obra dificil.)

#### 2.3.3 Fuzzificação (membership functions)

**Nível do aluno** (eixo 0.5–6.5, acomoda o shift de motivação):

```
TERM beginner     := (0.5, 1) (1.5, 1) (3.5, 0)   ← trapézio: platô 0.5–1.5, declive até 3.5
TERM intermediate := (2, 0)   (3, 1)   (4, 1)  (5, 0)   ← trapézio: platô 3–4
TERM advanced     := (3.5, 0) (5.5, 1) (6.5, 1)   ← rampa a subir até 5.5, platô até 6.5
```

**Dificuldade da obra** (eixo 1–6):

```
TERM easy   := (1, 1)  (2, 1)  (3.5, 0)             ← platô 1–2, declive até 3.5
TERM medium := (2, 0)  (3, 1)  (4, 1)  (5, 0)       ← trapézio: platô 3–4  (NÃO é triângulo!)
TERM hard   := (3.5, 0)  (5, 1)  (6, 1)             ← rampa a subir, platô 5–6
```

```
easy:   ██████▌              medium:     ╱▔▔▔▔╲        hard:          ╱▔▔▔▔▔▔
        1.0 ▔▔╲                       ╱          ╲                    ╱
                 ╲                    ╱             ╲                 ╱
        0.0 ──────┼──               0.0─┼──────────┼─           0.0──┼──────────
             1 2 3.5                    2  3   4   5               3.5  5  6
```

#### 2.3.4 Defuzzificação (saída)

```
TERM veryLow := (0, 1)   (0.2, 0)
TERM low     := (0.1, 0) (0.3, 1) (0.5, 0)
TERM medium  := (0.4, 0) (0.6, 1) (0.8, 0)
TERM high    := (0.7, 0) (0.9, 1) (1, 1)
METHOD : COG  (Centre of Gravity)
DEFAULT := 0
```

**O que é o COG e porque foi escolhido:**

Após a acumulação (MAX das regiões ativadas pelas regras), o resultado ainda é
uma figura geométrica difusa (uma ou mais regiões com área). O COG converte essa
figura num **único número** calculando o "centro de equilíbrio" da área total:

```
        ∫ x · μ(x) dx
COG = ─────────────────
          ∫ μ(x) dx
```

Intuitivamente: imagina a área acumulada como uma peça de cartão. O COG é o
ponto onde ela ficaria em equilíbrio numa agulha. Se duas regiões de igual área
estiverem em lados opostos, o resultado fica a meio — não salta para uma nem
para a outra.

Alternativas comuns e porque não foram usadas:

| Método | Comportamento | Problema para este sistema |
|---|---|---|
| **MOM** (Mean of Maxima) | Usa só o pico da região mais alta | Ignora as outras regras que também dispararam — perde a contribuição da assimetria |
| **SOM/LOM** (Smallest/Largest of Maxima) | Usa o extremo esquerdo/direito do pico | Demasiado drástico; pequenas variações de entrada dão saltos no output |
| **COG** | Pondera toda a área acumulada | Suave e contínuo — transições graduais entre obras adequadas e inadequadas, que é exatamente o que se quer |

O COG é o método padrão para sistemas Mamdani que modelam grandezas contínuas
(como "adequação") onde se quer uma resposta proporcional e sem descontinuidades.

#### 2.3.5 As 9 regras (RULEBLOCK No1)

```
AND  : MIN    (a conjunção usa o mínimo dos graus de pertença)
ACT  : MIN    (ativação por clipping — corta o termo de saída)
ACCU : MAX    (acumulação por máximo)

// BEGINNER: obras fáceis são ideais; dificuldade sobe → penaliza rápido
RULE 1: IF studentLevel IS beginner     AND workDifficulty IS easy   THEN suitability IS high
RULE 2: IF studentLevel IS beginner     AND workDifficulty IS medium THEN suitability IS low
RULE 3: IF studentLevel IS beginner     AND workDifficulty IS hard   THEN suitability IS veryLow

// INTERMEDIATE: obras médias são ideais
// ASSIMETRIA: easy → medium (0.6) é mais aceitável que hard → low (0.3)
RULE 4: IF studentLevel IS intermediate AND workDifficulty IS easy   THEN suitability IS medium
RULE 5: IF studentLevel IS intermediate AND workDifficulty IS medium THEN suitability IS high
RULE 6: IF studentLevel IS intermediate AND workDifficulty IS hard   THEN suitability IS low

// ADVANCED: obras difíceis são ideais
RULE 7: IF studentLevel IS advanced     AND workDifficulty IS easy   THEN suitability IS low
RULE 8: IF studentLevel IS advanced     AND workDifficulty IS medium THEN suitability IS medium
RULE 9: IF studentLevel IS advanced     AND workDifficulty IS hard   THEN suitability IS high
```

#### 2.3.6 Exemplo de cálculo

Aluno `INTERMEDIATE` (3.5) + motivação `HIGH` (+1.0) = 4.5. Obra dif 5.

**Fuzzificação das entradas:**

| Termo | studentLevel=4.5 | Cálculo |
|---|---|---|
| beginner | **0** | 4.5 > 3.5 (fim do declive) |
| intermediate | **0.5** | descida do platô: (5−4.5)/(5−4) = 0.5 |
| advanced | **0.5** | subida da rampa: (4.5−3.5)/(5.5−3.5) = 0.5 |

| Termo | workDifficulty=5 | Cálculo |
|---|---|---|
| easy | **0** | 5 > 3.5 |
| medium | **0** | 5 = extremo direito do trapézio (2,0)(3,1)(4,1)(5,0) |
| hard | **1.0** | 5 está no início do platô (5,1)(6,1) |

**Regras que disparam:**

| Regra | studentLevel | workDifficulty | Força = MIN | Saída |
|---|---|---|---|---|
| R5 | intermediate 0.5 | medium **0** | 0 | high (não dispara) |
| R6 | intermediate **0.5** | hard **1.0** | **0.5** | low (clipped a 0.5) |
| R8 | advanced **0.5** | medium **0** | 0 | medium (não dispara) |
| R9 | advanced **0.5** | hard **1.0** | **0.5** | high (clipped a 0.5) |

> **Nota:** O `advanced` vale 0.5 (não 1.0) porque a rampa só atinge 1.0 em 5.5,
> e o aluno está em 4.5 — a meio do caminho.

**Acumulação (MAX):** low(0.5) + high(0.5) → ambos os termos contribuem com igual força.

**Defuzzificação (COG):** as duas regiões simétricas centram-se ≈ **0.60**.

Este valor (> 0.5) passa o threshold e a obra entra na sessão Drools com
CF inicial ≈ 0.60.

#### 2.3.7 Threshold e filtragem

```java
SUITABILITY_THRESHOLD = 0.5
```

Obras com suitability < 0.5 são descartadas. Não entram na sessão Drools.
Para um aluno intermédio (3.5), isto significa:

| Dificuldade | Suitability ~ | Passa? |
|---|---|---|
| 1-2 (easy) | ~0.50 | ✅ (borderline) |
| 3-4 (medium) | ~0.63 | ✅ |
| 5-6 (hard) | ~0.30 | ❌ |

### 2.4 STEP 3 — Sessão Drools (factos e regras)

**Classe:** `org.sibac.bassoon.RecommendationEngine`

Para cada obra que passou o filtro difuso, o motor:

1. **Insere o facto `Work`** — a obra do catálogo (não tem CF; é um dado certo)
2. **Insere `Hypothesis("candidate", nomeObra, suitability)`** — a candidatura,
   com CF inicial igual à suitability do fuzzy
3. **Insere as `Evidence`** do professor:

| EvidenceType | Valor | CF |
|---|---|---|
| `SKILL_1` | `Skill.LEGATO` | 0.9 (confiança do professor) |
| `SKILL_2` | `Skill.STACCATO` | 0.7 |
| `LAST_ERA` | `Era.BAROQUE` | 1.0 |
| `PREFERRED_ACCOMPANIMENT` | `Accompaniment.ORCHESTRA` | 0.8 |

Depois chama `kSession.fireAllRules()`.

### 2.5 Como as regras ajustam o CF

#### 2.5.1 Anatomia de uma regra de skill

Cada nível de skill tem a sua regra. Exemplo — `skill HIGH`:

```drl
rule "skill HIGH"
@CF(0.5)                     ← CF da regra (contribuição máxima se evidência CF=1.0)
lock-on-active true          ← evita reativações quando o facto Hypothesis é updated
when
    Evidence( description == EvidenceType.SKILL_1
              || description == EvidenceType.SKILL_2
              || description == EvidenceType.SKILL_3,
              $skill : value )          ← a skill que o professor pediu
    Work( $name : name,
          skillLevelFor($skill) == SkillLevel.HIGH )  ← a obra tem HIGH nessa skill
then
    Hypothesis $h = TrackingAgendaListener.getFactRef(
        Hypothesis.class, "candidate", $name);
    $h.update();                 ← aplica MYCIN (via TrackingAgendaListener)
end
```

O padrão repete-se para todos os 7 níveis de `SkillLevel` (REFERENCE, HIGH,
MEDIUM_HIGH, MEDIUM, MEDIUM_LOW, LOW, AVOID), cada um com o seu `@CF`.

#### 2.5.2 Como o `$h.update()` funciona

O `TrackingAgendaListener` interceta cada regra **antes** de o RHS correr
(`beforeMatchFired`) e captura: (a) os factos do LHS, (b) o `@CF` da regra,
(c) o nome da regra.

O método `Hypothesis.update()` (chamado do RHS):

1. Obtém o `FactHandle` da Hypothesis na memória de trabalho via `getKieSession()`
2. Pede ao listener o **CF mínimo** entre os factos do LHS que implementam `CfFact`
   (a `Evidence`; `Work` NÃO implementa `CfFact`, portanto não entra no mínimo)
3. Multiplica esse mínimo pelo `@CF` da regra (**regra da cadeia mais fraca**):
   ```
   contribuição = min(CF dos factos CfFact do LHS) × @CF(regra)
   ```
4. Combina com o CF atual da Hypothesis usando a fórmula **MYCIN**:
   ```
   CF_novo = MYCIN.combine(CF_atual, contribuição)
   ```
5. Regista a regra disparada no `RuleFiredTracker` (para mostrar na UI)
6. Faz `session.update(handle, this)` para propagar a mudança na memória de trabalho

#### 2.5.3 Fórmula MYCIN

**Classe:** `org.sibac.bassoon.cf.Mycin`

```java
public static double combine(double oldCf, double newCf) {
    if (oldCf >= 0 && newCf >= 0)
        return oldCf + newCf * (1 - oldCf);           // ambos positivos
    if (oldCf <= 0 && newCf <= 0)
        return oldCf + newCf * (1 + oldCf);           // ambos negativos
    return (oldCf + newCf) / (1 - min(|oldCf|, |newCf|));  // sinais opostos
}
```

**Exemplo de propagação:**

Obra com CF inicial 0.60. Professor pediu `LEGATO` (CF 0.9). A obra tem
`LEGATO = HIGH` (@CF 0.5).

```
contribuição = min(0.9) × 0.5 = 0.45
CF novo = 0.60 + 0.45 × (1 - 0.60) = 0.60 + 0.18 = 0.78
```

Depois o professor também pediu `STACCATO` (CF 0.7). A obra tem
`STACCATO = MEDIUM` (@CF 0.1).

```
contribuição = min(0.7) × 0.1 = 0.07
CF novo = 0.78 + 0.07 × (1 - 0.78) = 0.78 + 0.015 = 0.795
```

Se o professor indicou `lastEra = BAROQUE` e a obra é Barroca:

```
contribuição = min(1.0) × (-0.30) = -0.30
CF novo = (0.795 + (-0.30)) / (1 - min(0.795, 0.30))
        = 0.495 / 0.70 = 0.707
```

> A evidência LAST_ERA não tem CF explícito — é inserida sem CF, portanto assume 1.0.
> O motor usa o terceiro ramo do MYCIN (sinais opostos): (old + new) / (1 − min(|old|, |new|)).

Se o acompanhamento coincide (`ORCHESTRA`, CF 0.8):

```
contribuição = min(0.8) × 0.5 = 0.40
CF novo = 0.707 + 0.40 × (1 - 0.707) = 0.707 + 0.117 = 0.824
```

**Score final: 0.824.**

#### 2.5.4 Todas as regras e CFs

| Regra | `@CF` | Ficheiro | Quando dispara |
|---|---|---|---|
| `skill REFERENCE` | +0.8 | `skills_rules.drl` | Obra é referência para skill pedida |
| `skill HIGH` | +0.5 | `skills_rules.drl` | Obra é muito boa para skill pedida |
| `skill MEDIUM_HIGH` | +0.3 | `skills_rules.drl` | Obra é boa para skill pedida |
| `skill MEDIUM` | +0.1 | `skills_rules.drl` | Obra trabalha a skill |
| `skill MEDIUM_LOW` | -0.1 | `skills_rules.drl` | Obra é fraca para skill pedida |
| `skill LOW` | -0.4 | `skills_rules.drl` | Obra é má para skill pedida |
| `skill AVOID` | -0.8 | `skills_rules.drl` | Obra é péssima para skill pedida |
| `era penalty` | -0.3 | `last_era_rules.drl` | Época = última estudada |
| `accompaniment match` | +0.5 | `accompaniment_rules.drl` | Acompanhamento = preferido |

### 2.6 STEP 4 — Ordenação e pós-processamento

#### 2.6.1 Ordenação

Após `fireAllRules()`, as candidaturas (`Hypothesis`) são recolhidas e
ordenadas por CF decrescente.

#### 2.6.2 Filtro de score mínimo

```java
MIN_RECOMMENDATION_SCORE = 0.30
```

Obras com CF final < 0.30 são descartadas. Remove ruído — obras que passaram
o fuzzy mas foram fortemente penalizadas pelas regras (ex: várias skills AVOID).

#### 2.6.3 Pré-requisitos

Se uma obra tem `prerequisiteId` (ex: Sonatine ID 15 → prerequisiteId = 14,
a Suite), e a Suite está na lista mas **depois** da Sonatine, o motor move-a
para antes. É uma reordenação em Java puro — **não é regra Drools**.

O algoritmo `applyPrerequisites` usa um loop `do-while` que repete enquanto
houver movimentos: garante que cadeias transitivas (A→B→C) são corretamente
resolvidas numa única passagem.

Exemplo:
```
[Sonatine 0.80, Concerto 0.75, Suite 0.65]   ← ordenado por CF
                         ↓ applyPrerequisites
[Suite 0.65, Sonatine 0.80, Concerto 0.75]    ← Suite movida para antes
```

> Ordem de execução: 1) ordenação por CF, 2) `applyPrerequisites`, 3) filtro de score mínimo.

### 2.7 STEP 5 — Justificação via LLM

**Classes:** `JustificationService`, `GroqClient`, `PtLabels`

#### 2.7.1 Construção de factos

Para cada obra recomendada, o serviço constrói factos em Português:

| Facto | Exemplo |
|---|---|
| Dificuldade | `Dificuldade 4/6.` |
| Skill (REFERENCE) | `Excelente para Legato (obra de referencia).` |
| Skill (HIGH) | `Muito boa para Staccato.` |
| Skill (MEDIUM_HIGH) | `Boa para Coordenacao.` |
| Skill (MEDIUM) | `Razoavel para Trilos.` |
| Skill (MEDIUM_LOW) | `Fraca para Flicking.` |
| Skill (LOW) | `Ma para Dinamicas.` |
| Skill (AVOID) | `Pessima para Tecnicas contemporaneas.` |
| Acompanhamento (match) | `Acompanhamento Orquestra (preferido pelo professor).` |
| Acompanhamento (mismatch) | `Acompanhamento Piano — diferente do preferido (Orquestra).` |
| Época (igual) | `ATENCAO: Barroco — mesma epoca da ultima obra. Perdeu prioridade.` |
| Época (diferente) | `Epoca Classico — diferente da ultima estudada. Favorece variedade.` |
| Pré-requisito (direto) | `Estudar "Suite" antes pode ser uma boa preparacao para esta obra.` |
| Pré-requisito (reverso) | `Boa preparacao para "Sonatine" — por isso aparece primeiro.` |

#### 2.7.2 Prompt do sistema

```
Es um professor de fagote. Para cada obra, recebes o que o professor
pediu e o que a obra oferece. Escreve um paragrafo natural — como se
falasses com um colega, nao como um robo a listar factos.

Cobre todos os pontos recebidos: skills, acompanhamento, epoca,
pre-requisitos. Mostra o balanco entre pontos fortes e fracos.

NAO inventes. NAO uses frases feitas ("e uma obra que", "permite
desenvolver", "ajuda a"). NAO uses adjetivos vagos.
Portugues europeu, COM ACENTOS. Escreve "não", nunca "nao".

[segue-se um exemplo de factos e resposta esperada]

Responde APENAS com JSON:
{"justificacoes": [{"obra": <numero>, "texto": "<justificacao>"}]}
```

#### 2.7.3 Pós-processamento

O método `clean()` remove frases proibidas que o LLM insiste em usar
("e uma obra que", "permite desenvolver", etc.) e corrige acentos em falta
("nao" → "não", "epoca" → "época").

#### 2.7.4 Modelo e API

- **Modelo:** `llama-3.3-70b-versatile` (Groq, tier gratuito)
- **Endpoint:** `https://api.groq.com/openai/v1/chat/completions`
- **Temperatura:** 0.3 (baixa, para respostas consistentes)
- **JSON mode:** ativo (`response_format: json_object`)

---

## 3. Classes do motor (bassoon-engine)

### 3.1 `model/` — Domínio

| Classe | Descrição |
|---|---|
| `Work` | Obra do catálogo. Campos: id, name, composer, era, country, accompaniment, difficultyLevel, videoLink, prerequisiteId, `Map<Skill, SkillLevel>` |
| `Skill` | Enum com 24 competências, organizadas em 7 grupos (Articulação, Registo, Tempo, Controlo do som, Desafios técnicos, Ritmo, Carácter) |
| `SkillLevel` | Enum com 7 níveis: `REFERENCE`, `HIGH`, `MEDIUM_HIGH`, `MEDIUM`, `MEDIUM_LOW`, `LOW`, `AVOID` |
| `StudentLevel` | `BEGINNER`, `INTERMEDIATE`, `ADVANCED` |
| `Motivation` | `LOW`, `NEUTRAL`, `HIGH` |
| `Era` | `BAROQUE`, `CLASSICAL`, `ROMANTIC`, `CONTEMPORARY`, `OTHER` |
| `Accompaniment` | `SOLO`, `PIANO`, `BASSO_CONTINUO`, `ORCHESTRA` |
| `DifficultyLevel` | `LEVEL_1` a `LEVEL_6` |
| `Evidence` | Facto de entrada do professor. Implementa `CfFact`. Campos: description (EvidenceType), value (Object tipado), cf (double). Tem construtor sem CF (assume 1.0 para factos determinísticos como `LAST_ERA`) |
| `EvidenceType` | Enum: `SKILL_1`, `SKILL_2`, `SKILL_3`, `LAST_ERA`, `PREFERRED_ACCOMPANIMENT`. **Nota:** `studentLevel` e `motivation` NÃO são factos Drools — são passados diretamente ao sistema fuzzy antes da sessão |
| `Hypothesis` | Conclusão intermédia. Implementa `CfFact`. `update()` aplica MYCIN usando o `TrackingAgendaListener` |
| `CfFact` | Interface: `double getCf()`. Permite ao listener saber que factos têm CF sem obrigar `Work` a ter |

### 3.2 `cf/` — Motor de factores de confiança

| Classe | Descrição |
|---|---|
| `Mycin` | Fórmula MYCIN. Método estático `combine(oldCf, newCf)`. Comutativa e associativa — a ordem das regras não afeta o resultado final |
| `TrackingAgendaListener` | Implementa `AgendaEventListener`. Actua como **ponte** entre Drools e o motor de CFs: no evento `beforeMatchFired` captura os factos do LHS, o `@CF` da regra e o nome da regra em campos estáticos (um pedido de cada vez). O RHS chama então `Hypothesis.update()` que lê esses campos para calcular a contribuição |
| `FactListener` | Implementa `RuleRuntimeEventListener`. Regista inserções/atualizações/remoções de factos em DEBUG — apenas para diagnóstico |
| `RuleFiredTracker` | Mapa estático `nomeDaObra → List<FiredRule>`. `record()` é chamado por `Hypothesis.update()`. Limpo entre pedidos com `clear()` |

### 3.3 `fuzzy/` — Lógica difusa

| Classe | Descrição |
|---|---|
| `FuzzySuitabilityService` | Carrega `suitability.fcl` via classpath. `suitability(studentLevel, workDifficulty)` devolve double [0..1]. `synchronized` porque o FIS tem estado interno |
| `StudentLevelMapper` | `toStudentLevel(StudentLevel, Motivation)` → double. Conversão determinística |

### 3.4 `kb/` — Base de conhecimento

| Classe | Descrição |
|---|---|
| `KnowledgeBase` | 31 obras. Método estático `works()` devolve `List<Work>`. Populada a partir de `obras_fagote_v5.xlsx` (escala de 7 níveis, distribuição artificial) |

### 3.5 `output/` — Resultados

| Classe | Descrição |
|---|---|
| `Recommendation` | Obra recomendada: workId, workName, score (CF final), initialScore (suitability), justification, firedRules |
| `FiredRule` | Regra que disparou: name, cf, category (SKILL/ACCOMPANIMENT/ERA), detail |

---

## 4. Classes da API (bassoon-api)

| Classe | Descrição |
|---|---|
| `BassoonApiApplication` | `@SpringBootApplication` — entry point |
| `RecommendationController` | `@RestController`. `POST /recommend` — recebe `RecommendationRequest`, devolve `RecommendationResponse`. CORS aberto para dev (localhost:5173) |
| `DroolsService` | Converte DTO → inputs do motor, corre `engine.run()`, enriquece com metadados do `WorkCatalog` |
| `WorkCatalog` | Carrega `KnowledgeBase.works()`. Indexado por ID: `byId(double)`. Partilhado por `DroolsService` e `JustificationService` |
| `JustificationService` | Constrói factos, chama LLM, pós-processa. Injetado com `WorkCatalog` e `GroqClient` |
| `GroqClient` | HTTP client para Groq API. `generateJson(systemInstruction, userPrompt)` devolve String |
| `PtLabels` | Etiquetas PT para enums: `skill()`, `skillLevelPhrase()`, `era()`, `accompaniment()` |
| `RecommendationRequest` | DTO de entrada (Lombok `@Data`). Contém inner classes `SkillPreference` e `AccompanimentPreference` |
| `RecommendationResponse` | DTO de saída (Lombok `@Data`). Contém inner class `RecommendedWork` |

---

## 5. Configuração (valores ajustáveis)

### 5.1 Thresholds

| Parâmetro | Valor | Local |
|---|---|---|
| Fuzzy threshold | 0.5 | `RecommendationEngine.SUITABILITY_THRESHOLD` |
| Score mínimo | 0.30 | `RecommendationEngine.MIN_RECOMMENDATION_SCORE` |
| Motivation shift | ±1.0 | `StudentLevelMapper.MOTIVATION_SHIFT` |

### 5.2 CFs das regras

| Regra | CF | Ficheiro |
|---|---|---|
| `skill REFERENCE` | +0.8 | `skills_rules.drl` |
| `skill HIGH` | +0.5 | `skills_rules.drl` |
| `skill MEDIUM_HIGH` | +0.3 | `skills_rules.drl` |
| `skill MEDIUM` | +0.1 | `skills_rules.drl` |
| `skill MEDIUM_LOW` | -0.1 | `skills_rules.drl` |
| `skill LOW` | -0.4 | `skills_rules.drl` |
| `skill AVOID` | -0.8 | `skills_rules.drl` |
| `era penalty` | -0.3 | `last_era_rules.drl` |
| `accompaniment match` | +0.5 | `accompaniment_rules.drl` |

### 5.3 Conversão Excel → SkillLevel (v5, opção A)

| Intervalo original | Nível |
|---|---|
| +0.8 a +1.0 | `REFERENCE` |
| +0.6 a +0.7 | `HIGH` |
| +0.4 a +0.5 | `MEDIUM_HIGH` |
| +0.1 a +0.3 | `MEDIUM` |
| -0.2 a 0.0 | `MEDIUM_LOW` |
| -0.6 a -0.3 | `LOW` |
| -1.0 a -0.7 | `AVOID` |

---

## 6. Técnicas de IA utilizadas

| Técnica | Onde | Porquê |
|---|---|---|
| **Lógica difusa** (Mamdani) | Apenas no cálculo da suitability (FCL) | A adequação obra/aluno não tem fronteira clara; o resto do sistema (skills, época, acompanhamento) usa CFs, não fuzzy |
| **Factores de confiança** (MYCIN) | Regras Drools | Combinar múltiplas evidências com incerteza; propagação comutativa e associativa |
| **Sistema pericial** (Drools) | Motor de regras (.drl) | Separar conhecimento declarativo do código imperativo; regras independentes e modificáveis |
| **LLM** (Large Language Model) | Justificações (Groq) | Gerar texto pedagógico natural em Português a partir de factos estruturados |

---

## 7. Base de conhecimento

31 obras de fagote, validadas pelo perito (Prof. Carolino Carreira).

**Por obra:**
- 9 metadados (ID, nome, compositor, época, país, acompanhamento, dificuldade 1-6, pré-requisito, videoLink YouTube)
- 24 competências pontuadas em 7 níveis

**6 relações de pré-requisito:**
- Kreutzer → Crusell (ID 26 → ID 6)
- Weber Op.75 → Andante e Rondo (ID 7 → ID 8)
- Suite → Sonatine (ID 14 → ID 15)
- Niggun → Hopi (ID 23 → ID 24)
- Don Pasquale → Lucia (ID 27 → ID 28)
- Lucia → Rossini (ID 28 → ID 29)

---

## 8. Stack tecnológica

| Camada | Tecnologia | Versão |
|---|---|---|
| Linguagem | Java | 17 |
| Motor de regras | Drools | 7.74.1.Final |
| Lógica difusa | jFuzzyLogic | 3.3 |
| API REST | Spring Boot | 3.3.0 |
| LLM | Groq API (llama-3.3-70b) | — |
| Interface | React + Vite | 18 / 5 |
| Build | Maven (multi-módulo) | — |

---

## 9. Justificações de design — perguntas esperadas na defesa

### 9.1 "Porque é que a lógica difusa só é usada num sítio?"

O sistema lida com dois tipos de incerteza distintos, e cada um tem o tratamento certo:

| Tipo de incerteza | Exemplo no sistema | Tratamento correto |
|---|---|---|
| **Vaguidade** — conceitos sem fronteira nítida | "Esta obra tem a dificuldade certa para este aluno?" | **Lógica difusa** |
| **Incerteza epistémica** — confiança na evidência | "O professor acha que o aluno precisa de LEGATO, mas talvez não a 100%" | **Factores de confiança (MYCIN)** |

Usar fuzzy para as skills seria errado: a obra ou é referência para LEGATO ou não é — o perito
atribuiu 7 níveis discretos a cada par (obra, skill) de forma deliberada. Não há vaguidade
no conceito; a incerteza é sobre *o que o professor pediu*, não sobre a classificação da obra.
O MYCIN foi criado exatamente para este tipo de incerteza epistémica (diagnóstico médico).

Usar fuzzy para a época ou o acompanhamento seria igualmente errado: são categorias discretas,
não grandezas contínuas.

A adequação dificuldade/nível é genuinamente vaga porque um aluno de nível 3.4 e um de nível 3.6
não são fundamentalmente diferentes — não existe uma fronteira onde "passa" e "não passa". É aqui
que a lógica difusa tem razão de existir.

---

### 9.2 "O input do professor sobre o nível do aluno não devia ser também fuzzy?"

Esta é uma limitação real do sistema, e vale a pena reconhecê-la honestamente.

**O que o sistema faz:** o professor escolhe `INTERMEDIATE` + `HIGH` → o `StudentLevelMapper`
converte deterministicamente para **4.5** → esse número entra no sistema fuzzy.

O 4.5 é um número **certo**, sem incerteza. Só depois é que o fuzzy o fuzzifica:
intermediate=0.5, advanced=0.5.

**Porque a tensão existe:** quando o professor diz "INTERMEDIATE", isso é em si um conceito
vago — não há uma fronteira nítida entre intermédio e avançado. Uma abordagem mais "puramente
fuzzy" deixaria o professor expressar graus de pertença directamente, por exemplo
"intermédio a 0.7, avançado a 0.3". Isso não foi modelado.

**Porque o design actual ainda funciona:**

O `StudentLevelMapper` coloca o aluno num eixo contínuo (0.5–6.5), e o fuzzy já faz a
transição gradual sobre esse valor:

```
INTERMEDIATE + LOW  → 2.5 → beginner=0.5, intermediate=0.5
INTERMEDIATE        → 3.5 → intermediate=1.0
INTERMEDIATE + HIGH → 4.5 → intermediate=0.5, advanced=0.5
```

A motivação serve exactamente para o professor expressar "este aluno está mais para o lado
avançado do intermédio". O fuzzy absorve essa imprecisão no passo seguinte.

**O que não se modelou:** se o professor diz INTERMEDIATE mas *não tem a certeza* da categoria
— se está genuinamente em dúvida entre intermédio e avançado — essa incerteza sobre a
*escolha da categoria em si* não é capturada. Seria necessário um input de CF sobre o nível
(como existe para as skills), o que aumentaria a complexidade da UI.

---

### 9.3 "Porque é que as regras usam `lock-on-active true`?"

Sem `lock-on-active`, quando `$h.update()` chama `session.update(handle, this)`, o Drools
reinsere a Hypothesis na agenda. Isso poderia reativar a mesma regra com os mesmos factos,
criando um loop infinito de atualizações.

`lock-on-active true` diz ao Drools: "enquanto esta regra estiver ativa na agenda, não a
reative — mesmo que os factos do LHS se alterem". Garante que cada regra dispara **exatamente
uma vez** por combinação (Evidence, Work).

---

### 9.4 "Porque é que o MYCIN é comutativo? Não devia importar a ordem?"

A fórmula MYCIN tem a propriedade matemática de ser comutativa e associativa — combinar
evidência A depois de B dá o mesmo resultado que B depois de A. Isto é deliberado: num
sistema pericial, a ordem em que as regras disparam não deve influenciar a conclusão final.

O Drools não garante ordem de disparo entre regras independentes (a menos que se use
`salience`), por isso a comutatividade do MYCIN é uma garantia de correcção do motor.

---

### 9.5 "O LLM não pode inventar recomendações?"

Não — o LLM apenas recebe **factos estruturados** gerados pelo motor (dificuldade, resultado
de cada skill, época, acompanhamento, pré-requisitos) e é instruído explicitamente a não
inventar. As obras a apresentar, a sua ordem e os factos que as descrevem são 100%
determinados pelo motor de inferência. O LLM só transforma esses factos em linguagem natural.

Se o LLM falhar (erro de rede, resposta inválida), o sistema apresenta um fallback textual
sem quebrar — a recomendação em si não é afectada.

---

### 9.6 "Porque é que o motor usa uma sessão Drools nova por pedido?"

O `TrackingAgendaListener` e o `RuleFiredTracker` usam estado estático (campos `static`).
Uma sessão partilhada entre pedidos causaria condições de corrida e resultados misturados.

Criar uma sessão nova por pedido (`kieContainer.newKieSession`) e chamar `dispose()` no fim
garante isolamento total. O custo de criação é baixo (o `KieContainer` já tem as regras
compiladas em memória).

---

### 9.7 "Como é que o sistema lida com uma obra sem avaliação para uma skill?"

Se o perito não avaliou um par (obra, skill) na base de conhecimento, o método
`Work.getSkillLevel(skill)` devolve **`MEDIUM`** por omissão:

```java
return skills.getOrDefault(skill, SkillLevel.MEDIUM);
```

Isto significa que a obra recebe um boost neutro baixo (+0.1 CF) para essa skill — nem
penaliza nem favorece. É uma decisão conservadora: na dúvida, não se penaliza a obra por
falta de dados.
