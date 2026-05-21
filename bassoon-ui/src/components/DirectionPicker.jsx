export default function DirectionPicker({ value, onChange, leftOption, rightOption }) {
  return (
    <div className="dir-picker">
      <button
        type="button"
        className={`dir-btn ${value === leftOption.id ? 'on' : ''}`}
        onClick={() => onChange(value === leftOption.id ? null : leftOption.id)}
      >
        <span className="arrow">&larr;</span>
        <span>inclina para {leftOption.label.toLowerCase()}</span>
      </button>

      <button
        type="button"
        className={`dir-btn neutral ${value === null ? 'on' : ''}`}
        onClick={() => onChange(null)}
      >
        sem inclinacao
      </button>

      <button
        type="button"
        className={`dir-btn ${value === rightOption.id ? 'on' : ''}`}
        onClick={() => onChange(value === rightOption.id ? null : rightOption.id)}
      >
        <span>inclina para {rightOption.label.toLowerCase()}</span>
        <span className="arrow">&rarr;</span>
      </button>
    </div>
  )
}
