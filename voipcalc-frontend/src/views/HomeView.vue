<template>
  <div class="home">
    <div class="container">

      <!-- Page Title -->
      <div class="page-header animate-fade-in-up">
        <h1 class="page-title">{{ $t('pageTitle') }}</h1>
        <p class="page-subtitle">{{ $t('pageSubtitle') }}</p>
      </div>

      <!-- Calculator Form -->
      <div class="calculator-grid">

        <!-- Input Panel -->
        <section class="panel input-panel animate-fade-in-up" style="animation-delay: 80ms">
          <div class="panel-header">
            <h2 class="panel-title">{{ $t('panelTitle') }}</h2>
            <p class="panel-desc">{{ $t('panelDesc') }}</p>
          </div>

          <form class="form" @submit.prevent="handleCalculate">

            <!-- Caller Number -->
            <div class="form-group">
              <label class="form-label" for="callerNumber">
                <span class="label-icon">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
                    <circle cx="12" cy="7" r="4"/>
                  </svg>
                </span>
                {{ $t('callerNumber') }}
              </label>
              <input
                id="callerNumber"
                v-model="form.callerNumber"
                type="text"
                class="form-input"
                placeholder="+8613812345678"
                autocomplete="off"
              />
              <span class="form-hint">{{ $t('callerNumberHint') }}</span>
            </div>

            <!-- Callee Number -->
            <div class="form-group">
              <label class="form-label" for="calleeNumber">
                <span class="label-icon">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07 19.5 19.5 0 01-6-6 19.79 19.79 0 01-3.07-8.67A2 2 0 014.11 2h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L8.09 9.91a16 16 0 006 6l1.27-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z"/>
                  </svg>
                </span>
                {{ $t('calleeNumber') }}
              </label>
              <input
                id="calleeNumber"
                v-model="form.calleeNumber"
                type="text"
                class="form-input"
                placeholder="+14155551234"
                autocomplete="off"
              />
              <span class="form-hint">{{ $t('calleeNumberHint') }}</span>
            </div>

            <!-- Customer Type -->
            <div class="form-group">
              <label class="form-label" for="customerType">
                <span class="label-icon">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/>
                    <circle cx="9" cy="7" r="4"/>
                    <path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/>
                  </svg>
                </span>
                {{ $t('customerType') }}
              </label>
              <div class="radio-group">
                <label class="radio-option" :class="{ active: form.customerType === 'NORMAL' }">
                  <input type="radio" v-model="form.customerType" value="NORMAL" />
                  <span class="radio-content">
                    <span class="radio-label">{{ $t('normal') }}</span>
                    <span class="radio-desc">{{ $t('normalDesc') }}</span>
                  </span>
                </label>
                <label class="radio-option vip" :class="{ active: form.customerType === 'VIP' }">
                  <input type="radio" v-model="form.customerType" value="VIP" />
                  <span class="radio-content">
                    <span class="radio-label">{{ $t('vip') }}</span>
                    <span class="radio-desc">{{ $t('vipDesc') }}</span>
                  </span>
                </label>
              </div>
            </div>

            <!-- Call Start Time -->
            <div class="form-group">
              <label class="form-label" for="callStartTime">
                <span class="label-icon">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"/>
                    <polyline points="12 6 12 12 16 14"/>
                  </svg>
                </span>
                {{ $t('callStartTime') }}
              </label>
              <div class="time-input-group">
                <input
                  id="callStartTime"
                  v-model="form.callStartTime"
                  type="datetime-local"
                  class="form-input time-input"
                  :class="{ 'night-active': isNightTime }"
                />
                <button
                  type="button"
                  class="time-now-btn"
                  @click="setNow"
                  :title="$t('setNow')"
                >
                  {{ $t('setNow') }}
                </button>
              </div>
              <span class="form-hint time-hint" :class="{ night: isNightTime }">
                {{ $t('nightHint') }}
              </span>
            </div>

            <!-- Submit -->
            <button type="submit" class="btn-calculate" :disabled="loading || !isFormValid">
              <span v-if="loading" class="btn-loading">
                <span class="loading-dot"></span>
                <span class="loading-dot"></span>
                <span class="loading-dot"></span>
              </span>
              <span v-else class="btn-text">
                {{ $t('calculateRate') }}
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="5" y1="12" x2="19" y2="12"/>
                  <polyline points="12 5 19 12 12 19"/>
                </svg>
              </span>
            </button>

          </form>
        </section>

        <!-- Result Panel -->
        <section class="panel result-panel animate-fade-in-up" style="animation-delay: 160ms">

          <!-- Empty State -->
          <div v-if="!result && !error" class="result-empty">
            <div class="empty-icon">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
                <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
                <line x1="8" y1="21" x2="16" y2="21"/>
                <line x1="12" y1="17" x2="12" y2="21"/>
              </svg>
            </div>
            <p class="empty-text">{{ $t('resultEmpty') }}</p>
            <p class="empty-hint">{{ $t('resultEmptyHint') }}</p>
          </div>

          <!-- Error State -->
          <div v-if="error" class="result-error animate-fade-in">
            <div class="error-icon">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <circle cx="12" cy="12" r="10"/>
                <line x1="15" y1="9" x2="9" y2="15"/>
                <line x1="9" y1="9" x2="15" y2="15"/>
              </svg>
            </div>
            <p class="error-title">{{ $t('requestFailed') }}</p>
            <p class="error-message">{{ error }}</p>
            <button class="btn-retry" @click="handleCalculate">{{ $t('retry') }}</button>
          </div>

          <!-- Success Result -->
          <div v-if="result" class="result-content animate-fade-in">

            <!-- Final Rate Hero -->
            <div class="rate-hero">
              <div class="rate-label">{{ $t('finalRate') }}</div>
              <div class="rate-value">
                <span class="rate-number">{{ formatRate(result.finalRate) }}</span>
                <span class="rate-unit">CNY/min</span>
              </div>
              <div class="rate-meta">
                <span class="meta-badge" :class="result.breakdown.isNightOffPeak ? 'night' : 'day'">
                  {{ result.breakdown.isNightOffPeak ? $t('nightOffPeak') : $t('daytime') }}
                </span>
                <span class="meta-badge" :class="result.breakdown.customerType === 'VIP' ? 'vip' : 'normal'">
                  {{ result.breakdown.customerType }}
                </span>
              </div>
            </div>

            <!-- Breakdown Table -->
            <div class="breakdown">
              <h3 class="breakdown-title">{{ $t('breakdown') }}</h3>

              <div class="breakdown-table">
                <div class="breakdown-row">
                  <span class="breakdown-key">{{ $t('destination') }}</span>
                  <span class="breakdown-value">
                    <span class="country-flag">{{ getCountryEmoji(result.breakdown.countryCode) }}</span>
                    {{ result.breakdown.countryName }}
                    <span class="country-code">{{ result.breakdown.countryCode }}</span>
                  </span>
                </div>
                <div class="breakdown-row">
                  <span class="breakdown-key">{{ $t('baseRate') }}</span>
                  <span class="breakdown-value mono">¥{{ result.baseRate }}</span>
                </div>
                <div class="breakdown-row">
                  <span class="breakdown-key">{{ $t('discountFactor') }}</span>
                  <span class="breakdown-value mono">
                    {{ result.discountFactor }} &times;
                    <span class="discount-note" v-if="result.discountFactor < 1">({{ $t('discountNote', { percent: ((1 - result.discountFactor) * 100).toFixed(0) }) }})</span>
                  </span>
                </div>
                <div class="breakdown-row">
                  <span class="breakdown-key">{{ $t('offPeakReduction') }}</span>
                  <span class="breakdown-value mono">
                    -¥{{ result.offPeakReduction }}
                    <span class="reduction-note" v-if="result.offPeakReduction > 0">{{ $t('reductionNote') }}</span>
                  </span>
                </div>
                <div class="breakdown-divider"></div>
                <div class="breakdown-row total">
                  <span class="breakdown-key">{{ $t('finalRate') }}</span>
                  <span class="breakdown-value mono">¥{{ formatRate(result.finalRate) }}/min</span>
                </div>
              </div>
            </div>

            <!-- Calculation Formula -->
            <div class="formula">
              <div class="formula-label">{{ $t('calculation') }}</div>
              <div class="formula-content">
                <span class="formula-step">¥{{ result.baseRate }}</span>
                <span class="formula-op">&times;</span>
                <span class="formula-step">{{ result.discountFactor }}</span>
                <span class="formula-op">&minus;</span>
                <span class="formula-step">¥{{ result.offPeakReduction }}</span>
                <span class="formula-op">=</span>
                <span class="formula-result">¥{{ formatRate(result.finalRate) }}</span>
              </div>
            </div>

          </div>
        </section>

      </div>

      <!-- API Test Section -->
      <section class="api-section animate-fade-in-up" style="animation-delay: 240ms">
        <div class="api-header">
          <h2 class="api-title">{{ $t('apiTitle') }}</h2>
          <span class="api-method">GET</span>
          <code class="api-url">/api/rate</code>
        </div>
        <div class="api-example">
          <div class="api-example-label">{{ $t('exampleRequest') }}</div>
          <div class="code-block">
            <span class="code-text">
              /api/rate?callerNumber={{ form.callerNumber || '+8613812345678' }}&amp;calleeNumber={{ form.calleeNumber || '+14155551234' }}&amp;customerType={{ form.customerType }}&amp;callStartTime={{ form.callStartTime || '2024-06-06T02:30:00' }}
            </span>
          </div>
        </div>
      </section>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import axios from 'axios'

const form = ref({
  callerNumber: '+8613812345678',
  calleeNumber: '+14155551234',
  customerType: 'NORMAL',
  callStartTime: ''
})

const result = ref(null)
const error = ref(null)
const loading = ref(false)

const isFormValid = computed(() => {
  return form.value.callerNumber.trim() &&
    form.value.calleeNumber.trim() &&
    form.value.customerType &&
    form.value.callStartTime
})

const isNightTime = computed(() => {
  if (!form.value.callStartTime) return false
  const time = new Date(form.value.callStartTime)
  const hour = time.getHours()
  return hour >= 23 || hour < 5
})

function setNow() {
  const now = new Date()
  now.setMinutes(now.getMinutes() - now.getTimezoneOffset())
  form.value.callStartTime = now.toISOString().slice(0, 16)
}

function formatRate(rate) {
  const num = parseFloat(rate)
  return num.toFixed(4)
}

function getCountryEmoji(countryCode) {
  const map = {
    '+86': '\u{1F1E8}\u{1F1F3}', '+1': '\u{1F1FA}\u{1F1F8}'
  }
  return map[countryCode] || '\u{1F30D}'
}

async function handleCalculate() {
  if (!isFormValid.value || loading.value) return

  loading.value = true
  error.value = null
  result.value = null

  try {
    const params = {
      callerNumber: form.value.callerNumber,
      calleeNumber: form.value.calleeNumber,
      customerType: form.value.customerType,
      callStartTime: form.value.callStartTime
    }

    const response = await axios.get('/api/rate', { params })
    result.value = response.data
  } catch (err) {
    error.value = err.response?.data?.message || err.message || 'An error occurred'
  } finally {
    loading.value = false
  }
}

watch(() => form.value.callStartTime, () => {
  if (result.value) {
    result.value = null
  }
})
</script>

<style scoped>
.home {
  max-width: 1100px;
  margin: 0 auto;
}

/* Page Header */
.page-header {
  text-align: center;
  margin-bottom: 56px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 600;
  letter-spacing: -0.04em;
  color: var(--color-text-primary);
  margin-bottom: 12px;
}

.page-subtitle {
  font-size: 1rem;
  color: var(--color-text-secondary);
  max-width: 520px;
  margin: 0 auto;
  line-height: 1.7;
}

/* Calculator Grid */
.calculator-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 48px;
}

@media (max-width: 768px) {
  .calculator-grid {
    grid-template-columns: 1fr;
  }
}

/* Panel */
.panel {
  background: var(--color-bg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-xl);
  padding: 32px;
  box-shadow: var(--shadow-sm);
}

.panel-header {
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--color-border-light);
}

.panel-title {
  font-size: 1.0625rem;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.panel-desc {
  font-size: 0.8125rem;
  color: var(--color-text-tertiary);
}

/* Form */
.form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.8125rem;
  font-weight: 500;
  color: var(--color-text-primary);
}

.label-icon {
  color: var(--color-text-tertiary);
  display: flex;
  align-items: center;
}

.form-input {
  font-size: 0.9375rem;
  padding: 11px 14px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--color-border);
  background: var(--color-bg);
  color: var(--color-text-primary);
  transition: all var(--transition-fast);
}

.form-input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.06);
}

.form-input.night-active {
  border-color: var(--color-text-secondary);
  background: var(--color-bg-secondary);
}

.form-hint {
  font-size: 0.75rem;
  color: var(--color-text-tertiary);
  line-height: 1.5;
}

.form-hint.time-hint {
  transition: color var(--transition-fast);
}

.form-hint.time-hint.night {
  color: var(--color-text-secondary);
}

/* Radio Group */
.radio-group {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}

.radio-option {
  position: relative;
  cursor: pointer;
}

.radio-option input {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.radio-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: 12px 14px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
  background: var(--color-bg);
}

.radio-option:hover .radio-content {
  border-color: var(--color-text-tertiary);
}

.radio-option.active .radio-content {
  border-color: var(--color-accent);
  background: var(--color-bg);
  box-shadow: 0 0 0 1px var(--color-accent);
}

.radio-label {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--color-text-primary);
}

.radio-desc {
  font-size: 0.75rem;
  color: var(--color-text-tertiary);
}

.radio-option.vip.active .radio-content {
  border-color: var(--color-text-secondary);
  box-shadow: 0 0 0 1px var(--color-text-secondary);
}

/* Time Input */
.time-input-group {
  display: flex;
  gap: 8px;
}

.time-input {
  flex: 1;
}

.time-now-btn {
  padding: 0 16px;
  background: var(--color-surface);
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
  font-size: 0.8125rem;
  font-weight: 500;
  border-radius: var(--radius-sm);
  white-space: nowrap;
}

.time-now-btn:hover {
  background: var(--color-border);
  color: var(--color-text-primary);
}

/* Calculate Button */
.btn-calculate {
  width: 100%;
  padding: 14px 24px;
  background: var(--color-accent);
  color: white;
  border-radius: var(--radius-sm);
  font-size: 0.9375rem;
  font-weight: 500;
  letter-spacing: -0.01em;
  margin-top: 8px;
  box-shadow: var(--shadow-sm);
}

.btn-calculate:hover:not(:disabled) {
  background: var(--color-accent-hover);
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.btn-calculate:active:not(:disabled) {
  transform: translateY(0);
  box-shadow: var(--shadow-sm);
}

.btn-calculate:disabled {
  background: var(--color-surface);
  color: var(--color-text-tertiary);
  box-shadow: none;
}

.btn-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.loading-dot {
  width: 5px;
  height: 5px;
  background: white;
  border-radius: 50%;
  animation: pulse 1.2s ease-in-out infinite;
}

.loading-dot:nth-child(2) { animation-delay: 0.2s; }
.loading-dot:nth-child(3) { animation-delay: 0.4s; }

/* Result Empty */
.result-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 24px;
  text-align: center;
  color: var(--color-text-tertiary);
  min-height: 360px;
}

.empty-icon {
  margin-bottom: 16px;
  opacity: 0.3;
}

.empty-text {
  font-size: 0.9375rem;
  color: var(--color-text-tertiary);
  margin-bottom: 6px;
}

.empty-hint {
  font-size: 0.8125rem;
  color: var(--color-text-tertiary);
  opacity: 0.6;
}

/* Result Error */
.result-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 24px;
  text-align: center;
  min-height: 360px;
}

.error-icon {
  color: var(--color-error);
  margin-bottom: 16px;
}

.error-title {
  font-size: 1rem;
  font-weight: 500;
  color: var(--color-text-primary);
  margin-bottom: 8px;
}

.error-message {
  font-size: 0.8125rem;
  color: var(--color-text-secondary);
  margin-bottom: 20px;
}

.btn-retry {
  padding: 8px 20px;
  background: var(--color-surface);
  color: var(--color-text-primary);
  border: 1px solid var(--color-border);
  font-size: 0.8125rem;
  border-radius: var(--radius-sm);
}

.btn-retry:hover {
  background: var(--color-border);
}

/* Rate Hero */
.rate-hero {
  text-align: center;
  padding: 32px 0 24px;
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: 24px;
}

.rate-label {
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: 8px;
}

.rate-value {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 6px;
  margin-bottom: 16px;
}

.rate-number {
  font-size: 3.5rem;
  font-weight: 600;
  letter-spacing: -0.04em;
  color: var(--color-text-primary);
  font-variant-numeric: tabular-nums;
}

.rate-unit {
  font-size: 0.875rem;
  color: var(--color-text-tertiary);
}

.rate-meta {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.meta-badge {
  font-size: 0.6875rem;
  font-weight: 500;
  padding: 4px 10px;
  border-radius: 20px;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.meta-badge.night {
  background: var(--color-text-primary);
  color: white;
}

.meta-badge.day {
  background: var(--color-surface);
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
}

.meta-badge.vip {
  background: var(--color-text-primary);
  color: white;
}

.meta-badge.normal {
  background: var(--color-surface);
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
}

/* Breakdown */
.breakdown {
  margin-bottom: 20px;
}

.breakdown-title {
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: 12px;
}

.breakdown-table {
  display: flex;
  flex-direction: column;
}

.breakdown-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid var(--color-border-light);
}

.breakdown-row:last-child {
  border-bottom: none;
}

.breakdown-key {
  font-size: 0.8125rem;
  color: var(--color-text-secondary);
}

.breakdown-value {
  font-size: 0.8125rem;
  font-weight: 500;
  color: var(--color-text-primary);
  display: flex;
  align-items: center;
  gap: 6px;
}

.breakdown-value.mono {
  font-family: var(--font-mono);
}

.breakdown-divider {
  height: 1px;
  background: var(--color-border);
  margin: 4px 0;
}

.breakdown-row.total .breakdown-key,
.breakdown-row.total .breakdown-value {
  font-weight: 600;
  font-size: 0.9375rem;
}

.country-flag {
  font-size: 1rem;
}

.country-code {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  color: var(--color-text-tertiary);
  margin-left: 4px;
}

.discount-note {
  font-size: 0.75rem;
  color: var(--color-text-tertiary);
  font-weight: 400;
}

.reduction-note {
  font-size: 0.75rem;
  color: var(--color-text-tertiary);
  font-weight: 400;
}

/* Formula */
.formula {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 16px 20px;
}

.formula-label {
  font-size: 0.6875rem;
  font-weight: 500;
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: 10px;
}

.formula-content {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  font-family: var(--font-mono);
  font-size: 0.8125rem;
}

.formula-step {
  color: var(--color-text-primary);
  font-weight: 500;
}

.formula-op {
  color: var(--color-text-tertiary);
  font-size: 0.75rem;
}

.formula-result {
  color: var(--color-text-primary);
  font-weight: 600;
  font-size: 0.9375rem;
}

/* API Section */
.api-section {
  background: var(--color-bg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-xl);
  padding: 28px 32px;
  box-shadow: var(--shadow-sm);
}

.api-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.api-title {
  font-size: 0.9375rem;
  font-weight: 600;
  color: var(--color-text-primary);
}

.api-method {
  font-size: 0.6875rem;
  font-weight: 600;
  padding: 3px 8px;
  background: var(--color-text-primary);
  color: white;
  border-radius: 4px;
  letter-spacing: 0.02em;
}

.api-url {
  font-family: var(--font-mono);
  font-size: 0.8125rem;
  color: var(--color-text-secondary);
  word-break: break-all;
}

.api-example-label {
  font-size: 0.75rem;
  color: var(--color-text-tertiary);
  margin-bottom: 8px;
}

.code-block {
  background: var(--color-surface);
  border-radius: var(--radius-sm);
  padding: 14px 16px;
  border: 1px solid var(--color-border-light);
}

.code-text {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  color: var(--color-text-secondary);
  word-break: break-all;
  line-height: 1.7;
  display: block;
}
</style>
