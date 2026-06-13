// ---------------------------------------------------------------------------
// Labels - PT
// ---------------------------------------------------------------------------

// Default certainty factor when the teacher adds a skill / accompaniment
export const DEFAULT_CF = 0.75

// Anchor marks for the continuous level slider (1.5–5.5 axis).
export const NIVEIS = [
  { value: 1.5, label: 'Iniciante' },
  { value: 3.5, label: 'Intermédio' },
  { value: 5.5, label: 'Avançado' },
]

// Maps a continuous level (1.5–5.5) to the nearest word label.
export const nivelLabel = (v) => {
  if (v == null) return '—'
  if (v < 2.5) return 'Iniciante'
  if (v < 4.5) return 'Intermédio'
  return 'Avançado'
}

export const MOTIVACOES = [
  { id: 'LOW',     label: 'Baixa' },
  { id: 'NEUTRAL', label: 'Neutra' },
  { id: 'HIGH',    label: 'Alta' },
]

export const EPOCAS = [
  { id: 'BAROQUE',      label: 'Barroco',       years: 'c. 1600-1750' },
  { id: 'CLASSICAL',    label: 'Clássico',      years: 'c. 1750-1820' },
  { id: 'ROMANTIC',     label: 'Romântico',     years: 'c. 1820-1910' },
  { id: 'CONTEMPORARY', label: 'Contemporâneo', years: '1910 - hoje' },
  { id: 'OTHER',        label: 'Outro',         years: 'indefinido' },
]

export const COMP_GROUPS = [
  {
    id: 'articulacao',
    title: 'Articulação',
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
      { id: 'MID_REGISTER',      label: 'Médio' },
      { id: 'HIGH_REGISTER',      label: 'Agudo' },
      { id: 'VERY_HIGH_REGISTER', label: 'Sobreagudo' },
    ],
  },
  {
    id: 'tempo',
    title: 'Tempo de execução',
    items: [
      { id: 'SLOW_TEMPO',     label: 'Lento' },
      { id: 'MODERATE_TEMPO', label: 'Moderado' },
      { id: 'FAST_TEMPO',     label: 'Rápido' },
      { id: 'VIRTUOSO_TEMPO', label: 'Virtuoso' },
    ],
  },
  {
    id: 'som',
    title: 'Controlo do som',
    items: [
      { id: 'ENDURANCE',      label: 'Resistência' },
      { id: 'SOUND_QUALITY',  label: 'Qualidade do som' },
      { id: 'FLEXIBILITY',    label: 'Flexibilidade' },
      { id: 'INTONATION',     label: 'Afinação' },
      { id: 'DYNAMICS',       label: 'Dinâmicas' },
    ],
  },
  {
    id: 'desafios',
    title: 'Desafios técnicos',
    items: [
      { id: 'COORDINATION',            label: 'Coordenação' },
      { id: 'FLICKING',                label: 'Flicking' },
      { id: 'TRILLS',                  label: 'Trilos' },
      { id: 'ORNAMENTATION',           label: 'Ornamentação' },
      { id: 'HALF_HOLE_TECHNIQUE',     label: 'Meio-buraco' },
      { id: 'CONTEMPORARY_TECHNIQUES', label: 'Técnicas contemporâneas' },
    ],
  },
  {
    id: 'ritmo',
    title: 'Ritmo',
    items: [
      { id: 'RHYTHMIC_COMPLEXITY', label: 'Complexidade rítmica' },
    ],
  },
  {
    id: 'caracter',
    title: 'Carácter',
    items: [
      { id: 'TECHNICAL_CHARACTER', label: 'Técnico' },
      { id: 'EXPRESSIVE_CHARACTER', label: 'Expressivo' },
    ],
  },
]

export const ACOMPANHAMENTOS = [
  { id: 'SOLO',           label: 'Solo' },
  { id: 'PIANO',          label: 'Piano' },
  { id: 'BASSO_CONTINUO', label: 'Baixo contínuo' },
  { id: 'ORCHESTRA',      label: 'Orquestra' },
]

// id -> label lookups (fall back to the id if unknown)
export const eraLabel = (id) => EPOCAS.find((e) => e.id === id)?.label ?? id
export const acompLabel = (id) => ACOMPANHAMENTOS.find((a) => a.id === id)?.label ?? id

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
