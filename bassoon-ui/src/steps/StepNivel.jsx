import { NIVEIS } from '../data'
import RadioRow from '../components/RadioRow'
import CFRange from '../components/CFRange'
import DirectionPicker from '../components/DirectionPicker'
import FooterNav from '../components/FooterNav'

export default function StepNivel({ state, set, onNext }) {
  const isIntermedio = state.nivelAluno === 'INTERMEDIO'
  const hasDoubt = state.cfNivelAluno < 0.95
  const showDirection = isIntermedio && hasDoubt
  const canAdvance = !!state.nivelAluno

  const leftOption = NIVEIS.find((n) => n.id === 'INICIANTE')
  const rightOption = NIVEIS.find((n) => n.id === 'AVANCADO')

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
          onChange={(v) => {
            const patch = { nivelAluno: v }
            if (v !== 'INTERMEDIO') patch.nivelIncerteza = null
            set(patch)
          }}
        />
      </div>

      {state.nivelAluno && (
        <div className="field">
          <div className="field-head">
            <span className="q">ii. grau de certeza</span>
            <span className="section-label">obrigatorio</span>
          </div>
          <CFRange
            value={state.cfNivelAluno}
            onChange={(v) => {
              const patch = { cfNivelAluno: v }
              if (v >= 0.95) patch.nivelIncerteza = null
              set(patch)
            }}
          />

          {showDirection && (
            <>
              <span className="dir-caption">
                em que sentido pende a duvida?
              </span>
              <DirectionPicker
                value={state.nivelIncerteza}
                onChange={(v) => set({ nivelIncerteza: v })}
                leftOption={leftOption}
                rightOption={rightOption}
              />
            </>
          )}
        </div>
      )}

      <FooterNav
        onNext={onNext}
        nextDisabled={!canAdvance}
        meta="passo i de vi - nivel"
      />
    </div>
  )
}
