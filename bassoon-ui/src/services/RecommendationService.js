// ---------------------------------------------------------------------------
// RecommendationService
// Comunicacao com o bassoon-api (Spring Boot em localhost:8080).
// O proxy do Vite redireciona /recommend para o backend em dev.
//
// USE_MOCK = true  -> devolve dados mock (para desenvolvimento sem backend)
// USE_MOCK = false -> chama a API real
// ---------------------------------------------------------------------------

import { MOCK_RESPONSE } from './mockData'

const API_BASE = '/recommend'
const USE_MOCK = true

/**
 * Converte o estado do wizard no DTO esperado pelo RecommendationController.
 */
function buildRequest(state) {
  return {
    nivelAluno: state.nivelAluno,
    cfNivelAluno: state.cfNivelAluno,
    nivelIncerteza: state.nivelIncerteza || null,
    competencias: state.competencias.map((c) => ({
      competencia: c.id,
      cf: c.cf,
    })),
    motivacao: state.motivacao || null,
    cfMotivacao: state.motivacao ? state.cfMotivacao : null,
    motivacaoIncerteza: state.motivacaoIncerteza || null,
    ultimoPeriodo: state.ultimoPeriodo || null,
  }
}

/**
 * Envia o pedido de recomendacao ao motor pericial.
 * Retorna o RecommendationResponse (recomendacoes + justificacao).
 */
export async function recommend(state) {
  if (USE_MOCK) {
    // Simula latencia de rede
    await new Promise((r) => setTimeout(r, 1200))
    return MOCK_RESPONSE
  }

  const body = buildRequest(state)

  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })

  if (!res.ok) {
    const text = await res.text().catch(() => '')
    throw new Error(text || `HTTP ${res.status}`)
  }

  return res.json()
}
