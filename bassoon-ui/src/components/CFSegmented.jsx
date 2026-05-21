import { useMemo } from 'react'

export const CF_OPTIONS = [
  { v: 0.20, val: '0.2',  label: 'Nao sei' },
  { v: 0.50, val: '0.5',  label: 'Em duvida' },
  { v: 0.75, val: '0.75', label: 'Confiante' },
  { v: 1.00, val: '1.0',  label: 'Certeza' },
]

export const CF_OPTIONS_COMP = [
  { v: 0.25, val: '0.25', label: 'Vago' },
  { v: 0.50, val: '0.5',  label: 'Util' },
  { v: 0.75, val: '0.75', label: 'Importante' },
  { v: 1.00, val: '1.0',  label: 'Essencial' },
]

export default function CFSegmented({
  value,
  onChange,
  options = CF_OPTIONS,
  showValues = true,
}) {
  const currentIdx = useMemo(() => {
    let best = 0
    let bestDist = Infinity
    options.forEach((o, i) => {
      const d = Math.abs(o.v - value)
      if (d < bestDist) {
        bestDist = d
        best = i
      }
    })
    return best
  }, [value, options])

  return (
    <div className="cf-segmented" role="radiogroup">
      {options.map((o, i) => (
        <button
          key={o.v}
          type="button"
          role="radio"
          aria-checked={currentIdx === i}
          className={`cf-seg ${currentIdx === i ? 'selected' : ''}`}
          onClick={() => onChange(o.v)}
        >
          {showValues && <div className="val">cf {o.val}</div>}
          <div className="lbl">{o.label}</div>
        </button>
      ))}
    </div>
  )
}
