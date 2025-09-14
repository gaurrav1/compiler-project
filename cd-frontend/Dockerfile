# Stage 1: Build
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine
# Install envsubst for templating configs
RUN apk add --no-cache gettext

# Static assets
COPY --from=builder /app/dist /usr/share/nginx/html

# Nginx config template and entrypoint
COPY --from=builder /app/nginx.conf.template /etc/nginx/nginx.conf.template
COPY --from=builder /app/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# Runtime envs
ENV PORT=80

EXPOSE 80

ENTRYPOINT ["/docker-entrypoint.sh"]
