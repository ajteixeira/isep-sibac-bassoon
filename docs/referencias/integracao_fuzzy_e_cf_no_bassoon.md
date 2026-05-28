# Integrar Logica Difusa + Fatores de Confianca no bassoon

Documento de decisao e desenho. Responde a pergunta: como juntar as duas tecnicas (logica difusa
da ficha 3 e fatores de confianca da ficha 4) no projeto bassoon, da forma mais simples e natural.

Le primeiro `analise_ficha3_logica_difusa.md` e `analise_ficha4_fatores_confianca.md`.

## 1. A pergunta e a resposta curta

> Vou ter o `bassoon-engine` em Drools e outro `bassoon-fuzzy` em jFuzzy? Ou cabe tudo no mesmo
> `bassoon-engine`?

**Cabe tudo no mesmo `bassoon-engine`. Nao crias um segundo modulo.**

Razao simples: o jFuzzyLogic e apenas uma biblioteca Java (um `.jar`). Correr logica difusa e
chamar um metodo que recebe numeros e devolve um numero. Nao e um servico, nao tem servidor, nao
tem ciclo de vida proprio. Separar isto num modulo a parte so traria fronteiras de rede,
serializacao e deployment, sem nenhum ganho.

A separacao que queremos e **logica, nao fisica**: a logica difusa e o *front-end* (transforma
entradas graduais num grau), os fatores de confianca sao o *back-end* (combinam evidencias e
produzem o ranking). Os dois vivem no mesmo modulo, em pacotes diferentes.

```
bassoon-engine/
├── src/main/java/org/sibac/bassoon/
│   ├── model/            Evidence, Hypothesis, Obra, ...        (ja existe)
│   ├── cf/               TrackingAgendaListener, combinacao CF  (a portar da ficha 4)
│   └── fuzzy/            FuzzyAdequacaoService                  (NOVO, fino)
└── src/main/resources/
    ├── rules/rules.drl                                          (ja existe)
    └── fuzzy/adequacao.fcl                                      (NOVO)
```

## 2. Como as duas tecnicas se encaixam

A chave da integracao e uma unica ideia: **o resultado da logica difusa e um numero entre 0 e 1,
e um CF tambem e um numero entre 0 e 1.** Logo o output do fuzzy pode entrar diretamente no motor
de CF como o fator de confianca de um facto.

```
                                A PONTE
   logica difusa                  │                 fatores de confianca
   defuzzify() -> 0.72   ───────────────────►   facto com CF = 0.72
                            "grau de adequacao
                             vira confianca"
```

### 2.1 Onde a logica difusa e natural no bassoon

O melhor candidato e a **adequacao da obra ao nivel do aluno**. Hoje (ver decisoes do projeto)
isto e uma tabela rigida de CFs escrita a mao: para cada nivel, um valor por dificuldade
(INICIANTE: dif1=+0.9, dif2=+0.9, dif3=-0.3, ...). Essa tabela e exatamente o tipo de
conhecimento que a logica difusa modela melhor, porque a adequacao nao tem saltos: uma obra de
dificuldade 3 e quase tao adequada como uma de 3.5 para o mesmo aluno.

O principio pedagogico que o perito quer e suave: **a obra deve ter dificuldade igual ou um pouco
acima do nivel atual do aluno** (desafio atingivel). Demasiado facil e pouco util, demasiado
dificil e frustrante. Isto desenha-se com tres funcoes de pertenca e uma matriz de regras, tal
como o `prescricao.fcl` da ficha 3.

### 2.2 Arquitetura de fluxo

```
INPUT do professor
   nivel do aluno (+ direcao de duvida), competencias, motivacao,
   ultimo periodo, preferencia de acompanhamento
        │
        ▼
[1] FRONT-END DIFUSO  (org.sibac.bassoon.fuzzy, usa jFuzzyLogic)
    adequacao.fcl:  nivel x dificuldade_da_obra  ->  grau de adequacao [0,1]
    corre uma vez por obra do repertorio
        │  por obra: um numero de adequacao (ex. 0.72)
        ▼
[2] SEMEAR A MEMORIA DE TRABALHO
    para cada obra, insere Hypothesis("candidatura", nomeObra, cf = adequacao)
        │
        ▼
[3] BACK-END SIMBOLICO  (Drools + CF, regras @CF)
    regras combinam POR CIMA da adequacao, via propagacao + MYCIN:
      - competencias prioritarias x utilidade da obra
      - penalizacao por epoca repetida
      - preferencia de acompanhamento
        │
        ▼
[4] POS-PROCESSAMENTO Java
    recolhe candidaturas, calcula score, aplica pre-requisitos, ordena
        │
        ▼
RANKING final de obras (CF + justificacao)
```

A logica difusa faz o passo [1]. Os fatores de confianca fazem o passo [3]. A ponte e o passo
[2]: o grau difuso vira o CF inicial da candidatura.

## 3. Esbocos de codigo

### 3.1 Dependencia Maven (bassoon-engine/pom.xml)

```xml
<dependency>
    <groupId>net.sourceforge.jFuzzyLogic</groupId>
    <artifactId>jFuzzyLogic</artifactId>
    <version>3.3</version>
</dependency>
```

Se o artefacto nao estiver no Maven Central acessivel, alternativa: instalar o `.jar` da ficha 3
no repositorio local com `mvn install:install-file`. Mas tenta primeiro a dependencia normal.

### 3.2 O ficheiro difuso (src/main/resources/fuzzy/adequacao.fcl)

Duas entradas (nivel do aluno numa escala 1..10 e dificuldade da obra 1..6) e uma saida (grau de
adequacao 0..1). A matriz de 9 regras codifica o principio "dificuldade deve acompanhar o nivel".

```
FUNCTION_BLOCK adequacao

VAR_INPUT
    nivel       : REAL;   // INICIANTE~2, INTERMEDIO~5, AVANCADO~8 (ajustavel pela direcao de duvida)
    dificuldade : REAL;   // 1..6 (vem da Obra)
END_VAR

VAR_OUTPUT
    grau : REAL;          // 0..1 -> vai virar o CF de adequacao da obra
END_VAR

FUZZIFY nivel
    TERM baixo := (1, 1) (4, 0) ;
    TERM medio := (2, 0) (5, 1) (8, 0) ;
    TERM alto  := (6, 0) (9, 1) (10, 1) ;
END_FUZZIFY

FUZZIFY dificuldade
    TERM facil   := (1, 1) (3, 0) ;
    TERM media   := (2, 0) (3.5, 1) (5, 0) ;
    TERM dificil := (4, 0) (6, 1) ;
END_FUZZIFY

DEFUZZIFY grau
    TERM baixa := (0, 1) (0.4, 0) ;
    TERM media := (0.2, 0) (0.5, 1) (0.8, 0) ;
    TERM alta  := (0.6, 0) (1, 1) ;
    METHOD : COG;
    DEFAULT := 0;
END_DEFUZZIFY

RULEBLOCK No1
    AND : MIN;
    ACT : MIN;
    ACCU : MAX;

    // aluno baixo: facil e ideal, dificil e mau
    RULE 1 : IF nivel IS baixo AND dificuldade IS facil   THEN grau IS alta;
    RULE 2 : IF nivel IS baixo AND dificuldade IS media   THEN grau IS media;
    RULE 3 : IF nivel IS baixo AND dificuldade IS dificil THEN grau IS baixa;

    // aluno medio: media e ideal, facil aborrece, dificil ainda serve
    RULE 4 : IF nivel IS medio AND dificuldade IS facil   THEN grau IS media;
    RULE 5 : IF nivel IS medio AND dificuldade IS media   THEN grau IS alta;
    RULE 6 : IF nivel IS medio AND dificuldade IS dificil THEN grau IS media;

    // aluno alto: dificil e ideal, facil e desperdicio
    RULE 7 : IF nivel IS alto AND dificuldade IS facil   THEN grau IS baixa;
    RULE 8 : IF nivel IS alto AND dificuldade IS media   THEN grau IS media;
    RULE 9 : IF nivel IS alto AND dificuldade IS dificil THEN grau IS alta;
END_RULEBLOCK

END_FUNCTION_BLOCK
```

Vantagem sobre a tabela rigida: para mudar o comportamento o perito ajusta funcoes de pertenca ou
regras no `.fcl`, sem tocar em Java nem recompilar a tabela de CFs.

### 3.3 O servico difuso (org.sibac.bassoon.fuzzy.FuzzyAdequacaoService)

Classe fina: carrega o FCL uma vez e expoe um metodo que recebe dois numeros e devolve o grau.

```java
package org.sibac.bassoon.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import java.io.InputStream;

public class FuzzyAdequacaoService {

    private final FIS fis;

    public FuzzyAdequacaoService() {
        // carregar do classpath (o .fcl esta em resources/fuzzy/)
        InputStream in = getClass().getResourceAsStream("/fuzzy/adequacao.fcl");
        this.fis = FIS.load(in, true);
        if (this.fis == null) {
            throw new IllegalStateException("Nao foi possivel carregar adequacao.fcl");
        }
    }

    /**
     * Devolve o grau de adequacao [0..1] de uma obra a um aluno.
     * synchronized porque o objeto FIS guarda estado entre setVariable e evaluate
     * e nao e seguro para varias threads ao mesmo tempo.
     */
    public synchronized double adequacao(double nivel, double dificuldade) {
        fis.setVariable("nivel", nivel);
        fis.setVariable("dificuldade", dificuldade);
        fis.evaluate();
        return fis.getVariable("grau").defuzzify();   // numero em [0..1]
    }
}
```

Mapear o nivel categorico para a escala numerica (com a "direcao de duvida" a empurrar o valor):

```java
// INTERMEDIO sozinho -> 5.0 ; INTERMEDIO a tender para AVANCADO -> ~6.0
double nivel = switch (nivelAluno) {
    case INICIANTE  -> 2.0;
    case INTERMEDIO -> 5.0;
    case AVANCADO   -> 8.0;
};
if (direcaoDuvida == AVANCADO)  nivel += 1.0;   // duvida para cima
if (direcaoDuvida == INICIANTE) nivel -= 1.0;   // duvida para baixo
```

### 3.4 A ponte: semear as candidaturas (pre-processamento, antes de fireAllRules)

Este e o ponto de integracao recomendado. Corre o fuzzy em Java, insere o resultado como CF da
candidatura, e so depois dispara as regras de CF. Simples de ler, simples de testar.

```java
FuzzyAdequacaoService fuzzy = new FuzzyAdequacaoService();
double nivel = nivelParaEscala(input);   // ver 3.3

for (Obra obra : repertorio) {
    double adeq = fuzzy.adequacao(nivel, obra.getDificuldade());   // [0..1]

    // a adequacao difusa vira o CF inicial da candidatura desta obra
    kSession.insert(new Hypothesis("candidatura", obra.getNome(), adeq));
}

// agora as regras @CF (competencias, epoca, acompanhamento) combinam por cima
kSession.fireAllRules();
```

Isto substitui as regras R1 (adequacao por nivel) e R2 (ajuste por motivacao) atuais: em vez de
uma tabela de CFs disparada por regras, a adequacao passa a sair de um calculo difuso. As
restantes regras de CF nao mudam, continuam a refinar a candidatura por cima.

Nota sobre os negativos: o grau difuso esta em [0..1], so positivo. As penalizacoes (epoca
repetida, obra inadequada) continuam a vir de regras com `@CF` negativo, que a combinacao MYCIN
trata. Se quiseres que a propria adequacao possa ser negativa (obra claramente desadequada puxa
o CF para baixo), mapeia o grau para [-1..1] com `cf = 2*grau - 1`. Recomendacao: comeca com
[0..1] (mais simples) e so passa a [-1..1] se o perito sentir falta.

### 3.5 Alternativa: chamar o fuzzy de dentro de uma regra

E possivel chamar o `FuzzyAdequacaoService` no RHS de uma regra DRL (a regra le a Obra, calcula o
grau, atualiza a candidatura). Funciona, mas mistura duas tecnologias dentro do `.drl`, e mais
dificil de seguir e de testar. **Nao recomendado para comecar.** O pre-processamento (3.4)
mantem o fuzzy e o CF separados e claros.

## 4. O ponto sensivel: estado estatico e a API web

O `TrackingAgendaListener` da ficha 4 guarda estado em variaveis estaticas (sessao, CF da regra,
factos do LHS). E o `FIS` do jFuzzyLogic tambem guarda estado entre `setVariable` e `evaluate`.
Em consola, com um pedido de cada vez, nao ha problema. Numa API Spring Boot com pedidos em
paralelo, dois pedidos podem pisar o estado um do outro.

Recomendacoes minimas para o `bassoon-api`:

Criar uma `KieSession` nova por pedido e fazer `dispose()` no fim (nao partilhar sessao). Tornar
o `adequacao(...)` do servico difuso `synchronized` (ja feito no esboco) ou usar uma instancia de
`FIS` por pedido. Idealmente, refatorar o `TrackingAgendaListener` para guardar o estado por
instancia (uma instancia de listener por sessao) em vez de em campos estaticos. Para a entrega
academica, sessao por pedido + `synchronized` resolve.

## 5. Resumo da decisao

Um unico modulo `bassoon-engine`. A logica difusa entra como dependencia Maven e vive no pacote
`fuzzy`, com o conhecimento no ficheiro `adequacao.fcl`. O motor de fatores de confianca e o
Drools, ja existente. A ponte entre os dois e um numero: o grau de adequacao difuso (0..1) vira o
CF inicial da candidatura de cada obra. O fuzzy corre primeiro (front-end), o CF combina por cima
(back-end), tudo no mesmo processo, sem segundo modulo nem servico extra.

A divisao natural de papeis:

A logica difusa responde a "quao bem esta obra encaixa no nivel deste aluno?", onde a resposta e
gradual e sem saltos. Os fatores de confianca respondem a "juntando todas as evidencias (nivel,
competencias, epoca, acompanhamento), com que confianca recomendo esta obra?", combinando
contributos incertos que vem de varias regras.

## 6. Primeiro passo minimo (para arrancar simples)

1. Garantir que o motor de CF funciona sozinho: portar o `TrackingAgendaListener` da ficha 4 e
   registar na `KieSession`; inserir uma `Hypothesis` de teste e ver o CF a combinar.
2. So depois, acrescentar o fuzzy: dependencia no `pom.xml`, criar `adequacao.fcl`, criar
   `FuzzyAdequacaoService`, e testar isolado (dado nivel=5, dificuldade=3, ver o grau).
3. Ligar os dois com o ciclo de pre-processamento da seccao 3.4 e comparar o ranking com a tabela
   de CFs antiga, para validar que a adequacao difusa faz sentido para o perito.

Fazer por esta ordem mantem cada peca testavel sozinha antes de as juntar.
