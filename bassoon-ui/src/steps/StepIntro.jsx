export default function StepIntro({ onStart }) {
  return (
    <div className="step step-intro">
      <div className="intro-eyebrow">recomendacao de repertorio</div>
      <div className="display">
        Vamos <em>recomendar</em>
        <br />
        uma obra.
      </div>
      <div className="subtitle">
        Quatro perguntas rapidas sobre o aluno - o sistema usa a base de
        conhecimento do perito para sugerir obras alinhadas com nivel,
        competencias e contexto.
      </div>
      <div className="intro-actions">
        <button
          type="button"
          className="btn btn-primary intro-cta"
          onClick={onStart}
        >
          Comecar &rarr;
        </button>
        <span className="intro-meta">
          dura cerca de 1 minuto - podes saltar perguntas opcionais
        </span>
      </div>
    </div>
  )
}
