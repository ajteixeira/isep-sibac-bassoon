import { COMP_BY_ID, MOTIVACOES, nivelLabel, acompLabel, eraLabel } from '../labels'

// ---------------------------------------------------------------------------
// Inference panel — shows fired rules grouped by category with full context
// ---------------------------------------------------------------------------

const RULE_DESCRIPTIONS = {
  'skill REFERENCE': 'obra de referência',
  'skill VERY_SUITABLE': 'muito adequada',
  'skill SUITABLE': 'adequada',
  'skill MODERATE': 'razoável',
  'skill WEAK': 'fraca',
  'skill UNSUITABLE': 'desadequada',
  'skill TOTALLY_UNSUITABLE': 'totalmente desadequada',
  'accompaniment match': 'preferência correspondida',
  'era penalty': 'penalização por repetição de época',
}

const CATEGORY_HEADERS = {
  SKILL: { label: 'Competências' },
  ACCOMPANIMENT: { label: 'Acompanhamento' },
  ERA: { label: 'Época última obra' },
  OTHER: { label: 'Outras regras' },
}

function translateDetail(category, detail) {
  if (!detail) return null
  if (category === 'SKILL') {
    const comp = COMP_BY_ID[detail]
    return comp ? `${comp.groupTitle} — ${comp.label}` : detail
  }
  if (category === 'ACCOMPANIMENT') return acompLabel(detail)
  if (category === 'ERA') return eraLabel(detail)
  return detail
}

export default function InferencePanel({ firedRules, workName, initialScore, score, state }) {
  const rules = (firedRules || []).filter((r) => typeof r === 'object')
  if (rules.length === 0) {
    return (
      <div className="inf-panel">
        <div className="inf-empty">Nenhuma regra disparada para esta obra.</div>
      </div>
    )
  }

  const nivelTexto = nivelLabel(state.nivelAluno)
  const motivLabel = state.motivacao
    ? MOTIVACOES.find((m) => m.id === state.motivacao)?.label.toLowerCase()
    : 'neutra'

  // fired rules split by category
  const firedAccomp = rules.filter((r) => r.category === 'ACCOMPANIMENT')
  const firedEra = rules.filter((r) => r.category === 'ERA')
  const otherGrouped = {}
  for (const r of rules) {
    const cat = r.category || 'OTHER'
    if (cat === 'ACCOMPANIMENT' || cat === 'ERA') continue
    if (!otherGrouped[cat]) otherGrouped[cat] = []
    otherGrouped[cat].push(r)
  }

  // user inputs that decide whether a dimension is shown at all
  const prefAccomp = state.acompanhamentos || []
  const lastEra = state.ultimoPeriodo || null

  // a standard fired-rule row (numeric CF, coloured by sign)
  const firedRow = (category, r) => {
    const detailLabel = translateDetail(category, r.detail)
    const desc = RULE_DESCRIPTIONS[r.name] || r.name
    const positive = r.cf >= 0
    return (
      <div key={r.name + (r.detail || '')} className="inf-rule">
        <span className="inf-rule-subject">{detailLabel || desc}</span>
        <span className="inf-rule-desc">{detailLabel ? desc : ''}</span>
        <span className={`inf-rule-cf ${positive ? 'pos' : 'neg'}`}>
          {positive ? '+' : ''}{r.cf.toFixed(2)}
        </span>
      </div>
    )
  }

  // a "rule did not fire" row — no score impact, neutral white marker
  const idleRow = (key, subject, desc, good) => (
    <div key={key} className="inf-rule">
      <span className="inf-rule-subject">{subject}</span>
      <span className="inf-rule-desc">{desc}</span>
      <span className="inf-rule-cf none">{good ? '✓' : '✗'}</span>
    </div>
  )

  return (
    <div className="inf-panel">
      <div className="inf-head">
        <span className="inf-head-label">regras aplicadas</span>
        <span className="inf-head-score">score {score.toFixed(2)}</span>
        <span className="inf-head-work">{workName}</span>
      </div>

      {initialScore != null && (
        <div className="inf-cat">
          <div className="inf-cat-head">adequação difusa</div>
          <div className="inf-rule">
            <span className="inf-rule-subject">
              nível {nivelTexto.toLowerCase()} &middot; motivação {motivLabel}
            </span>
            <span className="inf-rule-desc" />
            <span className="inf-rule-cf pos">{initialScore.toFixed(2)}</span>
          </div>
        </div>
      )}

      {Object.entries(otherGrouped).map(([category, catRules]) => {
        const header = CATEGORY_HEADERS[category] || CATEGORY_HEADERS.OTHER
        return (
          <div key={category} className="inf-cat">
            <div className="inf-cat-head">{header.label}</div>
            {catRules.map((r) => firedRow(category, r))}
          </div>
        )
      })}

      {prefAccomp.length > 0 && (
        <div className="inf-cat">
          <div className="inf-cat-head">{CATEGORY_HEADERS.ACCOMPANIMENT.label}</div>
          {prefAccomp.map((pref) => {
            const fired = firedAccomp.find((r) => r.detail === pref.id)
            return fired
              ? firedRow('ACCOMPANIMENT', fired)
              : idleRow(
                  `accomp-${pref.id}`,
                  acompLabel(pref.id),
                  'preferência de acompanhamento não correspondida',
                  false
                )
          })}
        </div>
      )}

      {lastEra && (
        <div className="inf-cat">
          <div className="inf-cat-head">{CATEGORY_HEADERS.ERA.label}</div>
          {firedEra.length > 0
            ? firedEra.map((r) => firedRow('ERA', r))
            : idleRow(
                `era-${lastEra}`,
                eraLabel(lastEra),
                'sem penalização por repetição de época',
                true
              )}
        </div>
      )}
    </div>
  )
}
