export default function RadioRow({ options, value, onChange, columns = 3 }) {
  return (
    <div className={`radio-row cols-${columns}`}>
      {options.map((o, i) => (
        <button
          key={o.id}
          type="button"
          className={`radio-card ${value === o.id ? 'selected' : ''}`}
          onClick={() => onChange(o.id)}
        >
          <div className="rc-num">{String(i + 1).padStart(2, '0')}</div>
          <div className="rc-title">{o.label}</div>
          {o.desc && <div className="rc-desc">{o.desc}</div>}
          {o.years && <div className="rc-desc">{o.years}</div>}
          <div className="rc-mark">&bull;</div>
        </button>
      ))}
    </div>
  )
}
