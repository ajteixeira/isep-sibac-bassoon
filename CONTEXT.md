# Bassoon - Sistema Pericial de Recomendação de Repertório de Fagote

Projeto desenvolvido no âmbito da UC Sistemas Baseados em Conhecimento (SIBAC),
mestrado em Engenharia Informática.

Grupo: Inês Lemos (1231832), Jorge Teixeira (1900106), Rafael Fonseca (1211622).
Perito do domínio: Carolino Carreira (professor de fagote do ensino superior).

> Este documento descreve a **implementação atual** do sistema. Sempre que houver
> dúvida, o código é a fonte de verdade.

---

## Em uma frase

O professor descreve o aluno e os objetivos pedagógicos num formulário; o sistema
combina **lógica difusa** (para a adequação obra/aluno) com **fatores de confiança**
(para competências, época e acompanhamento) e devolve uma lista ordenada de obras,
com uma justificação em linguagem natural gerada por um LLM.

---

## Stack (versões reais)

| Camada | Tecnologia | Versão |
|--------|------------|--------|
| Linguagem | Java | 17 |
| Motor de regras | Drools | 7.74.1.Final |
| Lógica difusa | jFuzzyLogic | 3.3 (não está no Maven Central, ver README) |
| API REST | Spring Boot | 3.3.0 |
| LLM | Groq (API compatível com OpenAI) | modelo `llama-3.1-8b-instant` |
| Interface | React + Vite | React 18, Vite 5 |
| Build Java | Maven (multi-módulo) + Spotless (Google Java Format) | - |

> Nota: o `pom` está em Spring Boot 3.3.0, não 4. Ver secção "Pontos em aberto".

---

## Estrutura do repositório

```
bassoon/
├── bassoon-engine/   Motor de inferência: Drools + lógica difusa + modelos + base de conhecimento
├── bassoon-api/      Spring Boot 3.3 - REST API + integração com o LLM
└── bassoon-ui/       React 18 + Vite - formulário do professor (wizard por passos)
```

O `bassoon-ui` é gerido por npm, não por Maven. Os módulos Java são `bassoon-engine`
(packaging `kjar`, por causa do Drools) e `bassoon-api` (packaging `jar`).

---

## Arquitetura e fluxo

```
Professor preenche o formulário (React, wizard de 7 passos)
        │
        ▼
POST /recommend  (Spring Boot - RecommendationController)
        │
        ▼
DroolsService converte o pedido (DTO) nos inputs do RecommendationEngine
        │
        ▼
RecommendationEngine.run(...)  ── o coração do sistema:
   1. StudentLevelMapper: nível + motivação -> valor contínuo no eixo 1..6
   2. FUZZY (suitability.fcl): adequação de cada obra ao aluno -> grau 0..1
      (threshold 0.4 filtra as obras pouco adequadas)
   3. PONTE: o grau difuso vira o CF inicial de cada candidatura
      Hypothesis("candidate", nomeObra, grau)
   4. Drools fireAllRules(): regras de CF (competências, época, acompanhamento)
      ajustam o CF de cada candidatura
   5. Ordenação (Java): recolhe as candidaturas e ordena por CF decrescente
   6. Pré-requisitos (Java): reordena para garantir que as obras pré-requisito aparecem antes das que delas dependem
        │
        ▼
JustificationService monta um prompt com as obras + regras que dispararam
        │
        ▼
GroqClient chama o LLM -> justificação pedagógica em português (PT-PT)
        │
        ▼
RecommendationResponse devolvido ao React (lista de obras + justificação)
```

---

## As duas técnicas de raciocínio

Esta é a decisão de desenho central, e substitui a versão antiga (que era só fatores
de confiança com uma tabela rígida nível -> dificuldade).

### 1. Front-end difuso: adequação obra/aluno

A pergunta "esta obra tem a dificuldade certa para este aluno?" não tem uma fronteira
clara, por isso é tratada com lógica difusa.

Ficheiro: `bassoon-engine/src/main/resources/fuzzy/suitability.fcl` (sistema Mamdani).

Entradas:
- `studentLevel`: nível do aluno já ajustado pela motivação, no eixo contínuo 0.5..6.5
- `workDifficulty`: dificuldade da obra, no eixo 1..6 (vem do campo `DifficultyLevel`)

Saída:
- `suitability`: grau de adequação 0..1

Termos:
- `studentLevel`: beginner, intermediate, advanced
- `workDifficulty`: easy, medium, hard
- `suitability`: veryLow, low, medium, high

As 9 regras do RULEBLOCK codificam o conhecimento do perito sobre **assimetria**: é mais
aceitável o aluno tocar uma obra um nível **abaixo** do que um nível **acima**. Por isso,
para um aluno intermédio, uma obra fácil dá adequação "medium" enquanto uma obra difícil
dá apenas "low".

O `StudentLevelMapper` (em `fuzzy/`) faz a conversão do nível categórico para o eixo
contínuo, deixando a motivação deslocar o aluno um ponto para cima (HIGH) ou para baixo
(LOW):

```
BEGINNER     -> 1.5      (banda 1-2)
INTERMEDIATE -> 3.5      (banda 3-4)
ADVANCED     -> 5.5      (banda 5-6)
motivação HIGH  = +1.0   |  LOW = -1.0  |  NEUTRAL = 0
resultado limitado ao intervalo [0.5, 6.5]
```

Exemplo: aluno INTERMEDIATE com motivação HIGH -> 3.5 + 1.0 = 4.5 no eixo difuso.

### 2. Back-end de fatores de confiança: ajuste do CF

Depois do fuzzy, cada obra que passou o threshold entra na sessão Drools como uma
candidatura (`Hypothesis("candidate", nome, grauFuzzy)`). As regras de CF ajustam esse
CF inicial. Há três ficheiros de regras, em `bassoon-engine/src/main/resources/rules/`:

| Ficheiro | Regra(s) | @CF | O que faz |
|----------|----------|-----|-----------|
| `skills_rules.drl` | skill REFERENCE | 1.00 | Obra de referência para uma competência pedida |
| | skill HIGH | 0.70 | Trabalha bem a competência |
| | skill MEDIUM | 0.30 | Trabalha a competência, mas há melhores |
| | (LOW) | sem regra | Não contribui (CF = 0) |
| | skill NONE | -0.50 | Desaconselhada para a competência (penaliza) |
| `last_era_rules.drl` | era penalty | -0.30 | Penaliza obras da mesma época da última estudada (favorece variedade) |
| `accompaniment_rules.drl` | accompaniment match | 0.30 | Reforça obras com o acompanhamento preferido pelo professor |

Todas as regras seguem o mesmo molde (baseado na ficha 4 do professor):

```drl
rule "skill HIGH"
@CF(0.7)
lock-on-active true
when
    Evidence( description == EvidenceType.SKILL_1
              || description == EvidenceType.SKILL_2
              || description == EvidenceType.SKILL_3,
              $skill : value )
    Work( $name : name, skillLevelFor($skill) == SkillLevel.HIGH )
then
    Hypothesis $h = TrackingAgendaListener.getFactRef(Hypothesis.class, "candidate", $name);
    $h.update();
end
```

> Detalhe de Drools: não se usa cast dentro das constraints (`getSkillLevel((Skill)$skill)`)
> porque o parser tropeça. Por isso `Work` expõe `skillLevelFor(Object)`, que faz o cast em
> Java. Pela mesma razão usa-se `==` com `||` em vez de `in (...)` com constantes de enum.

---

## Mecanismo dos fatores de confiança (pacote `cf/`)

O CF propaga-se exatamente como na ficha 4 do professor, com uma melhoria nossa para
suportar factos sem CF no LHS (a `Work`):

- `@CF(valor)` na regra define o CF dessa regra.
- `TrackingAgendaListener` interceta cada disparo (`beforeMatchFired`): guarda os factos
  que casaram no LHS, o CF da regra e o nome da regra.
- `getLHSminimumCF(...)` devolve o CF mínimo entre os factos do LHS **que têm CF** (o elo
  mais fraco). Só conta factos que implementam a interface `CfFact` (`Evidence` e
  `Hypothesis`); a `Work` não a implementa, por isso o catálogo não baixa a confiança.
- `Hypothesis.update()` (chamado no RHS) calcula:
  ```
  contribuição = min(CF dos factos com CF no LHS) * CF(regra)
  CF novo da candidatura = MYCIN(CF atual, contribuição)
  ```
  e faz `session.update(...)` para a mudança ser visível às outras regras.
- `RuleFiredTracker` regista que regras dispararam para cada obra (para depois aparecerem
  na justificação).
- `Mycin.combine(antigo, novo)` isola a fórmula clássica:

```
ambos >= 0:        antigo + novo * (1 - antigo)
ambos <= 0:        antigo + novo * (1 + antigo)
sinais contrários: (antigo + novo) / (1 - min(|antigo|, |novo|))
```

> O estado do listener e do tracker é estático (igual ao professor). Só é seguro com um
> pedido de cada vez. A API cria uma `KieSession` nova por pedido e faz `dispose()` no fim.

---

## Pós-processamento em Java (ordenação e pré-requisitos)

Ficam em Java, no `RecommendationEngine`, porque não são inferências:

- **Ordenação:** recolhe as `Hypothesis` de candidatura e ordena por CF decrescente.
- **Pré-requisitos:** se uma obra tem `prerequisiteId`, garante que a obra pré-requisito
  aparece **antes** dela na lista (só reordena, não inventa nem remove).

---

## Como o desenho antigo (relatório) mapeia para o código atual

O relatório prévio fala de regras R1 a R7. Esta é a correspondência com a implementação:

| Relatório | Implementação atual |
|-----------|---------------------|
| R1 (nível -> dificuldade) | Substituída pelo front-end difuso (`suitability.fcl` + `StudentLevelMapper`) |
| R2 (motivação corrige a faixa) | Substituída: a motivação entra como deslocamento no `StudentLevelMapper` |
| Filtro de candidatos | Threshold de adequação difusa (0.4) no `RecommendationEngine` |
| R4 (competências) | `skills_rules.drl` (4 regras, uma por `SkillLevel`) |
| R6 (repetição de época) | `last_era_rules.drl` (era penalty) |
| (novo) preferência de acompanhamento | `accompaniment_rules.drl` (accompaniment match) |
| Ordenação | Java, no `RecommendationEngine` |
| Pré-requisitos | Java, no `RecommendationEngine` |

---

## Modelo (bassoon-engine, pacote `model/`)

Todo o código está em inglês. Só os comentários ficam em português.

### Enums

| Enum | Valores |
|------|---------|
| `StudentLevel` | BEGINNER, INTERMEDIATE, ADVANCED |
| `Motivation` | LOW, NEUTRAL, HIGH |
| `Era` | BAROQUE, CLASSICAL, ROMANTIC, CONTEMPORARY, OTHER |
| `Accompaniment` | SOLO, PIANO, BASSO_CONTINUO, ORCHESTRA |
| `DifficultyLevel` | LEVEL_1 .. LEVEL_6 (cada um com valor inteiro 1..6) |
| `SkillLevel` | REFERENCE, HIGH, MEDIUM, LOW, NONE |
| `Skill` | 24 competências (ver tabela abaixo) |
| `EvidenceType` | STUDENT_LEVEL, SKILL_1, SKILL_2, SKILL_3, MOTIVATION, LAST_ERA, PREFERRED_ACCOMPANIMENT |

### Competências (24, definidas pelo perito)

| Grupo | Competências (Skill) |
|-------|----------------------|
| Articulação | LEGATO, STACCATO |
| Registo | LOW_REGISTER, MID_REGISTER, HIGH_REGISTER, VERY_HIGH_REGISTER |
| Tempo de execução | SLOW_TEMPO, MODERATE_TEMPO, FAST_TEMPO, VIRTUOSO_TEMPO |
| Controlo do som | ENDURANCE, SOUND_QUALITY, FLEXIBILITY, INTONATION, DYNAMICS |
| Desafios técnicos | COORDINATION, FLICKING, TRILLS, ORNAMENTATION, HALF_HOLE_TECHNIQUE, CONTEMPORARY_TECHNIQUES |
| Ritmo | RHYTHMIC_COMPLEXITY |
| Carácter | TECHNICAL_CHARACTER, EXPRESSIVE_CHARACTER |

### Classes principais

- `Work` (pacote `kb` usa, modelo em `model/`): obra do catálogo. Campos: id, name, composer,
  era, country, accompaniment, difficultyLevel, videoLink, prerequisiteId,
  `Map<Skill, SkillLevel> skills`. `getSkillLevel(skill)` devolve `LOW` se a competência não
  estiver definida; `skillLevelFor(Object)` é a versão usada nas regras.
- `Evidence` (implementa `CfFact`): input do professor. `EvidenceType` + valor tipado (Object)
  + CF. Tem construtor sem CF (assume 1.0) para factos determinísticos como o nível.
- `Hypothesis` (implementa `CfFact`): conclusão intermédia. Aqui usa-se sempre a descrição
  `"candidate"` com o nome da obra. `update()` faz a propagação de CF descrita acima.
- `CfFact`: interface fina `{ double getCf(); }` (com `compareTo` por CF). Permite ao listener
  saber que factos têm CF sem obrigar a `Work` a tê-lo.
- `Recommendation` (pacote `output/`): resultado final (workName, score, justification,
  firedRules).

---

## Base de conhecimento (bassoon-engine, `kb/KnowledgeBase.java`)

Método estático `KnowledgeBase.works()` que devolve a lista de obras. **Estado atual: 2 obras
de exemplo** com todas as 24 competências preenchidas:

- Sonata em Fá menor (Telemann, BAROQUE, BASSO_CONTINUO, LEVEL_1)
- Concerto in B♭, K. 191 (Mozart, CLASSICAL, ORCHESTRA, LEVEL_5)

O objetivo é chegar às 31 obras da Tabela 9 do relatório, mas para validar o motor estas
duas chegam.

---

## API REST (bassoon-api)

Endpoint único: `POST /recommend` (`RecommendationController`), com CORS aberto para o
servidor de desenvolvimento do React (`http://localhost:5173`).

Fluxo do controller: chama o `DroolsService` (motor) e depois o `JustificationService` (LLM),
e devolve os dois resultados juntos.

### DTOs

O contrato JSON com a interface usa os **nomes** dos campos em inglês (iguais aos do modelo
Java). A UI traduz as etiquetas para português, mas os valores dos enums são enviados em inglês.

`RecommendationRequest`:
```
studentLevel    : StudentLevel
skills          : [ { skill: Skill, cf: double } ]        (até 3)
motivation      : Motivation        (opcional)
accompaniments  : [ { type: Accompaniment, cf: double } ] (opcional)
lastEra         : Era               (opcional)
```

`RecommendationResponse`:
```
recommendations : [ RecommendedWork ]

RecommendedWork = { workName, composer, era, country, difficulty,
                    accompaniment, score, videoLink, prerequisite, firedRules,
                    justification }
```

### Serviços

- `DroolsService`: carrega a KB no arranque, converte o DTO nos inputs do
  `RecommendationEngine`, corre o pipeline, e enriquece cada resultado com os metadados da
  obra (compositor, época, vídeo, pré-requisito...).
- `JustificationService` + `GroqClient`: montam um prompt com as obras e as regras que
  dispararam, e chamam a API do Groq. O `GroqClient` usa o `java.net.http.HttpClient`
  (sem SDK externo). O system instruction pede texto pedagógico em PT-PT, conciso, sem
  inventar factos.

### Configuração

`application.properties`:
```
server.port=8080
groq.api.key=${GROQ_API_KEY:}                       # vem do ambiente ou do perfil local
spring.web.cors.allowed-origins=http://localhost:5173
```

A chave real fica em `application-local.properties` (perfil `local`), que **não vai para o
Git**. Ver README para o setup.

---

## Interface (bassoon-ui)

React 18 + Vite. É um **wizard por passos** que recolhe o input do professor e mostra os
resultados:

```
StepIntro -> StepNivel -> StepCompetencias -> StepMotivacao -> StepAcompanhamento
          -> StepPeriodo -> StepResultados
```

Componentes de apoio: `Stepper`, `FooterNav`, `RadioRow`, `CFRange`, `CFSegmented`,
`DirectionPicker`. As etiquetas em português dos enums estão em `src/labels.js`.

A comunicação com a API está em `src/services/RecommendationService.js`: `buildRequest`
converte o estado do wizard no DTO, e faz `POST /recommend` (o proxy do Vite encaminha para
o backend). Há um `USE_MOCK` (atualmente `false`) e um `mockData.js` para desenvolvimento
sem backend; **esse mock está desatualizado** e não reflete o catálogo nem as regras atuais.

---

## Testes (bassoon-engine)

- `cf/MycinTest`: a fórmula MYCIN, em Java puro.
- `fuzzy/StudentLevelMapperTest` e `fuzzy/FuzzySuitabilityServiceTest`: o front-end difuso.
- `rules/SkillRulesTest`, `rules/LastEraRulesTest`, `rules/AccompanimentRulesTest`:
  integração Drools de cada família de regras.

---

## Pontos em aberto

- **Catálogo:** só 2 obras; faltam as restantes da Tabela 9.
- **Spring Boot:** o `pom` usa 3.3.0. A preferência do grupo é Spring Boot 4; decidir se se
  faz o upgrade.
- **Texto desatualizado a limpar:** `mockData.js`, alguns comentários que ainda dizem "Vue" ou
  "R1/R2/R4/R6", e TODOs antigos (ex.: "preencher 31 obras", "escolher SDK do LLM").

---

## TODO

### Base de conhecimento

- [ ] Carregar todas as obras na base de conhecimento
- [ ] Preencher links YouTube e pré-requisitos em falta

### Regras

- [ ] Ajustar valores finais de CF
- [ ] Ajustar thresholds e membership functions do fuzzy
- [ ] Remover valores numéricos de CF dos comentários (`.drl`, `SkillLevel`, `CONTEXT.md`,
  testes) — a fonte de verdade são os `@CF` nos `.drl`

### LLM

- [ ] Rever prompt do LLM

### UI

- [ ] Rever texto todo da UI
- [ ] Rever design da secção de justificação na UI

### Relatório

- [ ] Re-escrever relatório

### Código

- [ ] UI deve mostrar erro mais amigável se a API falhar (ex.: sem backend, ou sem chave do Groq)
- [x] Rever comentários e Javadoc no projeto todo (Java, JSX, .drl, .fcl, testes, Dockerfiles, configs)

---

## Referências (pasta `projetos_prof/`)

- `ficha_4/Haemorrhage-FC`: padrão dos fatores de confiança (`@CF`, `TrackingAgendaListener`,
  MYCIN, `update()`). É o molde do estilo das nossas regras.
- `ficha_3/JFuzzy`: lógica difusa com jFuzzyLogic (.fcl).
- `ficha_2/Haemorrhage-How`: AgendaEventListener e justificações.
- `ficha_0/Drools-FruitClassification`: estrutura geral Evidence/Hypothesis.
- Relatório prévio: tabelas de obras (Tabela 9) e competências (Tabela 7), e escala de níveis
  por competência (Tabela 6).
```