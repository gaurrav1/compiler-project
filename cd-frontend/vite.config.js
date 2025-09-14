/* eslint-env node */
import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { cwd } from 'node:process'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, cwd(), '')
  const port = Number(env.VITE_PORT || env.PORT || 5500)

  return {
    plugins: [react(), tailwindcss()],
    server: {
      open: true,
      port,
      host: true,
    },
  }
})
