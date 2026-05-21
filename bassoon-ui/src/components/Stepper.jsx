const ROMANS = ['I', 'II', 'III', 'IV', 'V', 'VI']

export default function Stepper({ steps, current }) {
  return (
    <nav className="stepper" role="navigation">
      {steps.map((name, i) => {
        const cls =
          i === current ? 'active' : i < current ? 'done' : ''
        return (
          <div key={name} className={`stepper-item ${cls}`}>
            <span className="num">{ROMANS[i]}</span>
            <span className="name">{name}</span>
          </div>
        )
      })}
    </nav>
  )
}
