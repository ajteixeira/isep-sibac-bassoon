/** Navigation bar with back / next buttons and step metadata. */
export default function FooterNav({
  onBack,
  onNext,
  nextLabel,
  nextDisabled,
  meta,
  nextEmphatic,
}) {
  return (
    <div className="sheet-footer">
      <div>
        {onBack ? (
          <button type="button" className="btn btn-ghost" onClick={onBack}>
            &larr; anterior
          </button>
        ) : (
          <span />
        )}
      </div>

      <div className="footer-meta">{meta}</div>

      <div>
        {onNext && (
          <button
            type="button"
            className="btn btn-primary"
            onClick={onNext}
            disabled={nextDisabled}
            style={
              nextEmphatic
                ? { background: 'var(--accent)', borderColor: 'var(--accent)' }
                : undefined
            }
          >
            {nextLabel || 'seguinte →'}
          </button>
        )}
      </div>
    </div>
  )
}
