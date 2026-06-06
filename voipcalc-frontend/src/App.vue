<template>
  <div class="app">
    <header class="app-header">
      <div class="container">
        <div class="header-inner">
          <div class="brand">
            <svg class="brand-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07 19.5 19.5 0 01-6-6 19.79 19.79 0 01-3.07-8.67A2 2 0 014.11 2h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L8.09 9.91a16 16 0 006 6l1.27-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z"/>
            </svg>
            <span class="brand-name">VoIPCalc</span>
            <span class="brand-tag">Core Engine</span>
          </div>
          <nav class="nav">
            <span class="nav-hint">Rate Calculation API v1.0</span>
          </nav>
        </div>
      </div>
    </header>

    <main class="app-main">
      <RouterView />
    </main>

    <footer class="app-footer">
      <div class="container">
        <p class="footer-text">VoIPCalc-Core &mdash; Cross-border VOIP Rate Engine &bull; Built with Spring Boot + Vue</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router'
</script>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  border-bottom: 1px solid var(--color-border-light);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-icon {
  color: var(--color-text-primary);
}

.brand-name {
  font-size: 1rem;
  font-weight: 600;
  letter-spacing: -0.03em;
  color: var(--color-text-primary);
}

.brand-tag {
  font-size: 0.6875rem;
  font-weight: 500;
  color: var(--color-text-tertiary);
  background: var(--color-surface);
  padding: 3px 8px;
  border-radius: 4px;
  letter-spacing: 0.02em;
}

.nav-hint {
  font-size: 0.8125rem;
  color: var(--color-text-tertiary);
  font-family: var(--font-mono);
}

.app-main {
  flex: 1;
  padding: 64px 0;
}

.app-footer {
  padding: 32px 0;
  border-top: 1px solid var(--color-border-light);
}

.footer-text {
  font-size: 0.8125rem;
  color: var(--color-text-tertiary);
  text-align: center;
}
</style>
