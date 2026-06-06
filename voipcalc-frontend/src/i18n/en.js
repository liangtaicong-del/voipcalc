export default {
  // Header
  brandName: 'VoIPCalc',
  brandTag: 'Core Engine',
  navHint: 'Rate Calculation API v1.0',

  // Footer
  footerText: 'VoIPCalc-Core — Cross-border VOIP Rate Engine • Built with Spring Boot + Vue',

  // Home - Page Header
  pageTitle: 'Rate Calculator',
  pageSubtitle: 'Enter call details to calculate the per-minute rate based on destination country, customer identity, and time-of-day policies.',

  // Home - Panel Header
  panelTitle: 'Call Parameters',
  panelDesc: 'Configure the call context',

  // Home - Form Labels
  callerNumber: 'Caller Number',
  callerNumberHint: 'The phone number initiating the call',
  calleeNumber: 'Callee Number',
  calleeNumberHint: 'The destination phone number (determines country rate)',
  customerType: 'Customer Type',
  callStartTime: 'Call Start Time',
  nightHint: 'Night off-peak: 23:00 – 05:00 (saves ¥0.02/min)',

  // Customer Types
  normal: 'Normal',
  normalDesc: 'Regular user — no discount',
  vip: 'VIP',
  vipDesc: 'Overseas student / Chinese card — 10% off',

  // Buttons
  setNow: 'Now',
  calculateRate: 'Calculate Rate',

  // Result Empty
  resultEmpty: 'Result will appear here',
  resultEmptyHint: 'Configure call parameters and click Calculate',

  // Result Error
  requestFailed: 'Request Failed',
  retry: 'Retry',

  // Result Success
  finalRate: 'Final Rate',
  nightOffPeak: 'Night Off-Peak',
  daytime: 'Daytime',
  breakdown: 'Rate Breakdown',
  destination: 'Destination',
  baseRate: 'Base Rate',
  discountFactor: 'Discount Factor',
  offPeakReduction: 'Off-Peak Reduction',
  discountNote: '{percent}% off',
  reductionNote: '(night rate)',
  calculation: 'Calculation',

  // API Section
  apiTitle: 'API Endpoint',
  exampleRequest: 'Example Request',
}
