import { ACOMPANHAMENTOS } from '../labels'
import CFRange from '../components/CFRange'
import FooterNav from '../components/FooterNav'

/** Step 4 — preferred accompaniment types (optional, multi-select). */
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
        Indica o acompanhamento preferido - obras com esse acompanhamento ganham prioridade
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">acompanhamento preferido</span>
          <span className="section-label">opcional · multipla escolha</span>
        </div>

        {/* Selected items tray */}
        <div className="comp-tray">
          <div className="comp-tray-head">
            <span>
              selecionados - <span className="count">{selected.length}</span>
            </span>
          </div>

          {selected.length === 0 ? (
            <div className="comp-tray-empty">
              <em>Nada selecionado.</em>
              Clica em um ou vários tipos de acompanhamento abaixo
            </div>
          ) : (
            selected.map((a) => {
              const info = ACOMPANHAMENTOS.find((x) => x.id === a.id)
              return (
                <div key={a.id} className="comp-row">
                  <div className="cr-name">{info?.label}</div>
                  <div className="cr-slider">
                    <CFRange
                      value={a.cf}
                      onChange={(v) => setCf(a.id, v)}
                    />
                  </div>
                  <button
                    type="button"
                    className="cr-remove"
                    onClick={() => toggle(a.id)}
                    aria-label="remover acompanhamento"
                    title="remover"
                  >
                    &times;
                  </button>
                </div>
              )
            })
          )}
        </div>

        {/* Available options */}
        <div className="comp-group">
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
      </div>

      <FooterNav
        onBack={onBack}
        onNext={onNext}
      />
    </div>
  )
}
