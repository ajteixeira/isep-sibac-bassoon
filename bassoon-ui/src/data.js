// ---------------------------------------------------------------------------
// Dados estaticos do dominio - fagote
// ---------------------------------------------------------------------------

export const NIVEIS = [
  { id: 'INICIANTE',  label: 'Iniciante',  desc: 'Pouca autonomia - acompanhamento proximo' },
  { id: 'INTERMEDIO', label: 'Intermedio', desc: 'Em construcao - repertorio consolidado' },
  { id: 'AVANCADO',   label: 'Avancado',   desc: 'Autonomia plena - capacidade de recital' },
]

export const MOTIVACOES = [
  { id: 'BAIXA',  label: 'Baixa',  desc: 'Precisa de repertorio acessivel' },
  { id: 'NEUTRA', label: 'Neutra', desc: 'Sem ajuste na dificuldade' },
  { id: 'ALTA',   label: 'Alta',   desc: 'Aceita repertorio exigente' },
]

export const EPOCAS = [
  { id: 'BARROCO',       label: 'Barroco',       years: 'c. 1600-1750' },
  { id: 'CLASSICO',      label: 'Classico',      years: 'c. 1750-1820' },
  { id: 'ROMANTICO',     label: 'Romantico',     years: 'c. 1820-1910' },
  { id: 'CONTEMPORANEO', label: 'Contemporaneo', years: '1910 - hoje' },
  { id: 'OUTRO',         label: 'Outro',         years: 'indefinido' },
]

export const COMP_GROUPS = [
  {
    id: 'articulacao',
    title: 'Articulacao',
    items: [
      { id: 'LEGATO',   label: 'Legato' },
      { id: 'STACCATO', label: 'Staccato' },
    ],
  },
  {
    id: 'registo',
    title: 'Registo',
    items: [
      { id: 'REGISTO_GRAVE',      label: 'Grave' },
      { id: 'REGISTO_MEDIO',      label: 'Medio' },
      { id: 'REGISTO_AGUDO',      label: 'Agudo' },
      { id: 'REGISTO_SOBREAGUDO', label: 'Sobreagudo' },
    ],
  },
  {
    id: 'tempo',
    title: 'Tempo de execucao',
    items: [
      { id: 'TEMPO_LENTO',    label: 'Lento' },
      { id: 'TEMPO_MODERADO', label: 'Moderado' },
      { id: 'TEMPO_RAPIDO',   label: 'Rapido' },
      { id: 'TEMPO_VIRTUOSO', label: 'Virtuoso' },
    ],
  },
  {
    id: 'som',
    title: 'Controlo do som',
    items: [
      { id: 'RESISTENCIA',   label: 'Resistencia' },
      { id: 'QUALIDADE_SOM', label: 'Qualidade do som' },
      { id: 'FLEXIBILIDADE', label: 'Flexibilidade' },
      { id: 'AFINACAO',      label: 'Afinacao' },
      { id: 'DINAMICAS',     label: 'Dinamicas' },
    ],
  },
  {
    id: 'desafios',
    title: 'Desafios tecnicos',
    items: [
      { id: 'COORDENACAO',             label: 'Coordenacao' },
      { id: 'FLICKING',                label: 'Flicking' },
      { id: 'TRILOS',                  label: 'Trilos' },
      { id: 'ORNAMENTACAO',            label: 'Ornamentacao' },
      { id: 'TECNICA_MEIO_BURACO',     label: 'Meio-buraco' },
      { id: 'TECNICAS_CONTEMPORANEAS', label: 'Tecnicas contemporaneas' },
    ],
  },
  {
    id: 'ritmo',
    title: 'Ritmo',
    items: [
      { id: 'COMPLEXIDADE_RITMICA', label: 'Complexidade ritmica' },
    ],
  },
  {
    id: 'caracter',
    title: 'Caracter',
    items: [
      { id: 'CARACTER_TECNICO',    label: 'Tecnico' },
      { id: 'CARACTER_EXPRESSIVO', label: 'Expressivo' },
    ],
  },
]

// Lookup rapido: id -> { label, groupId, groupTitle }
export const COMP_BY_ID = (() => {
  const m = {}
  for (const g of COMP_GROUPS) {
    for (const c of g.items) {
      m[c.id] = { ...c, groupId: g.id, groupTitle: g.title }
    }
  }
  return m
})()

export const ACOMPANHAMENTOS = [
  { id: 'SOLO',           label: 'Solo' },
  { id: 'PIANO',          label: 'Piano' },
  { id: 'BAIXO_CONTINUO', label: 'Baixo continuo' },
  { id: 'ORQUESTRA',      label: 'Orquestra' },
  { id: 'OUTRO',          label: 'Outro' },
]

export const ACOMP_LABELS = {
  SOLO: 'solo',
  PIANO: 'piano',
  BAIXO_CONTINUO: 'baixo continuo',
  ORQUESTRA: 'orquestra',
}
