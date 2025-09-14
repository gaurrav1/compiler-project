#!/usr/bin/env sh
set -eu

# Render Nginx config from template with PORT
: "${PORT:=80}"
export PORT
envsubst '${PORT}' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

# Generate runtime config.js in served folder
: "${API_BASE_URL:=}"
: "${BACKEND_PORT:=}"

# Basic escaping for quotes and backslashes
escape() {
  printf '%s' "$1" | sed -e 's/\\/\\\\/g' -e 's/"/\\"/g'
}

API_ESC=$(escape "$API_BASE_URL")
PORT_ESC=$(escape "$BACKEND_PORT")

cat > /usr/share/nginx/html/config.js <<EOF
// Generated at container start
window.__ENV = Object.assign({}, window.__ENV || {}, {
  API_BASE_URL: "${API_ESC}",
  BACKEND_PORT: "${PORT_ESC}"
});
EOF

# Start Nginx in foreground
exec nginx -g 'daemon off;'
