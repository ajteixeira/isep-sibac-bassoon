// ---------------------------------------------------------------------------
// RecommendationService
// Calls the bassoon-api REST endpoint (Spring Boot, localhost:8080).
// In dev, Vite proxies /recommend to the backend.
//
// USE_MOCK = true  -> returns mock data (dev without backend)
// USE_MOCK = false -> calls the real API
// ---------------------------------------------------------------------------

import { MOCK_RESPONSE } from './mockData'

const API_BASE = '/recommend'
const USE_MOCK = false

function buildRequest(state) {
  return {
    studentLevel: state.nivelAluno,
    skills: state.competencias.map((c) => ({
      skill: c.id,
      cf: c.cf,
    })),
    motivation: state.motivacao || null,
    accompaniments: (state.acompanhamentos || []).map((a) => ({
      type: a.id,
      cf: a.cf,
    })),
    lastEra: state.ultimoPeriodo || null,
  }
}

export async function recommend(state) {
  if (USE_MOCK) {
    await new Promise((r) => setTimeout(r, 1200))
    return MOCK_RESPONSE
  }

  const body = buildRequest(state)

  let res
  try {
    res = await fetch(API_BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
  } catch {
    throw new Error(
      'Não foi possível contactar o sistema de recomendação. Tenta novamente daqui a pouco.'
    )
  }

  if (!res.ok) {
    throw new Error(
      'O sistema encontrou um problema ao processar o pedido. Tenta novamente.'
    )
  }

  return res.json()
}
