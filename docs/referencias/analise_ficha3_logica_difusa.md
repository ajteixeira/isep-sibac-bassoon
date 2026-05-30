# Ficha 3 - Logica Difusa com jFuzzyLogic (projeto JFuzzy)

Documento de referencia para reimplementar logica difusa noutro contexto (projeto bassoon).
Analisa o projeto do professor `projetos_prof/ficha_3/JFuzzy`.

## 1. O que este projeto demonstra

A ficha 3 mostra como construir um **sistema de inferencia difusa (FIS)** com a biblioteca
jFuzzyLogic. A ideia central da logica difusa: em vez de uma variavel pertencer a uma so
categoria (verdadeiro/falso), pertence a varias categorias ao mesmo tempo com graus diferentes.

Exemplo do projeto: um valor `psa = 3` nao e simplesmente "baixo". E "baixo" com grau 0.25 e
"medio" com grau 0.75 ao mesmo tempo. A logica difusa trabalha com estes graus.

O fluxo de um FIS tem sempre tres passos:

```
   valores nitidos          graus de pertenca         valor nitido final
   (numeros do mundo)                                  (numero de saida)
        psa=3                low=0.25                       sl=720
        sd=50      ──►       medium=0.75      ──►            (ex.)
                   FUZZIFY   slow=0.0         INFERENCIA     DEFUZZIFY
                            ...               + regras
        [1] entrada        [2] fuzzificacao   [3] regras    [4] defuzzificacao
```

1. **Fuzzificacao**: transforma cada numero de entrada nos seus graus de pertenca.
2. **Inferencia**: aplica as regras IF/THEN difusas, combinando os graus.
3. **Defuzzificacao**: junta os resultados difusos das regras num unico numero de saida.

## 2. Organizacao do projeto

```
ficha_3/JFuzzy/JFuzzy/
├── lib/
│   └── jFuzzyLogic.jar          biblioteca (motor difuso + parser de FCL)
├── src/
│   ├── fcl/
│   │   ├── prescricao.fcl        sistema difuso principal (2 entradas, 1 saida, 9 regras)
│   │   └── tipper.fcl            exemplo classico da gorjeta (referencia)
│   └── org/engcia/
│       └── Main.java             carrega o .fcl, define entradas, avalia, mostra resultado
└── JFuzzy.iml                    projeto IntelliJ (sem Maven, jar adicionado a mao)
```

Pontos a reter sobre a organizacao:

A logica difusa **nao esta em codigo Java**. Esta declarada num ficheiro de texto `.fcl`
(Fuzzy Control Language, um standard IEC 61131-7). O Java so carrega esse ficheiro, mete os
valores de entrada e le o resultado. Isto e importante: o conhecimento do perito (as funcoes de
pertenca e as regras) fica separado do codigo, num formato legivel e editavel sem recompilar.

O projeto e simples de proposito: nao usa Maven, o `.jar` esta numa pasta `lib`. Para o bassoon
usaremos Maven (ver o documento de integracao).

## 3. O ficheiro FCL em detalhe (prescricao.fcl)

Um ficheiro FCL define um ou mais `FUNCTION_BLOCK`. Cada bloco tem quatro zonas.

### 3.1 Declaracao de variaveis

```
FUNCTION_BLOCK prescricao

VAR_INPUT
    psa : REAL;      // variavel de entrada
    sd  : REAL;      // variavel de entrada
END_VAR

VAR_OUTPUT
    sl : REAL;       // variavel de saida
END_VAR
```

### 3.2 Fuzzificacao das entradas (FUZZIFY)

Para cada variavel de entrada definem-se os **termos linguisticos** (low, medium, high) e a
**funcao de pertenca** de cada termo. A funcao e desenhada por pontos `(x, grau)` ligados por
retas, o que da triangulos e trapezios.

```
FUZZIFY psa
    TERM low    := (0, 1) (4, 0) ;            // reta que desce de grau 1 (em x=0) a grau 0 (em x=4)
    TERM medium := (0, 0) (4, 1) (8, 0) ;     // triangulo com pico em x=4
    TERM high   := (4, 0) (8, 1) (60, 1) ;    // sobe e fica em 1 ate x=60
END_FUZZIFY

FUZZIFY sd
    TERM slow   := (0, 1) (44, 0) ;
    TERM medium := (0, 0) (44, 1) (90, 0) ;
    TERM fast   := (44, 0) (90, 1) ;
END_FUZZIFY
```

Como ler `TERM medium := (0, 0) (4, 1) (8, 0)`: o grau de pertenca ao termo "medium" e 0 quando
psa=0, sobe ate 1 quando psa=4, e desce de volta a 0 quando psa=8. Para psa=2 o grau seria 0.5.

Diagrama da variavel psa (triangulos sobrepostos, eixo x = valor de psa):

```
grau
 1 |low\      /medium\          high _____________
   |    \    /        \        /
0.5|     \  /          \      /
   |      \/            \    /
 0 |______/\____________\__/________________________ psa
   0      4              8        ...              60
```

A sobreposicao e o ponto-chave: em psa=3 o ponto cai na zona onde "low" ainda nao chegou a 0 e
"medium" ja subiu. Por isso psa=3 ativa low **e** medium em simultaneo, com graus diferentes.

### 3.3 Defuzzificacao da saida (DEFUZZIFY)

A variavel de saida tambem tem termos linguisticos. Alem disso define o **metodo** de
defuzzificacao e um valor por omissao.

```
DEFUZZIFY sl
    TERM little     := (0, 1) (200, 1) (600, 0) ;
    TERM medium     := (200, 0) (600, 1) (1000, 0) ;
    TERM much       := (600, 0) (1000, 1) (1400, 0) ;
    TERM much_more  := (1000, 0) (1400, 1) (1600, 1) ;
    METHOD : COG;     // Center Of Gravity (centro de gravidade)
    DEFAULT := 0;     // valor se nenhuma regra disparar
END_DEFUZZIFY
```

`METHOD : COG` significa que a saida final e o centro de gravidade da area resultante das regras.
Na pratica e uma media ponderada que produz um numero suave, sem saltos. E o metodo mais comum.

#### Metodos de defuzzificacao: COG vs MOM vs BOA

Apos as regras dispararem, tens uma **area fuzzy acumulada** — figuras clipadas dos termos de
saida (little, medium, much, etc.) sobrepostas. E preciso extrair um unico numero crisp. Ha
3 metodos principais:

**COG — Centre of Gravity (o que o professor usou)**

\[ COG = \frac{\int x \cdot \mu(x)\,dx}{\int \mu(x)\,dx} \]

E o **centro de massa** da figura acumulada. Pondera cada contribuicao pela sua area: uma regra
que disparou com grau 0.8 pesa mais no resultado do que uma que disparou com 0.3.

- ✅ Suave e continuo — pequenas mudancas nas entradas dao pequenas mudancas na saida
- ✅ Sensivel a **todas** as regras que dispararam e com que intensidade
- ✅ Interpretacao fisica direta (centro de massa)

**MOM — Mean of Maxima**

Pega apenas nos pontos onde µ(x) atinge o **maximo global** da figura acumulada e faz a media.

- ❌ Ignora completamente a forma da area — so olha para o topo
- ❌ Pode dar saltos bruscos (ex.: duas regras com picos iguais em zonas opostas dao uma media
  que nao corresponde a nenhuma delas)
- ❌ Se low dispara a 0.5 e high dispara a 0.5, o MOM da ~0.6 (medium) — o que nao faz sentido

**BOA — Bisector of Area**

Encontra o ponto x onde a area a esquerda = area a direita.

\[ \int_{0}^{x} \mu(z)\,dz = \int_{x}^{1} \mu(z)\,dz \]

- ⚠️ Para figuras simetricas, BOA = COG
- ⚠️ Para figuras assimetricas, o BOA tende a puxar para o lado menos disperso, o COG para o
  lado com mais massa — o COG e geralmente mais intuitivo
- ⚠️ Menos comum na literatura e nas bibliotecas

**Porque o COG foi a escolha certa:** num sistema de adequacao pedagogica, a transicao entre
"obra adequada" e "obra inadequada" deve ser **suave e continua**. O MOM criaria saltos
abruptos que penalizariam ou beneficiariam o aluno de forma artificial. O BOA e raramente usado.
O COG e o padrao em sistemas Mamdani e o que o professor usou no `prescricao.fcl`.

### 3.4 Bloco de regras (RULEBLOCK)

```
RULEBLOCK No1
    AND  : MIN;       // "A AND B" -> usa o MINIMO dos dois graus
    ACT  : MIN;       // ativacao: corta a saida pelo grau da regra (metodo MIN)
    ACCU : MAX;       // acumulacao: junta varias regras usando o MAXIMO

    RULE 1 : IF psa IS low    AND sd IS slow   THEN sl IS little;
    RULE 2 : IF psa IS low    AND sd IS medium THEN sl IS medium;
    RULE 3 : IF psa IS low    AND sd IS fast   THEN sl IS medium;
    RULE 4 : IF psa IS medium AND sd IS slow   THEN sl IS little;
    RULE 5 : IF psa IS medium AND sd IS medium THEN sl IS medium;
    RULE 6 : IF psa IS medium AND sd IS fast   THEN sl IS much;
    RULE 7 : IF psa IS high   AND sd IS slow   THEN sl IS much;
    RULE 8 : IF psa IS high   AND sd IS medium THEN sl IS much;
    RULE 9 : IF psa IS high   AND sd IS fast   THEN sl IS much_more;
END_RULEBLOCK

END_FUNCTION_BLOCK
```

Tres operadores definem a matematica da inferencia:

`AND : MIN` define como combinar condicoes. Se psa e "low" com grau 0.25 e sd e "medium" com
grau 0.6, entao "psa IS low AND sd IS medium" vale `min(0.25, 0.6) = 0.25`. O OR usaria MAX
(lei de DeMorgan).

`ACT : MIN` define como o grau da regra molda o termo de saida. Com MIN, o termo de saida fica
"cortado" na altura do grau da regra.

`ACCU : MAX` define como juntar varias regras que apontam para a mesma saida. Sobrepoe as areas
e fica com o maximo.

As 9 regras cobrem todas as combinacoes possiveis (3 termos de psa x 3 termos de sd). Note que
varias regras disparam ao mesmo tempo, cada uma com o seu grau, e a defuzzificacao junta tudo.

## 4. O codigo Java (Main.java)

O Java e muito fino. So orquestra. As partes essenciais:

```java
// 1. carregar o sistema difuso a partir do ficheiro FCL
FIS fis = FIS.load("src/fcl/prescricao.fcl", true);

// 2. definir os valores de entrada (numeros nitidos do mundo real)
fis.setVariable("psa", 3);
fis.setVariable("sd", 50);

// 3. avaliar (corre fuzzificacao + regras + defuzzificacao)
fis.evaluate();

// 4. ler o resultado defuzzificado (numero nitido de saida)
Variable sl = fis.getVariable("sl");
System.out.println("Output value: " + sl.defuzzify());
```

O resto do `Main.java` e diagnostico util para perceber e justificar resultados:

`fis.getVariable("psa").getMembership("low")` devolve o grau de pertenca de psa ao termo "low".
Serve para mostrar a fuzzificacao ("psa=3 deu low=0.25, medium=0.75").

Iterar sobre `fis.getFunctionBlock("prescricao").getFuzzyRuleBlock("No1").getRules()` mostra cada
regra e o seu grau de ativacao. Serve para explicar quais regras contribuiram e quanto.

`JFuzzyChart.get().chart(fis)` desenha os graficos das funcoes de pertenca. So util em ambiente
grafico, nao serve numa API web (ver nota abaixo).

## 5. Padrao a reutilizar e armadilhas

O padrao essencial para o bassoon e este, e e curto:

```
[FCL com perito]  ->  FIS.load()  ->  setVariable(entradas)  ->  evaluate()  ->  defuzzify()
```

Quatro chamadas. Toda a complexidade (perito) vive no `.fcl`, que se edita sem recompilar.

Coisas a ter em atencao quando levarmos isto para o bassoon:

A API `JFuzzyChart` abre janelas Swing. Numa API REST (Spring Boot) **nao se chama** o chart;
so se usa `load`, `setVariable`, `evaluate` e `defuzzify`/`getMembership`.

O caminho do FCL no exemplo e relativo (`src/fcl/...`). No bassoon o FCL deve estar em
`src/main/resources` e ser carregado do classpath, nao por caminho de ficheiro.

A biblioteca jFuzzyLogic existe no Maven Central, por isso no bassoon entra como dependencia
normal no `pom.xml`, sem o `.jar` na pasta lib. O `groupId:artifactId` e
`net.sourceforge.jFuzzyLogic:jFuzzyLogic`.

Carregar o FIS tem custo. Faz-se uma vez (no arranque) e reutiliza-se o objeto `FIS` em cada
pedido, mudando so as variaveis de entrada com `setVariable` antes de `evaluate`.

## 6. Ligacao ao bassoon (resumo)

No bassoon a logica difusa serve para transformar entradas graduais (por exemplo nivel do aluno e
dificuldade da obra) num grau de adequacao suave, em vez de uma tabela rigida de valores. Esse
grau de saida vai depois alimentar o motor de fatores de confianca. O detalhe completo dessa
ponte esta em `integracao_fuzzy_e_cf_no_bassoon.md`.
