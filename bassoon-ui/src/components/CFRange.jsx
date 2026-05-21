const DEFAULT_CENTER_LABELS = [
  [0.00, 'muito incerto'],
  [0.25, 'em duvida'],
  [0.55, 'razoavelmente certo'],
  [0.80, 'confiante'],
  [0.97, 'certeza absoluta'],
]

export default function CFRange({
  value,
  onChange,
  leftLabel = 'em duvida',
  rightLabel = 'certeza absoluta',
  centerLabels = DEFAULT_CENTER_LABELS,
}) {
  const v = Math.max(0, Math.min(1, value))
  const pct = v * 100

  let caption = centerLabels[0][1]
  for (const [threshold, lbl] of centerLabels) {
    if (v >= threshold) caption = lbl
  }

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
          <span className="caption">{caption}</span>
          <span className="fc-num">FC = {v.toFixed(2)}</span>
        </span>
        <span className="right">{rightLabel} &rarr;</span>
      </div>
    </div>
  )
}
