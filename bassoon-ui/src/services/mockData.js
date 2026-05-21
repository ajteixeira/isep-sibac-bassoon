// ---------------------------------------------------------------------------
// Dados mock - simulam a resposta do RecommendationResponse do bassoon-api.
// Usados enquanto o backend nao esta a correr.
// Para desativar: mudar USE_MOCK para false no RecommendationService.
// ---------------------------------------------------------------------------

export const MOCK_RESPONSE = {
  recomendacoes: [
    {
      nomeObra: 'Concerto em Fa, J. 127',
      compositor: 'C. M. von Weber',
      epoca: 'ROMANTICO',
      pais: 'Alemanha',
      acompanhamento: 'ORQUESTRA',
      dificuldade: 5,
      score: 0.731,
      preRequisito: 'Sonata in B♭',
      youtubeId: 'G4k2wWjV6gY',
      comentario:
        'Sobe a fasquia ao colocar o aluno em dialogo com orquestra. A motivacao alta justifica a passagem para a faixa superior de dificuldade, mas atencao: o sistema sinaliza dependencia pedagogica da Sonata Wq. 132.',
      regrasFired: [
        { id: 'R1.2', cf: 0.72, desc: 'Nivel intermedio -> faixa de dificuldade 3-4' },
        { id: 'R2', cf: 0.6, desc: 'Motivacao alta -> desloca faixa para cima' },
        { id: 'R4.ALTA', cf: 0.7, desc: 'Trabalha fortemente LEGATO' },
        { id: 'R4.MEDIA', cf: 0.3, desc: 'Contribui para AFINACAO' },
      ],
    },
    {
      nomeObra: 'Hopi',
      compositor: 'P. Hersant',
      epoca: 'CONTEMPORANEO',
      pais: 'Franca',
      acompanhamento: 'SOLO',
      dificuldade: 6,
      score: 0.612,
      preRequisito: 'Niggun',
      youtubeId: 'HyGI35dG3qE',
      comentario:
        'Introduz o desafio do staccato contemporaneo. Foi penalizada por partilhar periodo com a ultima obra estudada, mas a sua relevancia tecnica para staccato manteve-a entre as escolhas viaveis.',
      regrasFired: [
        { id: 'R1.2', cf: 0.72, desc: 'Nivel intermedio -> faixa de dificuldade 3-4' },
        { id: 'R2', cf: 0.6, desc: 'Motivacao alta -> desloca faixa para cima' },
        { id: 'R4.ALTA', cf: 0.7, desc: 'Trabalha fortemente STACCATO' },
        { id: 'R6', cf: -0.75, desc: 'Penalizacao: periodo diferente do ultimo estudado' },
      ],
    },
    {
      nomeObra: 'Sonate, Op. 168',
      compositor: 'C. Saint-Saens',
      epoca: 'ROMANTICO',
      pais: 'Franca',
      acompanhamento: 'PIANO',
      dificuldade: 4,
      score: 0.584,
      preRequisito: null,
      youtubeId: 'JLRWLkugiXQ',
      comentario:
        'Alternativa romantica acessivel, sem pre-requisitos, com piano. Trabalha o legato com um vocabulario lirico distinto do das obras alemas.',
      regrasFired: [
        { id: 'R1.2', cf: 0.72, desc: 'Nivel intermedio -> faixa de dificuldade 3-4' },
        { id: 'R4.ALTA', cf: 0.7, desc: 'Trabalha fortemente LEGATO' },
        { id: 'R4.MEDIA', cf: 0.3, desc: 'Contribui para AFINACAO' },
      ],
    },
    {
      nomeObra: 'Sonata in F minor, TWV 41:f1',
      compositor: 'G. P. Telemann',
      epoca: 'BARROCO',
      pais: 'Alemanha',
      acompanhamento: 'BAIXO_CONTINUO',
      dificuldade: 3,
      score: 0.521,
      preRequisito: null,
      youtubeId: 'afudKfX_4Ys',
      comentario:
        'Opcao barroca com baixo continuo, na faixa baixa do nivel intermedio. Util se o aluno precisar de consolidar o staccato num enquadramento estilistico mais transparente.',
      regrasFired: [
        { id: 'R1.2', cf: 0.72, desc: 'Nivel intermedio -> faixa de dificuldade 3-4' },
        { id: 'R4.ALTA', cf: 0.7, desc: 'Trabalha fortemente STACCATO' },
        { id: 'R4.MEDIA', cf: 0.3, desc: 'Contribui para LEGATO' },
      ],
    },
    {
      nomeObra: 'Concerto in B♭, K. 191',
      compositor: 'W. A. Mozart',
      epoca: 'CLASSICO',
      pais: 'Austria',
      acompanhamento: 'ORQUESTRA',
      dificuldade: 5,
      score: 0.487,
      preRequisito: null,
      youtubeId: 'QfhxZMUy9DU',
      comentario:
        'Pilar do repertorio classico para fagote. Exige maturidade no legato e nas dinamicas, e a faixa de dificuldade encosta ao limite do nivel.',
      regrasFired: [
        { id: 'R1.2', cf: 0.72, desc: 'Nivel intermedio -> faixa de dificuldade 3-4' },
        { id: 'R2', cf: 0.6, desc: 'Motivacao alta -> desloca faixa para cima' },
        { id: 'R4.REFERENCIA', cf: 1.0, desc: 'Obra de referencia para LEGATO' },
        { id: 'R4.MEDIA', cf: 0.3, desc: 'Contribui para DINAMICAS' },
      ],
    },
    {
      nomeObra: 'Concerto in A minor, RV 497',
      compositor: 'A. Vivaldi',
      epoca: 'BARROCO',
      pais: 'Italia',
      acompanhamento: 'BAIXO_CONTINUO',
      dificuldade: 3,
      score: 0.432,
      preRequisito: null,
      youtubeId: 'AQ3wCBGsSYo',
      comentario:
        'Material barroco vivo, com passagens em staccato e ornamentacao idiomatica. Pode servir como exercicio preparatorio para escrita virtuosistica do barroco italiano.',
      regrasFired: [
        { id: 'R1.2', cf: 0.72, desc: 'Nivel intermedio -> faixa de dificuldade 3-4' },
        { id: 'R4.ALTA', cf: 0.7, desc: 'Trabalha fortemente STACCATO' },
        { id: 'R4.MEDIA', cf: 0.3, desc: 'Contribui para ORNAMENTACAO' },
      ],
    },
  ],
}
