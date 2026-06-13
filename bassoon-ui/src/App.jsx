import { useState } from 'react'
import { recommend } from './services/RecommendationService'
import Stepper from './components/Stepper'
import StepIntro from './steps/StepIntro'
import StepNivel from './steps/StepNivel'
import StepCompetencias from './steps/StepCompetencias'
import StepMotivacao from './steps/StepMotivacao'
import StepAcompanhamento from './steps/StepAcompanhamento'
import StepPeriodo from './steps/StepPeriodo'
import StepResultados from './steps/StepResultados'

/**
 * Root component. Manages the wizard state (current step, form data, results)
 * and delegates rendering to the individual step components.
 */

const INITIAL_STATE = {
  nivelAluno: null,
  competencias: [],
  motivacao: null,
  acompanhamentos: [],
  ultimoPeriodo: null,
}

const STEP_NAMES = [
  'Nível',
  'Motivação',
  'Competências',
  'Acompanhamento',
  'Período',
  'Recomendação',
]

export default function App() {
  const [hasStarted, setHasStarted] = useState(false)
  const [step, setStep] = useState(0)
  const [state, setState] = useState(INITIAL_STATE)
  const [results, setResults] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const set = (patch) => setState((s) => ({ ...s, ...patch }))

  const submit = async () => {
    setLoading(true)
    setError(null)
    setStep(5)
    try {
      const data = await recommend(state)
      setResults(data)
    } catch (e) {
      setError({
        title: 'Erro de comunicação',
        message:
          e.message ||
          'Não foi possível contactar o sistema de recomendação. Tenta novamente daqui a pouco.',
      })
    } finally {
      setLoading(false)
    }
  }

  const restart = () => {
    setHasStarted(false)
    setStep(0)
    setState(INITIAL_STATE)
    setResults(null)
    setError(null)
  }

  const renderStep = () => {
    if (!hasStarted) {
      return (
        <StepIntro onStart={() => { setHasStarted(true); setStep(0) }} />
      )
    }
    switch (step) {
      case 0:
        return <StepNivel state={state} set={set} onNext={() => setStep(1)} />
      case 1:
        return (
          <StepMotivacao
            state={state} set={set}
            onBack={() => setStep(0)} onNext={() => setStep(2)}
          />
        )
      case 2:
        return (
          <StepCompetencias
            state={state} set={set}
            onBack={() => setStep(1)} onNext={() => setStep(3)}
          />
        )
      case 3:
        return (
          <StepAcompanhamento
            state={state} set={set}
            onBack={() => setStep(2)} onNext={() => setStep(4)}
          />
        )
      case 4:
        return (
          <StepPeriodo
            state={state} set={set}
            onBack={() => setStep(3)} onSubmit={submit}
          />
        )
      case 5:
        return (
          <StepResultados
            state={state} results={results}
            loading={loading} error={error}
            onBack={() => setStep(4)} onRestart={restart}
          />
        )
      default:
        return null
    }
  }

  return (
    <div className="app-layout">
      <aside className="app-sidebar">
        <img
          src="/image_bassoon.png"
          alt=""
        />
      </aside>
      <div className="app-shell">
        <header className="app-header">
          <div className="brand">
            Bassoon
            <span className="sub">sistema pericial - repertorio de fagote</span>
          </div>
          <div className="app-header-right">sibac - 2026</div>
        </header>
        {hasStarted && <Stepper steps={STEP_NAMES} current={step} />}
        <main className="app-body">
          <div className="step-wrap">
            {renderStep()}
          </div>
        </main>
      </div>
    </div>
  )
}
