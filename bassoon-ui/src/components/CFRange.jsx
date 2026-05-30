/** Certainty factor slider. */
export default function CFRange({
  value,
  onChange,
  leftLabel = 'pouco prioritario',
  rightLabel = 'muito prioritario',
}) {
  const v = Math.max(0, Math.min(1, value))
  const pct = v * 100

  return (
    <div className="cf-range">
      <input
        type="range"
        min="0"
        max="1"
        step="0.01"
        value={v}
        onChange={(e) => onChange(parseFloat(e.target.value))}
        className="cf-native"
        style={{ '--pct': `${pct}%` }}
        aria-label="Factor de certeza"
      />
      <div className="cf-range-labels">
        <span className="left">&larr; {leftLabel}</span>
        <span className="center">
          <span className="fc-num">CF = {v.toFixed(2)}</span>
        </span>
        <span className="right">{rightLabel} &rarr;</span>
      </div>
    </div>
  )
}
