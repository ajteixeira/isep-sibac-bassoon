// ---------------------------------------------------------------------------
// Mock data — simulates the RecommendationResponse from bassoon-api.
// Used when the backend is not running.
// To enable: set USE_MOCK = true in RecommendationService.
// ---------------------------------------------------------------------------

export const MOCK_RESPONSE = {
  recommendations: [
    {
      workName: 'Concerto em Fa, J. 127',
      composer: 'C. M. von Weber',
      era: 'ROMANTIC',
      country: 'Alemanha',
      accompaniment: 'ORCHESTRA',
      difficulty: 5,
      score: 0.731,
      prerequisite: 'Sonata in B♭',
      videoLink: 'G4k2wWjV6gY',
      justification:
        'Sobe a fasquia ao colocar o aluno em dialogo com orquestra. A motivacao alta justifica a passagem para a faixa superior de dificuldade, mas atencao: o sistema sinaliza dependencia pedagogica da Sonata Wq. 132.',
      firedRules: [
        { name: 'skill REFERENCE', cf: 0.85, category: 'SKILL', detail: 'LEGATO' },
        { name: 'skill VERY_SUITABLE', cf: 0.7, category: 'SKILL', detail: 'STACCATO' },
        { name: 'skill MODERATE', cf: 0.3, category: 'SKILL', detail: 'LOW_REGISTER' },
        { name: 'accompaniment match', cf: 0.3, category: 'ACCOMPANIMENT', detail: 'ORCHESTRA' },
        { name: 'era penalty', cf: -0.3, category: 'ERA', detail: 'ROMANTIC' },
      ],
    },
    {
      workName: 'Hopi',
      composer: 'P. Hersant',
      era: 'CONTEMPORARY',
      country: 'Franca',
      accompaniment: 'SOLO',
      difficulty: 6,
      score: 0.612,
      prerequisite: 'Niggun',
      videoLink: 'HyGI35dG3qE',
      justification:
        'Introduz o desafio do staccato contemporaneo. Foi penalizada por partilhar periodo com a ultima obra estudada, mas a sua relevancia tecnica para staccato manteve-a entre as escolhas viaveis.',
      firedRules: [
        { name: 'skill VERY_SUITABLE', cf: 0.7, category: 'SKILL', detail: 'STACCATO' },
        { name: 'skill MODERATE', cf: 0.3, category: 'SKILL', detail: 'DYNAMICS' },
      ],
    },
    {
      workName: 'Sonate, Op. 168',
      composer: 'C. Saint-Saens',
      era: 'ROMANTIC',
      country: 'Franca',
      accompaniment: 'PIANO',
      difficulty: 4,
      score: 0.584,
      prerequisite: null,
      videoLink: 'JLRWLkugiXQ',
      justification:
        'Alternativa romantica acessivel, sem pre-requisitos, com piano. Trabalha o legato com um vocabulario lirico distinto do das obras alemas.',
      firedRules: [
        { name: 'skill VERY_SUITABLE', cf: 0.7, category: 'SKILL', detail: 'LEGATO' },
        { name: 'skill MODERATE', cf: 0.3, category: 'SKILL', detail: 'INTONATION' },
        { name: 'accompaniment match', cf: 0.3, category: 'ACCOMPANIMENT', detail: 'PIANO' },
      ],
    },
    {
      workName: 'Sonata in F minor, TWV 41:f1',
      composer: 'G. P. Telemann',
      era: 'BAROQUE',
      country: 'Alemanha',
      accompaniment: 'BASSO_CONTINUO',
      difficulty: 3,
      score: 0.521,
      prerequisite: null,
      videoLink: 'afudKfX_4Ys',
      justification:
        'Opcao barroca com baixo continuo, na faixa baixa do nivel intermedio. Util se o aluno precisar de consolidar o staccato num enquadramento estilistico mais transparente.',
      firedRules: [
        { name: 'skill VERY_SUITABLE', cf: 0.7, category: 'SKILL', detail: 'STACCATO' },
        { name: 'skill MODERATE', cf: 0.3, category: 'SKILL', detail: 'LEGATO' },
      ],
    },
    {
      workName: 'Concerto in B♭, K. 191',
      composer: 'W. A. Mozart',
      era: 'CLASSICAL',
      country: 'Austria',
      accompaniment: 'ORCHESTRA',
      difficulty: 5,
      score: 0.487,
      prerequisite: null,
      videoLink: 'QfhxZMUy9DU',
      justification:
        'Pilar do repertorio classico para fagote. Exige maturidade no legato e nas dinamicas, e a faixa de dificuldade encosta ao limite do nivel.',
      firedRules: [
        { name: 'skill REFERENCE', cf: 0.85, category: 'SKILL', detail: 'LEGATO' },
        { name: 'skill MODERATE', cf: 0.3, category: 'SKILL', detail: 'DYNAMICS' },
      ],
    },
    {
      workName: 'Concerto in A minor, RV 497',
      composer: 'A. Vivaldi',
      era: 'BAROQUE',
      country: 'Italia',
      accompaniment: 'BASSO_CONTINUO',
      difficulty: 3,
      score: 0.432,
      prerequisite: null,
      videoLink: 'AQ3wCBGsSYo',
      justification:
        'Material barroco vivo, com passagens em staccato e ornamentacao idiomatica. Pode servir como exercicio preparatorio para escrita virtuosistica do barroco italiano.',
      firedRules: [
        { name: 'skill VERY_SUITABLE', cf: 0.7, category: 'SKILL', detail: 'STACCATO' },
        { name: 'skill MODERATE', cf: 0.3, category: 'SKILL', detail: 'ORNAMENTATION' },
        { name: 'era penalty', cf: -0.3, category: 'ERA', detail: 'BAROQUE' },
      ],
    },
  ],
}
