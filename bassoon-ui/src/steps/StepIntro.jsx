/** Landing screen with a brief explanation and a start button. */
export default function StepIntro({ onStart }) {
  return (
    <div className="step step-intro">
      <div className="intro-eyebrow">recomendação de repertório</div>
      <div className="display">
        Que obra
        <br />
        <em>estudar a seguir?</em>
      </div>
      <div className="subtitle">
        Descreve o aluno e o sistema cruza esse perfil com o conhecimento
        do perito para sugerir as obras mais adequadas.
      </div>
      <div className="intro-actions">
        <button
          type="button"
          className="btn btn-primary intro-cta"
          onClick={onStart}
        >
          Começar &rarr;
        </button>
      </div>
    </div>
  )
}
