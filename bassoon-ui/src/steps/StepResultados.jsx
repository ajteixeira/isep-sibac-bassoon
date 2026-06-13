import { useState, useEffect } from 'react'
import { EPOCAS, ACOMPANHAMENTOS, COMP_BY_ID, NIVEIS, MOTIVACOES } from '../labels'
import FooterNav from '../components/FooterNav'

const eraLabel = (id) => EPOCAS.find((e) => e.id === id)?.label ?? id
const acompLabel = (id) => ACOMPANHAMENTOS.find((a) => a.id === id)?.label ?? id

function YouTubeEmbed({ videoLink, title }) {
  if (!videoLink) {
    return (
      <div className="yt-empty">
        <span>sem gravação de referência</span>
      </div>
    )
  }
  return (
    <div className="yt-embed">
      <iframe
        src={`https://www.youtube-nocookie.com/embed/${videoLink}?rel=0&modestbranding=1`}
        title={`Gravacao de referencia - ${title}`}
        loading="lazy"
        allow="accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        sandbox="allow-scripts allow-same-origin allow-presentation"
        allowFullScreen
      />
    </div>
  )
}

function RecommendCard({ work, idx, total }) {
  return (
    <article className="rec-card">
      <header className="rec-card-head">
        <span className="rch-counter">
          obra <strong>{String(idx + 1).padStart(2, '0')}</strong>
          <span className="of"> / {String(total).padStart(2, '0')}</span>
        </span>
        <span className="rch-rule">ordenadas por score</span>
      </header>

      <div className="rec-card-body">
        <h3 className="rec-title">{work.workName}</h3>
        <div className="rec-composer">{work.composer}</div>

        <div className="rec-meta">
          <div>
            <span className="m-key">período</span>
            {eraLabel(work.era)}
          </div>
          {work.country && (
            <div>
              <span className="m-key">país</span>
              {work.country}
            </div>
          )}
          {work.accompaniment && (
            <div>
              <span className="m-key">acompanhamento</span>
              {acompLabel(work.accompaniment)}
            </div>
          )}
        </div>

        {work.prerequisite && (
          <div className="work-prereq">
            pre-req - <strong>{work.prerequisite}</strong>
          </div>
        )}
      </div>

      <YouTubeEmbed videoLink={work.videoLink} title={work.workName} />
    </article>
  )
}

function RecommendList({ recs, currentIdx, onPick }) {
  return (
    <ol className="rec-list">
      {recs.map((w, i) => {
        const isCurrent = i === currentIdx
        return (
          <li
            key={w.workName}
            className={`rec-list-row ${isCurrent ? 'current' : ''}`}
            onClick={() => onPick(i)}
            role="button"
            tabIndex={0}
            onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); onPick(i); } }}
            aria-label={`Ver detalhes de ${w.workName}`}
          >
            <span className="rl-thumb" aria-hidden="true">
              {w.videoLink && (
                <img
                  src={`https://i.ytimg.com/vi/${w.videoLink}/mqdefault.jpg`}
                  alt=""
                  onError={(e) => {
                    e.currentTarget.style.display = 'none'
                  }}
                />
              )}
              <span className="rl-play" aria-hidden="true">
              </span>
            </span>

            <div className="rl-body">
              <div className="rl-rank-row">
                <span className="rl-rank">
                  {String(i + 1).padStart(2, '0')}
                </span>
                <span className="rl-score">score {w.score.toFixed(2)}</span>
                {w.prerequisite && (
                  <span className="rl-prereq">
                    pre-req &mdash; {w.prerequisite}
                  </span>
                )}
              </div>
              <div className="rl-title">{w.workName}</div>
              <div className="rl-composer">{w.composer}</div>
              <div className="rl-meta">
                <span>{eraLabel(w.era)}</span>
                {w.country && <span>- {w.country}</span>}
                {w.accompaniment && (
                  <span>- {acompLabel(w.accompaniment)}</span>
                )}
              </div>
            </div>
          </li>
        )
      })}
    </ol>
  )
}

/**
 * Step 6 — results screen. Shows a ranked list of recommended works
 * and their LLM-generated justifications.
 */
export default function StepResultados({
  results,
  loading,
  error,
  onRestart,
  onBack,
  state,
}) {
  const [showInference, setShowInference] = useState(false)
  const [currentIdx, setCurrentIdx] = useState(0)
  const [viewMode, setViewMode] = useState('single')

  useEffect(() => {
    window.scrollTo(0, 0)
  }, [viewMode, currentIdx])

  if (loading) {
    return (
      <div className="step">
        <div className="loading-shell">
          <div className="l-title">
            A consultar o
            <br />
            <em>motor pericial</em>...
          </div>
          <div className="l-pipeline">
            <span className="stage">factos</span>
            <span className="stage">regras</span>
            <span className="stage">ordenação</span>
            <span className="stage">justificação</span>
          </div>
        </div>
        <FooterNav onBack={onBack} />
      </div>
    )
  }

  if (error) {
    return (
      <div className="step">
        <div className="results-head">
          <div className="display">
            <em>Erro</em> na recomendação.
          </div>
        </div>
        <div className="error-shell">
          <div className="e-title">{error.title || 'Erro de comunicacao'}</div>
          <div className="e-body">{error.message}</div>
        </div>
        <FooterNav
          onBack={onBack}
          onNext={onRestart}
          nextLabel="recomeçar"
        />
      </div>
    )
  }

  if (!results) {
    return (
      <div className="step">
        <div className="loading-shell">
          <div className="l-title">A preparar...</div>
        </div>
        <FooterNav onBack={onBack} />
      </div>
    )
  }

  const recs = results.recommendations || []

  if (recs.length === 0) {
    return (
      <div className="step">
        <div className="results-head">
          <div className="display">
            Nenhuma obra
            <br />
            <em>recomendada.</em>
          </div>
          <div className="subtitle">
            Nenhuma obra do catalogo corresponde aos criterios indicados.
            Tenta ajustar o nivel do aluno ou as competencias.
          </div>
        </div>
        <FooterNav
          onBack={onBack}
          onNext={onRestart}
          nextLabel="nova consulta"
        />
      </div>
    )
  }

  const current = recs[currentIdx]
  const hasNext = currentIdx < recs.length - 1

  const pickFromList = (i) => {
    setCurrentIdx(i)
    setViewMode('single')
  }

  if (viewMode === 'list') {
    return (
      <div className="step">
        <div className="results-head">
          <div className="display">
            {recs.length} obras
            <br />
            <em>recomendadas.</em>
          </div>
          <div className="subtitle">
            ordenadas por score - clica numa para a explorar em detalhe
          </div>
        </div>

        <RecommendList
          recs={recs}
          currentIdx={currentIdx}
          onPick={pickFromList}
        />

        <div className="rec-actions">
          <button
            type="button"
            className="btn"
            onClick={() => setViewMode('single')}
          >
            &larr; voltar à recomendação
          </button>
        </div>

        <FooterNav
          onBack={onBack}
          onNext={onRestart}
          nextLabel="nova consulta"
        />
      </div>
    )
  }

  return (
    <div className="step">
      <div className="results-head">
        <div className="display">
          Recomendação
          <br />
          <em>#{currentIdx + 1}</em>
        </div>
        <div className="subtitle">
          ordenada por score - obras encontradas no total: {recs.length}
        </div>
      </div>

      <RecommendCard work={current} idx={currentIdx} total={recs.length} />

      <div className="rec-actions">
        <button
          type="button"
          className="btn btn-primary"
          onClick={() => hasNext && setCurrentIdx(currentIdx + 1)}
          disabled={!hasNext}
        >
          {hasNext
            ? 'próxima recomendação'
            : 'fim das obras'}
        </button>
        <button
          type="button"
          className="btn btn-ghost"
          onClick={() => setViewMode('list')}
        >
          ver lista ordenada
        </button>
        {!hasNext && (
          <button
            type="button"
            className="btn btn-ghost"
            onClick={() => setCurrentIdx(0)}
          >
            voltar à primeira
          </button>
        )}
      </div>

      {/* LLM justification */}
      <div className="justif">
        <div className="justif-head">
          <span className="j-title">Comentário sobre esta obra</span>
        </div>
        <div className="justif-body">
          {current.justification ? (
            <p>{current.justification}</p>
          ) : (
            <p className="hint">
              Sem justificacao disponivel.
            </p>
          )}
        </div>
      </div>

      {/* Inference panel */}
      <div className="inference-toggle">
        <button
          type="button"
          className="btn-link"
          onClick={() => setShowInference(!showInference)}
        >
          [ {showInference ? 'ocultar' : 'ver'} regras desta obra ]
        </button>

        {showInference && (
          <InferencePanel
            firedRules={current.firedRules}
            workName={current.workName}
            initialScore={current.initialScore}
            score={current.score}
            state={state}
          />
        )}
      </div>

      <FooterNav
        onBack={onBack}
        onNext={onRestart}
        nextLabel="nova consulta"
      />
    </div>
  )
}

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
  ERA: { label: 'Época' },
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

function InferencePanel({ firedRules, workName, initialScore, score, state }) {
  const rules = (firedRules || []).filter((r) => typeof r === 'object')
  if (rules.length === 0) {
    return (
      <div className="inf-panel">
        <div className="inf-empty">Nenhuma regra disparada para esta obra.</div>
      </div>
    )
  }

  const nivelLabel = NIVEIS.find((n) => n.id === state.nivelAluno)?.label || state.nivelAluno
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
              nível {nivelLabel.toLowerCase()} &middot; motivação {motivLabel}
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
