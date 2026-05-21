import { MOTIVACOES } from '../data'
import RadioRow from '../components/RadioRow'
import CFRange from '../components/CFRange'
import DirectionPicker from '../components/DirectionPicker'
import FooterNav from '../components/FooterNav'

export default function StepMotivacao({ state, set, onNext, onBack }) {
  const isNeutra = state.motivacao === 'NEUTRA'
  const hasDoubt = state.cfMotivacao < 0.95
  const showDirection = isNeutra && hasDoubt

  const leftOption = MOTIVACOES.find((m) => m.id === 'BAIXA')
  const rightOption = MOTIVACOES.find((m) => m.id === 'ALTA')

  return (
    <div className="step">
      <div className="display">
        Como esta <em>a motivacao</em>
        <br />
        do aluno?
      </div>
      <div className="subtitle">opcional - ajusta a faixa de dificuldade</div>

      <div className="skip-row">
        <span className="sk-text">
          {state.motivacao
            ? `Selecionado: ${MOTIVACOES.find((m) => m.id === state.motivacao)?.label}`
            : 'Sem indicacao - a motivacao nao vai ser considerada'}
        </span>
        {state.motivacao && (
          <button
            type="button"
            className="sk-btn"
            onClick={() => set({ motivacao: null, motivacaoIncerteza: null })}
          >
            Limpar selecao
          </button>
        )}
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">i. nivel de motivacao</span>
          <span className="section-label">opcional</span>
        </div>
        <RadioRow
          options={MOTIVACOES}
          value={state.motivacao}
          onChange={(v) => {
            const patch = { motivacao: v }
            if (v !== 'NEUTRA') patch.motivacaoIncerteza = null
            set(patch)
          }}
        />
      </div>

      {state.motivacao && (
        <div className="field">
          <div className="field-head">
            <span className="q">ii. grau de certeza</span>
            <span className="section-label">opcional</span>
          </div>
          <CFRange
            value={state.cfMotivacao}
            onChange={(v) => {
              const patch = { cfMotivacao: v }
              if (v >= 0.95) patch.motivacaoIncerteza = null
              set(patch)
            }}
          />

          {showDirection && (
            <>
              <span className="dir-caption">
                em que sentido pende a duvida?
              </span>
              <DirectionPicker
                value={state.motivacaoIncerteza}
                onChange={(v) => set({ motivacaoIncerteza: v })}
                leftOption={leftOption}
                rightOption={rightOption}
              />
            </>
          )}
        </div>
      )}

      <FooterNav
        onBack={onBack}
        onNext={onNext}
        meta="passo iii de vi - motivacao"
      />
    </div>
  )
}
