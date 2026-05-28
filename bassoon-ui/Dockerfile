# ===========================================================================
# bassoon-ui — multi-stage build
# Stage 1: npm build (static HTML/CSS/JS)
# Stage 2: serve with nginx (reverse proxy to the API)
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

# nginx config with reverse proxy to the API
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy static files
COPY --from=build /src/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=15s --timeout=3s --retries=3 \
    CMD wget -qO- http://localhost:80/ || exit 1
