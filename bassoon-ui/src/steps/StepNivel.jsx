import LevelSlider from '../components/LevelSlider'
import FooterNav from '../components/FooterNav'

/** Step 1 — student level on a continuous scale (iniciante → intermédio → avançado). */
export default function StepNivel({ state, set, onNext }) {
  const canAdvance = state.nivelAluno != null

  return (
    <div className="step">
      <div className="display">
        Em que <em>nível</em>
        <br />
        está o aluno?
      </div>
      <div className="subtitle">
        Arrasta para situar o aluno entre iniciante e avançado
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">nível</span>
          <span className="section-label">obrigatório</span>
        </div>
        <LevelSlider
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
