export default {
  // Header
  brandName: 'VoIPCalc',
  brandTag: '核心引擎',
  navHint: '费率计算 API v1.0',

  // Footer
  footerText: 'VoIPCalc-Core — 跨境 VOIP 费率引擎  •  Spring Boot + Vue',

  // Home - Page Header
  pageTitle: '费率计算器',
  pageSubtitle: '输入通话详情，基于目的地、客户身份和时间段策略计算每分钟费率。',

  // Home - Panel Header
  panelTitle: '通话参数',
  panelDesc: '配置通话上下文',

  // Home - Form Labels
  callerNumber: '主叫号码',
  callerNumberHint: '发起呼叫的电话号码',
  calleeNumber: '被叫号码',
  calleeNumberHint: '目标电话号码（决定国家费率）',
  customerType: '客户类型',
  callStartTime: '通话开始时间',
  nightHint: '夜间低谷：23:00 – 05:00（每分钟节省 ¥0.02）',

  // Customer Types
  normal: '普通用户',
  normalDesc: '普通用户 — 无折扣',
  vip: 'VIP',
  vipDesc: '留学生 / 华人卡 — 九折优惠',

  // Buttons
  setNow: '现在',
  calculateRate: '计算费率',

  // Result Empty
  resultEmpty: '结果将显示在这里',
  resultEmptyHint: '配置通话参数后点击计算',

  // Result Error
  requestFailed: '请求失败',
  retry: '重试',

  // Result Success
  finalRate: '最终费率',
  nightOffPeak: '夜间低谷',
  daytime: '白天',
  breakdown: '费率明细',
  destination: '目的地',
  baseRate: '基础费率',
  discountFactor: '折扣系数',
  offPeakReduction: '低谷减免',
  discountNote: '节省 {percent}%',
  reductionNote: '（夜间优惠）',
  calculation: '计算过程',

  // API Section
  apiTitle: 'API 端点',
  exampleRequest: '请求示例',
}
