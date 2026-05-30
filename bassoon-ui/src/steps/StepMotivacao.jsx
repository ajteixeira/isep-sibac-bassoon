import { MOTIVACOES } from '../labels'
import RadioRow from '../components/RadioRow'
import FooterNav from '../components/FooterNav'

/** Step 2 — student motivation (shifts difficulty range). */
export default function StepMotivacao({ state, set, onNext, onBack }) {
  const canAdvance = !!state.motivacao

  return (
    <div className="step">
      <div className="display">
        Como está a <em>motivação</em>
        <br />
        do aluno?
      </div>
      <div className="subtitle">
        Indica a motivação do aluno - alta sobe a fasquia, baixa desce
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">motivação</span>
          <span className="section-label">obrigatório</span>
        </div>
        <RadioRow
          options={MOTIVACOES}
          value={state.motivacao}
          onChange={(v) => set({ motivacao: v })}
        />
      </div>

      <FooterNav
        onBack={onBack}
        onNext={onNext}
        nextDisabled={!canAdvance}
      />
    </div>
  )
}
