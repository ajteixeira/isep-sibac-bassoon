import { ACOMPANHAMENTOS } from '../data'
import CFRange from '../components/CFRange'
import FooterNav from '../components/FooterNav'

export default function StepAcompanhamento({ state, set, onNext, onBack }) {
  const selected = state.acompanhamentos || []

  const isSelected = (id) => selected.some((a) => a.id === id)

  const toggle = (id) => {
    if (isSelected(id)) {
      set({ acompanhamentos: selected.filter((a) => a.id !== id) })
    } else {
      set({ acompanhamentos: [...selected, { id, cf: 0.75 }] })
    }
  }

  const setCf = (id, cf) => {
    set({ acompanhamentos: selected.map((a) => (a.id === id ? { ...a, cf } : a)) })
  }

  return (
    <div className="step">
      <div className="display">
        Qual o <em>acompanhamento</em>
        <br />
        preferencial?
      </div>
      <div className="subtitle">
        opcional - influencia a selecao de obras por tipo de acompanhamento
      </div>

      <div className="skip-row">
        <span className="sk-text">
          {selected.length > 0
            ? `${selected.length} ${selected.length === 1 ? 'opcao selecionada' : 'opcoes selecionadas'}`
            : 'Sem indicacao - o acompanhamento nao vai ser considerado'}
        </span>
        {selected.length > 0 && (
          <button
            type="button"
            className="sk-btn"
            onClick={() => set({ acompanhamentos: [] })}
          >
            Limpar selecao
          </button>
        )}
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">i. tipo de acompanhamento</span>
          <span className="section-label">opcional · multipla escolha</span>
        </div>
        <div className="comp-chips">
          {ACOMPANHAMENTOS.map((a) => (
            <button
              key={a.id}
              type="button"
              className={`comp-chip ${isSelected(a.id) ? 'selected' : ''}`}
              onClick={() => toggle(a.id)}
            >
              {a.label}
            </button>
          ))}
        </div>
      </div>

      {selected.length > 0 && (
        <div className="field">
          <div className="field-head">
            <span className="q">ii. grau de certeza por opcao</span>
            <span className="section-label">opcional</span>
          </div>
          <div className="acomp-tray">
            {selected.map((a) => {
              const info = ACOMPANHAMENTOS.find((x) => x.id === a.id)
              return (
                <div key={a.id} className="acomp-row">
                  <span className="acomp-label">{info?.label}</span>
                  <CFRange value={a.cf} onChange={(v) => setCf(a.id, v)} />
                </div>
              )
            })}
          </div>
        </div>
      )}

      <FooterNav
        onBack={onBack}
        onNext={onNext}
        meta="passo iv de vi - acompanhamento"
      />
    </div>
  )
}
