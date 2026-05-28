// ---------------------------------------------------------------------------
// Labels - PT
// ---------------------------------------------------------------------------

export const NIVEIS = [
  { id: 'BEGINNER',    label: 'Iniciante',  desc: 'Pouca autonomia - acompanhamento proximo' },
  { id: 'INTERMEDIATE', label: 'Intermedio', desc: 'Em construcao - repertorio consolidado' },
  { id: 'ADVANCED',    label: 'Avancado',   desc: 'Autonomia plena - capacidade de recital' },
]

export const MOTIVACOES = [
  { id: 'LOW',     label: 'Baixa',  desc: 'Precisa de repertorio acessivel' },
  { id: 'NEUTRAL', label: 'Neutra', desc: 'Sem ajuste na dificuldade' },
  { id: 'HIGH',    label: 'Alta',   desc: 'Aceita repertorio exigente' },
]

export const EPOCAS = [
  { id: 'BAROQUE',      label: 'Barroco',       years: 'c. 1600-1750' },
  { id: 'CLASSICAL',    label: 'Classico',      years: 'c. 1750-1820' },
  { id: 'ROMANTIC',     label: 'Romantico',     years: 'c. 1820-1910' },
  { id: 'CONTEMPORARY', label: 'Contemporaneo', years: '1910 - hoje' },
  { id: 'OTHER',        label: 'Outro',         years: 'indefinido' },
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
      { id: 'LOW_REGISTER',      label: 'Grave' },
      { id: 'MID_REGISTER',      label: 'Medio' },
      { id: 'HIGH_REGISTER',      label: 'Agudo' },
      { id: 'VERY_HIGH_REGISTER', label: 'Sobreagudo' },
    ],
  },
  {
    id: 'tempo',
    title: 'Tempo de execucao',
    items: [
      { id: 'SLOW_TEMPO',     label: 'Lento' },
      { id: 'MODERATE_TEMPO', label: 'Moderado' },
      { id: 'FAST_TEMPO',     label: 'Rapido' },
      { id: 'VIRTUOSO_TEMPO', label: 'Virtuoso' },
    ],
  },
  {
    id: 'som',
    title: 'Controlo do som',
    items: [
      { id: 'ENDURANCE',      label: 'Resistencia' },
      { id: 'SOUND_QUALITY',  label: 'Qualidade do som' },
      { id: 'FLEXIBILITY',    label: 'Flexibilidade' },
      { id: 'INTONATION',     label: 'Afinacao' },
      { id: 'DYNAMICS',       label: 'Dinamicas' },
    ],
  },
  {
    id: 'desafios',
    title: 'Desafios tecnicos',
    items: [
      { id: 'COORDINATION',            label: 'Coordenacao' },
      { id: 'FLICKING',                label: 'Flicking' },
      { id: 'TRILLS',                  label: 'Trilos' },
      { id: 'ORNAMENTATION',           label: 'Ornamentacao' },
      { id: 'HALF_HOLE_TECHNIQUE',     label: 'Meio-buraco' },
      { id: 'CONTEMPORARY_TECHNIQUES', label: 'Tecnicas contemporaneas' },
    ],
  },
  {
    id: 'ritmo',
    title: 'Ritmo',
    items: [
      { id: 'RHYTHMIC_COMPLEXITY', label: 'Complexidade ritmica' },
    ],
  },
  {
    id: 'caracter',
    title: 'Caracter',
    items: [
      { id: 'TECHNICAL_CHARACTER', label: 'Tecnico' },
      { id: 'EXPRESSIVE_CHARACTER', label: 'Expressivo' },
    ],
  },
]

export const ACOMPANHAMENTOS = [
  { id: 'SOLO',           label: 'Solo' },
  { id: 'PIANO',          label: 'Piano' },
  { id: 'BASSO_CONTINUO', label: 'Baixo continuo' },
  { id: 'ORCHESTRA',      label: 'Orquestra' },
]

// Quick lookup: id -> { label, groupId, groupTitle }
export const COMP_BY_ID = (() => {
  const m = {}
  for (const g of COMP_GROUPS) {
    for (const c of g.items) {
      m[c.id] = { ...c, groupId: g.id, groupTitle: g.title }
    }
  }
  return m
})()
