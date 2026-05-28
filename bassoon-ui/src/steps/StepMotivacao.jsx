import { MOTIVACOES } from '../labels'
import RadioRow from '../components/RadioRow'
import FooterNav from '../components/FooterNav'

/** Step 3 — student motivation (optional, shifts difficulty range). */
export default function StepMotivacao({ state, set, onNext, onBack }) {
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
            onClick={() => set({ motivacao: null })}
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
          onChange={(v) =>
            set({ motivacao: state.motivacao === v ? null : v })
          }
        />
      </div>

      <FooterNav
        onBack={onBack}
        onNext={onNext}
        meta="passo iii de vi - motivacao"
      />
    </div>
  )
}
