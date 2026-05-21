# Bassoon - Sistema Pericial de Recomendação de Repertório de Fagote

Projeto desenvolvido no âmbito da UC Sistemas Baseados em Conhecimento (SIBAC),
mestrado em Engenharia Informática.
Grupo: Inês Lemos (1231832), Jorge Teixeira (1900106), Rafael Fonseca (1211622)

---

## Estrutura do repositório

```
bassoon/
├── bassoon-engine/   Motor de inferência Drools + modelos + base de conhecimento
├── bassoon-api/      Spring Boot 4 - REST API + integração LLM
└── bassoon-ui/       Vue 3 - interface do professor
```

---

## Arquitetura e fluxo

```
Professor preenche formulário (Vue)
        ↓
POST /recommend  (Spring Boot - RecommendationController)
        ↓
DroolsService: injeta Evidence na sessão Drools + fireAllRules()
        ↓
Regras Drools: R1, R2, R4, R6
        ↓
Java: R5 (ordenação) + R7 (pré-requisitos)
        ↓
JustificationService: chama API do LLM com regras fired + resultados
        ↓
RecommendationResponse devolvido ao Vue (obras + justificação)
```

---

## Regras

| Regra | Onde | Descrição | @CF |
|-------|------|-----------|-----|
| R1 | Drools | Mapeamento nível aluno -> faixa de dificuldade | 0.90 |
| R2 | Drools | Correção da faixa por motivação | 0.80 |
| R4 | Drools | Pontuação de obras por nível de competência | varia por nível |
| R6 | Drools | Penalização por repetição de período estilístico | -0.75 |
| R5 | Java   | Ordenação das obras por score | - |
| R7 | Java   | Verificação de pré-requisitos (precisa da lista já ordenada) | - |

**Porquê R5 e R7 em Java:** R5 é uma ordenação, não uma inferência. R7 precisa de saber
qual é a obra em primeiro lugar, o que só existe após R5 - não é possível em Drools.

### R4 - detalhe

Uma regra por nível de NivelCompetencia, cada uma com @CF diferente:

| Nível | @CF | Significado |
|-------|-----|-------------|
| REFERENCIA | 1.00 | Obra de referência para esta competência |
| ALTA | 0.70 | Muito recomendada |
| MEDIA | 0.30 | Trabalha a competência mas há melhores |
| BAIXA | sem regra | Não contribui (CF=0) |
| NENHUMA | -0.50 | Penaliza (obra desaconselhada para esta competência) |

### Incerteza com direção (R1 e R2)

O professor pode indicar CF parcial + direção da dúvida:
- "80% certo que é intermédio, incerteza para avançado"
  -> R1 cria Hypothesis faixa {3,4} com CF=0.72 E faixa {5,6} com CF=0.18
- "60% certo que motivação é neutra, incerteza para alta"
  -> R2 aplica ajuste nulo com CF=0.60 E ajuste +1 com CF=0.40

---

## Modelos (bassoon-engine)

### Enums

| Enum | Valores |
|------|---------|
| NivelAluno | INICIANTE, INTERMEDIO, AVANCADO |
| Motivacao | ALTA, NEUTRA, BAIXA |
| Epoca | BARROCO, CLASSICO, ROMANTICO, CONTEMPORANEO, OUTRO |
| Acompanhamento | SOLO, PIANO, BAIXO_CONTINUO, ORQUESTRA |
| Competencia | 24 competências (ver tabela abaixo) |
| NivelCompetencia | REFERENCIA, ALTA, MEDIA, BAIXA, NENHUMA |
| EvidenceType | NIVEL_ALUNO, NIVEL_INCERTEZA, COMPETENCIA_1/2/3, MOTIVACAO, MOTIVACAO_INCERTEZA, ULTIMO_PERIODO |

### Competências (24 - definidas pelo perito)

| Grupo | Competências |
|-------|-------------|
| Articulação | LEGATO, STACCATO |
| Registo | REGISTO_GRAVE, REGISTO_MEDIO, REGISTO_AGUDO, REGISTO_SOBREAGUDO |
| Tempo de execução | TEMPO_LENTO, TEMPO_MODERADO, TEMPO_RAPIDO, TEMPO_VIRTUOSO |
| Controlo do som | RESISTENCIA, QUALIDADE_SOM, FLEXIBILIDADE, AFINACAO, DINAMICAS |
| Desafios técnicos | COORDENACAO, FLICKING, TRILOS, ORNAMENTACAO, TECNICA_MEIO_BURACO, TECNICAS_CONTEMPORANEAS |
| Ritmo | COMPLEXIDADE_RITMICA |
| Carácter | CARACTER_TECNICO, CARACTER_EXPRESSIVO |

### Classes principais

- **Evidence**: input do professor (EvidenceType + valor enum + CF)
- **Obra**: facto da KB (metadados + Map<Competencia, NivelCompetencia>)
- **Hypothesis**: conclusão intermédia das regras (description + value + CF)
- **Recommendation**: resultado final calculado em Java

### Nota importante sobre Hypothesis

Cada Obra precisa de ter uma `Hypothesis("candidatura", nome, 0.0)` inserida
no Main/DroolsService **antes** do `fireAllRules()`. As regras R4 e R6 chamam
`$h.update()` sobre esta hypothesis para acumular o CF via fórmula MYCIN.

---

## Base de conhecimento

31 obras definidas pelo perito (Tabela 9 do relatório prévio).
Para cada obra: compositor, época, país, acompanhamento, dificuldade (1-6),
cfDificuldade (certeza do perito na classificação), pré-requisito (se existe),
e NivelCompetencia para cada uma das 24 competências.

A preencher em `bassoon-engine/src/main/java/.../model/KnowledgeBase.java`
(a criar - classe com método estático que devolve List<Obra>).

---

## Mecanismo de CFs (baseado no projeto Haemorrhage-FC do professor)

- `@CF(valor)` na regra DRL define o CF da regra
- `TrackingAgendaListener` interceta cada disparo e calcula:
  `CF_conclusao = min(CF_evidencias_LHS) * CF_regra`
- Múltiplas regras sobre a mesma Hypothesis acumulam via fórmula MYCIN:
  - ambos positivos: `CF1 + CF2 * (1 - CF1)`
  - ambos negativos: `CF1 + CF2 * (1 + CF1)`
  - mistos: `(CF1 + CF2) / (1 - min(|CF1|, |CF2|))`

---

## Integração LLM (JustificationService)

Após fireAllRules() e ordenação, o JustificationService:
1. Monta um prompt com as obras recomendadas e as regras que dispararam
2. Chama a API do LLM (Claude ou OpenAI - a decidir)
3. Devolve justificação em linguagem natural para o professor

A chave da API é configurada em `application.properties` (não commitar no git).

---

## Referências

- Projeto base seguido: `projetos_prof/ficha_4/Haemorrhage-FC` (padrão de CFs)
- Estrutura geral: `projetos_prof/ficha_0/Drools-FruitClassification`
- Relatório prévio: disponível no grupo com tabelas de obras e competências
