# ===========================================================================
# bassoon-ui — multi-stage build
# Stage 1: npm build (gera HTML/CSS/JS estaticos)
# Stage 2: serve com nginx (inclui proxy reverso para a API)
# ===========================================================================

# --- Build ---
FROM node:20-alpine AS build

WORKDIR /src

COPY package.json package-lock.json* ./
RUN npm ci --silent

COPY . .
RUN npm run build

# --- Runtime ---
FROM nginx:stable-alpine

# Configuracao nginx com proxy reverso para a API
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar ficheiros estaticos
COPY --from=build /src/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=15s --timeout=3s --retries=3 \
    CMD wget -qO- http://localhost:80/ || exit 1
