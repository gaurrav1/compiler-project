// Runtime environment config injected at container start. Safe defaults for local dev.
window.__ENV = Object.assign({}, window.__ENV || {}, {
  // If empty, app falls back to window.location + BACKEND_PORT
  API_BASE_URL: '',
  BACKEND_PORT: ''
});

