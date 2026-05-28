import { useState } from 'react'
import { EPOCAS, ACOMPANHAMENTOS } from '../labels'
import FooterNav from '../components/FooterNav'

function YouTubeEmbed({ videoLink, title }) {
  if (!videoLink) {
    return (
      <div className="yt-empty">
        <span>sem gravacao de referencia</span>
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
        allowFullScreen
      />
    </div>
  )
}

function RecommendCard({ work, idx, total }) {
  const epocaLabel =
    EPOCAS.find((e) => e.id === work.era)?.label || work.era
  const acomp =
    ACOMPANHAMENTOS.find((a) => a.id === work.accompaniment)?.label ||
    work.accompaniment?.toLowerCase()

  return (
    <article className="rec-card">
      <header className="rec-card-head">
        <span className="rch-counter">
          obra <strong>{String(idx + 1).padStart(2, '0')}</strong>
          <span className="of"> / {String(total).padStart(2, '0')}</span>
        </span>
        <span className="rch-rule">ordered by score</span>
      </header>

      <div className="rec-card-body">
        <h3 className="rec-title">{work.workName}</h3>
        <div className="rec-composer">{work.composer}</div>

        <div className="rec-meta">
          <div>
            <span className="m-key">per</span>
            {epocaLabel}
          </div>
          {work.country && (
            <div>
              <span className="m-key">pais</span>
              {work.country}
            </div>
          )}
          {acomp && (
            <div>
              <span className="m-key">acmp</span>
              {acomp}
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
        const epocaLabel =
          EPOCAS.find((e) => e.id === w.era)?.label || w.era
        const isCurrent = i === currentIdx
        return (
          <li
            key={w.workName}
            className={`rec-list-row ${isCurrent ? 'current' : ''}`}
          >
            <button
              type="button"
              className="rl-thumb"
              onClick={() => onPick(i)}
              aria-label={`View ${w.workName}`}
            >
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
            </button>

            <div className="rl-body">
              <div className="rl-rank-row">
                <span className="rl-rank">
                  {String(i + 1).padStart(2, '0')}
                </span>
                <span className="rl-score">score {w.score.toFixed(2)}</span>
              </div>
              <div className="rl-title">{w.workName}</div>
              <div className="rl-composer">{w.composer}</div>
              <div className="rl-meta">
                <span>{epocaLabel}</span>
                {w.country && <span>- {w.country}</span>}
                {w.accompaniment && (
                  <span>
                    -{' '}
                    {ACOMPANHAMENTOS.find((a) => a.id === w.accompaniment)?.label ||
                      w.accompaniment.toLowerCase()}
                  </span>
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
}) {
  const [showInference, setShowInference] = useState(false)
  const [currentIdx, setCurrentIdx] = useState(0)
  const [viewMode, setViewMode] = useState('single')

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
            <span className="stage done">factos</span>
            <span className="stage live">regras</span>
            <span className="stage">ordenacao</span>
            <span className="stage">justificacao</span>
          </div>
        </div>
        <FooterNav onBack={onBack} meta="passo v de v - recomendacao" />
      </div>
    )
  }

  if (error) {
    return (
      <div className="step">
        <div className="results-head">
          <div className="display">
            <em>Erro</em> na recomendacao.
          </div>
        </div>
        <div className="error-shell">
          <div className="e-title">{error.title || 'Erro de comunicacao'}</div>
          <div className="e-body">{error.message}</div>
        </div>
        <FooterNav
          onBack={onBack}
          onNext={onRestart}
          nextLabel="recomecar"
          meta="passo v de v - recomendacao"
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
        <FooterNav onBack={onBack} meta="passo v de v - recomendacao" />
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
          meta="passo v de v - recomendacao"
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
            &larr; voltar a recomendacao
          </button>
        </div>

        <FooterNav
          onBack={onBack}
          onNext={onRestart}
          nextLabel="nova consulta"
          meta="passo v de v - recomendacao"
        />
      </div>
    )
  }

  return (
    <div className="step">
      <div className="results-head">
        <div className="display">
          Recomendacao
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
            ? 'proxima recomendacao'
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
            voltar a primeira
          </button>
        )}
      </div>

      {/* LLM justification */}
      <div className="justif">
        <div className="justif-head">
          <span className="j-title">Comentario - sobre esta obra</span>
          <span className="j-attr">
            gerado por LLM - revisivel pelo professor
          </span>
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
          <div className="inference-panel" style={{ textAlign: 'left' }}>
            <h4>Rules fired — {current.workName}</h4>
            <div className="work-block">
              <div className="wb-head">
                <span className="wb-name">
                  {current.workName}
                  <span className="wb-diff">
                    {' '}
                    — difficulty {current.difficulty}/6
                  </span>
                </span>
                <span className="wb-score">
                  score - {current.score.toFixed(3)}
                </span>
              </div>
              {current.firedRules?.map((r, i) => (
                <div key={i} className="rule-row">
                  {typeof r === 'string' ? (
                    <span className="r-id" style={{ gridColumn: '1 / -1' }}>{r}</span>
                  ) : (
                    <>
                      <span className="r-id">{r.id}</span>
                      <span className="r-desc">{r.desc}</span>
                      <span className="r-cf">
                        {r.cf > 0 ? '+' : ''}
                        {r.cf.toFixed(2)}
                      </span>
                    </>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      <FooterNav
        onBack={onBack}
        onNext={onRestart}
        nextLabel="nova consulta"
        meta="passo v de v - recomendacao"
      />
    </div>
  )
}
