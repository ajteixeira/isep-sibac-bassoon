import { COMP_GROUPS, COMP_BY_ID } from '../labels'
import CFRange from '../components/CFRange'
import FooterNav from '../components/FooterNav'

/** Step 3 — up to 3 priority skills, each with a certainty factor. */
export default function StepCompetencias({ state, set, onNext, onBack }) {
  const list = state.competencias || []
  const selectedIds = list.map((c) => c.id)

  const toggle = (id) => {
    const idx = selectedIds.indexOf(id)
    if (idx >= 0) {
      set({ competencias: list.filter((c) => c.id !== id) })
    } else if (list.length < 3) {
      set({ competencias: [...list, { id, cf: 0.75 }] })
    }
  }

  const updateCf = (id, cf) => {
    set({
      competencias: list.map((c) => (c.id === id ? { ...c, cf } : c)),
    })
  }

  const remove = (id) => {
    set({ competencias: list.filter((c) => c.id !== id) })
  }

  const canAdvance = list.length >= 1

  return (
    <div className="step">
      <div className="display">
        Que <em>competências</em>
        <br />
        queres trabalhar?
      </div>
      <div className="subtitle">
        Indica as competências a trabalhar - o sistema favorece obras que as desenvolvem
      </div>

      {/* Selected skills tray */}
      <div className="field">
        <div className="field-head">
          <span className="q">competências selecionadas</span>
          <span className="section-label">obrigatório · min. 1</span>
        </div>

      <div className="comp-tray">
        <div className="comp-tray-head">
          <span>
            selecionadas - <span className="count">{list.length}</span> / 3
          </span>
          {list.length === 3 && (
            <span className="hint-r">
              limite atingido - remove uma para escolher outra
            </span>
          )}
        </div>

        {list.length === 0 ? (
          <div className="comp-tray-empty">
            <em>Ainda nada escolhido.</em>
            Clica em pelo menos uma competência abaixo
          </div>
        ) : (
          list.map((item) => {
            const c = COMP_BY_ID[item.id]
            return (
              <div key={item.id} className="comp-row">
                <div className="cr-name">
                  <span className="cr-group">{c.groupTitle}</span>
                  {c.label}
                </div>
                <div className="cr-slider">
                  <CFRange
                    value={item.cf}
                    onChange={(v) => updateCf(item.id, v)}
                    leftLabel="pouco prioritario"
                    rightLabel="muito prioritario"
                  />
                </div>
                <button
                  type="button"
                  className="cr-remove"
                  onClick={() => remove(item.id)}
                  aria-label="remover competencia"
                  title="remover"
                >
                  &times;
                </button>
              </div>
            )
          })
        )}
      </div>
      </div>

      {/* Skill groups */}
      {COMP_GROUPS.map((g, gi) => (
        <div key={g.id} className="comp-group">
          <div className="comp-group-head">
            <span className="gn">{String(gi + 1).padStart(2, '0')} /</span>
            <span className="gt">{g.title}</span>
          </div>
          <div className="comp-chips">
            {g.items.map((item) => {
              const isSelected = selectedIds.includes(item.id)
              const isDisabled = !isSelected && list.length >= 3
              return (
                <button
                  key={item.id}
                  type="button"
                  className={`comp-chip ${isSelected ? 'selected' : ''}`}
                  onClick={() => toggle(item.id)}
                  disabled={isDisabled}
                >
                  {item.label}
                </button>
              )
            })}
          </div>
        </div>
      ))}

      <FooterNav
        onBack={onBack}
        onNext={onNext}
        nextDisabled={!canAdvance}
      />
    </div>
  )
}
