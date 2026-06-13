import { NIVEIS, nivelLabel } from '../labels'

const MIN = 1.5
const MAX = 5.5

/**
 * Continuous student-level slider (1.5–5.5) with iniciante / intermédio / avançado marks.
 * `value` is null until the teacher interacts — an explicit choice is required.
 */
export default function LevelSlider({ value, onChange }) {
  const touched = value != null
  const display = touched ? value : (MIN + MAX) / 2
  const pct = ((display - MIN) / (MAX - MIN)) * 100
  const current = touched ? nivelLabel(value) : null

  return (
    <div className={`cf-range level-slider ${touched ? '' : 'untouched'}`}>
      <input
        type="range"
        min={MIN}
        max={MAX}
        step="0.1"
        value={display}
        onChange={(e) => onChange(parseFloat(e.target.value))}
        className="cf-native"
        style={{ '--pct': `${pct}%` }}
        aria-label="Nível do aluno"
      />
      <div className="level-marks">
        {NIVEIS.map((n) => (
          <span
            key={n.value}
            className={`level-mark ${current === n.label ? 'active' : ''}`}
          >
            {n.label}
          </span>
        ))}
      </div>
    </div>
  )
}
