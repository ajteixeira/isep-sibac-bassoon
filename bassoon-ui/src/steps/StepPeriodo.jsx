import { EPOCAS } from '../labels'
import RadioRow from '../components/RadioRow'
import FooterNav from '../components/FooterNav'

/** Step 5 — last studied era (optional, used to avoid repetition). */
export default function StepPeriodo({ state, set, onSubmit, onBack }) {
  return (
    <div className="step">
      <div className="display">
        Qual foi o último
        <br />
        <em>período</em> estudado?
      </div>
      <div className="subtitle">
        Indica o último período estudado - obras da mesma epoca perdem prioridade
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">período estilístico</span>
          <span className="section-label">opcional</span>
        </div>
        <RadioRow
          options={EPOCAS}
          value={state.ultimoPeriodo}
          onChange={(v) =>
            set({ ultimoPeriodo: state.ultimoPeriodo === v ? null : v })
          }
          columns={5}
        />
      </div>

      <FooterNav
        onBack={onBack}
        onNext={onSubmit}
        nextLabel="recomendar repertório"
        nextEmphatic
      />
    </div>
  )
}
