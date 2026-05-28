import { NIVEIS } from '../labels'
import RadioRow from '../components/RadioRow'
import FooterNav from '../components/FooterNav'

/** Step 1 — student level (beginner, intermediate, advanced). */
export default function StepNivel({ state, set, onNext }) {
  const canAdvance = !!state.nivelAluno

  return (
    <div className="step">
      <div className="display">
        Em que nivel
        <br />
        esta <em>o aluno</em>?
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">i. nivel</span>
          <span className="section-label">obrigatorio</span>
        </div>
        <RadioRow
          options={NIVEIS}
          value={state.nivelAluno}
          onChange={(v) => set({ nivelAluno: v })}
        />
      </div>

      <FooterNav
        onNext={onNext}
        nextDisabled={!canAdvance}
        meta="passo i de vi - nivel"
      />
    </div>
  )
}
