import { NIVEIS } from '../labels'
import RadioRow from '../components/RadioRow'
import FooterNav from '../components/FooterNav'

/** Step 1 — student level (beginner, intermediate, advanced). */
export default function StepNivel({ state, set, onNext }) {
  const canAdvance = !!state.nivelAluno

  return (
    <div className="step">
      <div className="display">
        Em que <em>nível</em>
        <br />
        está o aluno?
      </div>
      <div className="subtitle">
        Indica o nível do aluno para definir a faixa de dificuldade das obras
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">nível</span>
          <span className="section-label">obrigatório</span>
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
      />
    </div>
  )
}
