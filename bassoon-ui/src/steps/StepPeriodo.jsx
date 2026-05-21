import { EPOCAS } from '../data'
import RadioRow from '../components/RadioRow'
import FooterNav from '../components/FooterNav'

export default function StepPeriodo({ state, set, onSubmit, onBack }) {
  return (
    <div className="step">
      <div className="display">
        Qual foi o <em>ultimo</em>
        <br />
        periodo estudado?
      </div>
      <div className="subtitle">
        opcional - o sistema usa esta informacao para evitar repetir o mesmo
        estilo no proximo repertorio
      </div>

      <div className="skip-row">
        <span className="sk-text">
          {state.ultimoPeriodo
            ? `Selecionado: ${EPOCAS.find((e) => e.id === state.ultimoPeriodo)?.label}`
            : 'Sem indicacao - o sistema nao vai penalizar nenhum periodo'}
        </span>
        {state.ultimoPeriodo && (
          <button
            type="button"
            className="sk-btn"
            onClick={() => set({ ultimoPeriodo: null })}
          >
            Limpar selecao
          </button>
        )}
      </div>

      <div className="field">
        <div className="field-head">
          <span className="q">periodo estilistico</span>
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
        nextLabel="&#8627; recomendar repertorio"
        nextEmphatic
        meta="passo v de vi - periodo"
      />
    </div>
  )
}
