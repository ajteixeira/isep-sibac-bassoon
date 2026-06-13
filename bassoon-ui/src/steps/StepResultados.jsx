import { useState, useEffect } from 'react'
import { eraLabel, acompLabel } from '../labels'
import FooterNav from '../components/FooterNav'
import InferencePanel from '../components/InferencePanel'

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
