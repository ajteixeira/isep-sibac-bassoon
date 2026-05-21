import { useState } from 'react'
import { EPOCAS, ACOMP_LABELS } from '../data'
import FooterNav from '../components/FooterNav'


// ---------------------------------------------------------------------------
// YouTube embed
// ---------------------------------------------------------------------------

function YouTubeEmbed({ videoId, title }) {
  return (
    <div className="yt-embed">
      <iframe
        src={`https://www.youtube-nocookie.com/embed/${videoId}?rel=0&modestbranding=1`}
        title={`Gravacao de referencia - ${title}`}
        loading="lazy"
        allow="accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowFullScreen
      />
    </div>
  )
}

// ---------------------------------------------------------------------------
// Card de uma recomendacao (vista single)
// ---------------------------------------------------------------------------

function RecommendCard({ work, idx, total }) {
  const epocaLabel =
    EPOCAS.find((e) => e.id === work.epoca)?.label || work.epoca
  const acomp =
    ACOMP_LABELS[work.acompanhamento] ||
    work.acompanhamento?.toLowerCase()

  return (
    <article className="rec-card">
      <header className="rec-card-head">
        <span className="rch-counter">
          obra <strong>{String(idx + 1).padStart(2, '0')}</strong>
          <span className="of"> / {String(total).padStart(2, '0')}</span>
        </span>
        <span className="rch-rule">R5 - ordenada por score</span>
      </header>

      <div className="rec-card-body">
        <h3 className="rec-title">{work.nomeObra}</h3>
        <div className="rec-composer">{work.compositor}</div>

        <div className="rec-meta">
          <div>
            <span className="m-key">per</span>
            {epocaLabel}
          </div>
          {work.pais && (
            <div>
              <span className="m-key">pais</span>
              {work.pais}
            </div>
          )}
          {acomp && (
            <div>
              <span className="m-key">acmp</span>
              {acomp}
            </div>
          )}
        </div>

        {work.preRequisito && (
          <div className="work-prereq">
            pre-req - <strong>{work.preRequisito}</strong>
          </div>
        )}
      </div>

      {work.youtubeId ? (
        <YouTubeEmbed videoId={work.youtubeId} title={work.nomeObra} />
      ) : (
        <div className="yt-empty">
          <span>sem gravacao de referencia</span>
        </div>
      )}
    </article>
  )
}

// ---------------------------------------------------------------------------
// Lista compacta (todas as recomendacoes)
// ---------------------------------------------------------------------------

function RecommendList({ recs, currentIdx, onPick }) {
  return (
    <ol className="rec-list">
      {recs.map((w, i) => {
        const epocaLabel =
          EPOCAS.find((e) => e.id === w.epoca)?.label || w.epoca
        const isCurrent = i === currentIdx
        return (
          <li
            key={w.nomeObra}
            className={`rec-list-row ${isCurrent ? 'current' : ''}`}
          >
            <button
              type="button"
              className="rl-thumb"
              onClick={() => onPick(i)}
              aria-label={`Ver ${w.nomeObra}`}
            >
              {w.youtubeId && (
                <img
                  src={`https://i.ytimg.com/vi/${w.youtubeId}/mqdefault.jpg`}
                  alt=""
                  onError={(e) => {
                    e.currentTarget.style.display = 'none'
                  }}
                />
              )}
              <span className="rl-play" aria-hidden="true">
                &#9654;
              </span>
            </button>

            <div className="rl-body">
              <div className="rl-rank-row">
                <span className="rl-rank">
                  {String(i + 1).padStart(2, '0')}
                </span>
                <span className="rl-score">score {w.score.toFixed(2)}</span>
                {isCurrent && (
                  <span className="rl-pill current-pill">a estudar</span>
                )}
              </div>
              <div className="rl-title">{w.nomeObra}</div>
              <div className="rl-composer">{w.compositor}</div>
              <div className="rl-meta">
                <span>{epocaLabel}</span>
                {w.pais && <span>- {w.pais}</span>}
                {w.acompanhamento && (
                  <span>
                    -{' '}
                    {ACOMP_LABELS[w.acompanhamento] ||
                      w.acompanhamento.toLowerCase()}
                  </span>
                )}
              </div>
            </div>

            <div className="rl-action">
              <button
                type="button"
                className="btn-link"
                onClick={() => onPick(i)}
              >
                {isCurrent ? 'atual' : 'ver →'}
              </button>
            </div>
          </li>
        )
      })}
    </ol>
  )
}

// ---------------------------------------------------------------------------
// Passo V - Resultados
// ---------------------------------------------------------------------------

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

  // --- Loading ---
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

  // --- Error ---
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
          nextLabel="&#8635; recomecar"
          meta="passo v de v - recomendacao"
        />
      </div>
    )
  }

  // --- Waiting ---
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

  const recs = results.recomendacoes || []
  const current = recs[currentIdx]
  const hasNext = currentIdx < recs.length - 1

  const pickFromList = (i) => {
    setCurrentIdx(i)
    setViewMode('single')
  }

  // --- List view ---
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
          nextLabel="&#8635; nova consulta"
          meta="passo v de v - recomendacao"
        />
      </div>
    )
  }

  // --- Single view ---
  return (
    <div className="step">
      <div className="results-head">
        <div className="display">
          Recomendacao
          <br />
          <em>#{currentIdx + 1}</em>
        </div>
        <div className="subtitle">
          ordenada por score - {recs.length} obras encontradas no total
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
            ? 'proxima recomendacao →'
            : '&#10003; chegaste a ultima obra'}
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
            &#8634; voltar a primeira
          </button>
        )}
      </div>

      {/* Justificacao LLM */}
      <div className="justif">
        <div className="justif-head">
          <span className="j-title">Comentario - sobre esta obra</span>
          <span className="j-attr">
            gerado por LLM - revisivel pelo professor
          </span>
        </div>
        <div className="justif-body">
          {current.comentario ? (
            <p>{current.comentario}</p>
          ) : (
            <p className="hint">
              Sem comentario disponivel para esta obra.
            </p>
          )}
        </div>
      </div>

      {/* Painel de inferencia (regras disparadas) */}
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
            <h4>Regras disparadas - {current.nomeObra}</h4>
            <div className="work-block">
              <div className="wb-head">
                <span className="wb-name">
                  {current.nomeObra}
                  <span className="wb-diff">
                    {' '}
                    - dificuldade {current.dificuldade}/6
                  </span>
                </span>
                <span className="wb-score">
                  score - {current.score.toFixed(3)}
                </span>
              </div>
              {current.regrasFired?.map((r, i) => (
                <div key={i} className="rule-row">
                  <span className="r-id">{r.id}</span>
                  <span className="r-desc">{r.desc}</span>
                  <span className="r-cf">
                    {r.cf > 0 ? '+' : ''}
                    {r.cf.toFixed(2)}
                  </span>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      <FooterNav
        onBack={onBack}
        onNext={onRestart}
        nextLabel="&#8635; nova consulta"
        meta="passo v de v - recomendacao"
      />
    </div>
  )
}
