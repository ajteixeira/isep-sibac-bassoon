# Ficha 4 - Fatores de Confianca (Certeza) sobre Drools (projeto Haemorrhage-FC)

Documento de referencia para reimplementar fatores de confianca (CF) noutro contexto
(projeto bassoon). Analisa o projeto do professor `projetos_prof/ficha_4/Haemorrhage-FC`.

## 1. O problema e a teoria

As regras classicas do Drools sao deterministas: ou disparam ou nao. Mas o conhecimento de um
perito raramente e absoluto. "Se ha impressoes digitais na arma, entao o suspeito e culpado"
nao e uma certeza, e uma forte indicacao. A **Teoria dos Fatores de Certeza** (modelo MYCIN)
serve exatamente para isto: associa a cada regra e a cada facto um numero entre -1 e +1.

```
  -1 .................. 0 .................. +1
  certeza que           desconhecido         certeza que
  e falso / contra      (sem evidencia)      e verdadeiro / a favor
```

Dois conceitos centrais:

Cada **evidencia** chega com um CF (quao certo o perito esta de a ter observado). Cada **regra**
tem um CF proprio (quao forte e a ligacao entre a evidencia e a conclusao). Quando varias regras
apontam para a mesma **hipotese**, os seus contributos **combinam-se** numa formula que faz o CF
da hipotese crescer com evidencias a favor e descer com evidencias contra.

O projeto Haemorrhage-FC implementa este mecanismo por cima do Drools, sem alterar o motor. E
esse o valor: o Drools nao conhece CFs, mas o projeto usa metadados de regra e listeners para
acrescentar essa camada.

## 2. Organizacao do projeto

```
ficha_4/Haemorrhage-FC/
├── pom.xml                              projeto Maven, packaging "kjar", drools 7.44
└── src/main/
    ├── java/org/engcia/
    │   ├── cf/model/
    │   │   ├── Uncertainty.java         interface: so tem update()
    │   │   ├── FactCF.java              classe base: cf, description, value + logica de combinacao
    │   │   ├── Evidence.java            extends FactCF (facto de entrada)
    │   │   └── Hypothesis.java          extends FactCF (conclusao com CF)
    │   ├── cf/listeners/
    │   │   ├── TrackingAgendaListener.java   le o @CF da regra e propaga o CF (peca central)
    │   │   └── FactListener.java             so faz log de insert/update/delete
    │   └── cfsample/
    │       └── DroolsTest.java          arranque: cria sessao, listeners, factos, fireAllRules
    └── resources/
        ├── META-INF/kmodule.xml         define a kbase e a ksession
        └── org/engcia/Rules.drl         as regras com @CF
```

A separacao importante: o **modelo** (FactCF e filhos) guarda os dados e a matematica da
combinacao; os **listeners** ligam-se ao ciclo do Drools para capturar o CF da regra e disparar a
propagacao; as **regras** (`.drl`) declaram so o @CF e mandam atualizar a hipotese.

## 3. O modelo de dados

### 3.1 Interface Uncertainty

```java
public interface Uncertainty {
    public void update();   // recalcula o CF deste facto a partir da regra que disparou
}
```

### 3.2 FactCF (classe base)

Guarda tres campos: `cf` (o fator de confianca atual), `description` (que facto e, ex.
"guilty") e `value` (o valor, ex. "true"). O par description+value identifica o facto.

```java
public class FactCF implements Comparable<FactCF>, Uncertainty {
    private double cf;
    private String description;
    private String value;
    // construtores, getters, setters ...

    // ordena por CF crescente -> usado para encontrar o MINIMO do LHS
    public int compareTo(FactCF f) {
        return Double.compare(this.cf, f.cf);
    }
}
```

`Evidence` e `Hypothesis` sao apenas subclasses de `FactCF` sem logica propria. A distincao
serve para o Drools poder filtrar por tipo (`Evidence(...)` no LHS, `Hypothesis` na conclusao) e
para o codigo ser legivel.

## 4. A peca central: @CF + TrackingAgendaListener

Aqui esta o truque que faz tudo funcionar. O Drools nao sabe o que e um CF, mas permite anotar
regras com **metadados** (`@CF(0.75)`) e permite registar um **listener da agenda** que e
chamado antes e depois de cada regra disparar.

### 4.1 As regras (Rules.drl)

```java
rule "r1:Fingerprints on the weapon"
@CF(0.75)                                  // metadado: forca desta regra
lock-on-active true                        // evita que a regra volte a disparar em loop
when
    Evidence( description == "fingerprints", value == "true" )
then
    // ir buscar a hipotese "guilty=true" que esta na memoria de trabalho
    Hypothesis $h = (Hypothesis) TrackingAgendaListener.getFactRef(
                        Hypothesis.class, "guilty", "true");
    $h.update();                           // dispara a propagacao do CF
end
```

A regra nao calcula nada de CF diretamente. So vai buscar a hipotese e chama `update()`. Toda a
matematica acontece dentro de `update()`, com a ajuda do listener.

### 4.2 O listener captura o contexto da regra

`TrackingAgendaListener` implementa `AgendaEventListener`. Os dois metodos que importam:

```java
public void beforeMatchFired(BeforeMatchFiredEvent event) {
    // 1. guarda a sessao
    kieSession = ...;
    // 2. guarda os factos que dispararam a regra (o LHS)
    activations.addAll(event.getMatch().getObjects());
    // 3. le o metadado @CF da regra (se nao existir, assume 1.0 -> regra determinista)
    Map<String,Object> meta = event.getMatch().getRule().getMetaData();
    ruleCF = meta.containsKey("CF") ? (Double) meta.get("CF") : 1.0;
    ruleName = event.getMatch().getRule().getName();
}

public void afterMatchFired(AfterMatchFiredEvent event) {
    activations.clear();   // limpa para a proxima regra
}
```

Antes de a regra correr o RHS, o listener ja sabe: que factos a ativaram, qual o CF da regra e o
nome da regra. Guarda tudo em variaveis estaticas, acessiveis de dentro do `update()`.

Dois metodos auxiliares completam o quadro:

```java
// encontra na memoria de trabalho o facto com aquele tipo+description+value
static FactCF getFactRef(Class<?> c, String description, String value) { ... }

// devolve o MENOR CF entre os factos do LHS (semantica do AND difuso/incerto)
static double getLHSminimumCF(Object conclusao) {
    activations.remove(conclusao);   // tira a propria hipotese da lista
    activations.sort(null);          // ordena por CF (compareTo)
    return ((FactCF) activations.get(0)).getCf();   // o primeiro e o minimo
}
```

### 4.3 A propagacao do CF (FactCF.update)

```java
public void update() {
    FactHandle fHandle = TrackingAgendaListener.getKieSession().getFactHandle(this);

    // 1. forca do antecedente = menor CF entre os factos do LHS
    double lhsCF = TrackingAgendaListener.getLHSminimumCF(this);

    // 2. contributo desta regra = forca do antecedente x forca da regra
    double newCF = lhsCF * TrackingAgendaListener.getRuleCF();

    // 3. combinar com o CF que a hipotese ja tinha (formula MYCIN)
    this.updateCF(newCF);

    // 4. avisar o Drools que o facto mudou
    TrackingAgendaListener.getKieSession().update(fHandle, this);
}
```

A formula de combinacao (`combineCF`) e o coracao do modelo MYCIN:

```java
private double combineCF(double oldCF, double newCF) {
    if (oldCF >= 0 && newCF >= 0)
        return oldCF + newCF * (1 - oldCF);          // dois a favor: reforcam-se
    else if (oldCF <= 0 && newCF <= 0)
        return oldCF + newCF * (1 + oldCF);          // dois contra: reforcam-se (negativo)
    else
        return (oldCF + newCF) / (1 - Math.min(Math.abs(oldCF), Math.abs(newCF)));  // sinais opostos: atenuam-se
}
```

Propriedades importantes desta formula: o resultado fica sempre dentro de [-1, +1]; duas
evidencias a favor dao sempre mais do que cada uma sozinha, mas nunca passam de 1; uma evidencia
contra forte (alibi) puxa o CF para baixo; e a ordem pela qual as regras disparam **nao altera**
o resultado final (a combinacao e comutativa).

### 4.4 Diagrama do ciclo completo

```
  regra dispara
        │
        ▼
  beforeMatchFired (listener)
   • guarda factos do LHS (activations)
   • le @CF da regra  -> ruleCF
        │
        ▼
  RHS da regra:  $h = getFactRef(...);  $h.update();
        │
        ▼
  FactCF.update()
   • lhsCF  = min(CF dos factos do LHS)      <- getLHSminimumCF
   • newCF  = lhsCF * ruleCF                 <- propagacao em serie
   • cf     = combineCF(cf_antigo, newCF)    <- combinacao MYCIN (paralelo)
   • kieSession.update(handle, this)         <- Drools reavalia
        │
        ▼
  afterMatchFired (listener): activations.clear()
```

Em resumo, ha duas operacoes distintas sobre os CFs:

**Propagacao em serie** (`lhsCF * ruleCF`): de uma evidencia, atraves de uma regra, ate a
hipotese. Multiplica-se a confianca da evidencia pela forca da regra.

**Combinacao em paralelo** (`combineCF`): junta o que varias regras dizem sobre a mesma hipotese.

## 5. Exemplo numerico passo a passo (o caso "guilty")

O `DroolsTest` insere uma hipotese a zero e tres evidencias:

```java
kSession.insert(new Hypothesis(0.0,  "guilty",       "true"));
kSession.insert(new Evidence(0.90,  "fingerprints", "true"));   // perito 90% certo
kSession.insert(new Evidence(0.50,  "motive",       "true"));   // perito 50% certo
kSession.insert(new Evidence(0.95,  "alibi",        "true"));   // perito 95% certo
kSession.fireAllRules();
```

Regras: r1 fingerprints @CF(0.75), r2 motive @CF(0.60), r3 alibi @CF(-0.80).
A hipotese "guilty" comeca em CF = 0.0. Cada regra que dispara atualiza-a:

```
r1 (fingerprints):
   lhsCF = 0.90          newCF = 0.90 * 0.75  = 0.675
   combina(0.0 , 0.675)  -> ambos >=0 -> 0.0 + 0.675*(1-0.0) = 0.675
   guilty CF = 0.675

r2 (motive):
   lhsCF = 0.50          newCF = 0.50 * 0.60  = 0.300
   combina(0.675, 0.300) -> ambos >=0 -> 0.675 + 0.300*(1-0.675) = 0.7725
   guilty CF = 0.7725

r3 (alibi):
   lhsCF = 0.95          newCF = 0.95 * (-0.80) = -0.760
   combina(0.7725, -0.760) -> sinais opostos
                         -> (0.7725 - 0.760) / (1 - min(0.7725, 0.760))
                         -> 0.0125 / (1 - 0.760) = 0.0125 / 0.24 = 0.052
   guilty CF = 0.052
```

Leitura: as impressoes digitais e o motivo empurraram a culpa para ~0.77 (forte suspeita), mas o
alibi quase certo anulou tudo, deixando o CF em ~0.05 (praticamente neutro). E exatamente o
raciocinio que um perito faria. E, como dito, se as regras tivessem disparado por outra ordem o
resultado final seria o mesmo ~0.05.

## 6. Arranque e configuracao

`kmodule.xml` define a base de conhecimento e a sessao:

```xml
<kmodule xmlns="http://www.drools.org/xsd/kmodule">
  <kbase name="rules" packages="org.engcia">
    <ksession name="ksession-rules"/>
  </kbase>
</kmodule>
```

`DroolsTest.main` faz o arranque tipico, com um detalhe critico: **registar os listeners antes
de inserir factos e disparar**.

```java
KieServices ks = KieServices.Factory.get();
KieContainer kc = ks.getKieClasspathContainer();
KieSession kSession = kc.newKieSession("ksession-rules");

kSession.addEventListener(new TrackingAgendaListener());   // CRITICO: sem isto nao ha CF
kSession.addEventListener(new FactListener());             // opcional: so logging

kSession.insert(new Hypothesis(0.0, "guilty", "true"));    // hipotese arranca a 0
kSession.insert(new Evidence(0.90, "fingerprints", "true"));
// ... mais evidencias
kSession.fireAllRules();
```

O `pom.xml` usa `packaging` **kjar** e o `kie-maven-plugin`, que compila as regras em tempo de
build. Drools 7.44, Java 8.

## 7. Pontos a reter e armadilhas (para o bassoon)

A hipotese tem de ser **inserida primeiro com CF = 0.0**. As regras nao a criam, so a atualizam
via `getFactRef`. Se a hipotese nao existir na memoria, `getFactRef` devolve null e rebenta.

`lock-on-active true` em cada regra evita reentrancia: como o `update()` chama
`kieSession.update()`, o facto muda e o Drools tende a reavaliar; sem o lock a mesma regra
voltaria a disparar para a mesma evidencia, em loop.

O `getLHSminimumCF` implementa o **AND** como minimo dos CFs. Se uma regra tiver varias
evidencias no LHS, a confianca da conjuncao e a da evidencia mais fraca. E uma escolha de
design coerente com a logica difusa (AND = MIN).

O listener guarda estado em **variaveis estaticas**. Funciona para uma sessao de cada vez. Numa
API web com pedidos concorrentes isto e um problema de seguranca de threads. No bassoon
convem isolar (uma sessao por pedido e cuidado com o estado estatico, ou refatorar o listener
para nao usar estatico). Ver o documento de integracao.

A combinacao MYCIN nao pondera ordem nem numero de evidencias de forma linear: muitas evidencias
fracas a favor saturam perto de 1 mas nunca chegam la. E o comportamento desejado.

## 8. Como o bassoon ja segue este padrao

O `bassoon-engine` ja esta desenhado sobre exatamente este modelo:

`model/Evidence.java` e `model/Hypothesis.java` correspondem aos da ficha 4 (com a diferenca de
o bassoon usar `EvidenceType` e `Object value` tipado em vez de strings, o que e uma melhoria de
legibilidade). As regras em `rules/rules.drl` ja usam `@CF(...)` e ja referem
`TrackingAgendaListener.getFactRef(...)`.

O que falta garantir no bassoon, por analogia direta com a ficha 4:

1. Portar a classe `TrackingAgendaListener` (e a logica de combinacao MYCIN, hoje em `FactCF`)
   para o pacote do bassoon. O `Evidence`/`Hypothesis` do bassoon precisam de uma forma de
   `update()` e de combinar CF, tal como o `FactCF` faz.
2. Registar o listener na criacao da `KieSession` (no `Main` do engine e, sobretudo, no
   `DroolsService` da API).
3. Inserir as `Hypothesis` (candidaturas de obra) com CF inicial antes de `fireAllRules`.
4. Resolver o estado estatico do listener para o contexto web (ver documento de integracao).

A formula MYCIN do bassoon e a mesma da ficha 4 (ja registada nas decisoes de design do
projeto):

```
ambos >= 0:        cf = old + new * (1 - old)
ambos <= 0:        cf = old + new * (1 + old)
sinais opostos:    cf = (old + new) / (1 - min(|old|, |new|))
```
