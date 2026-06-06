# AI 对话记录 — VoIPCalc-Core 开发过程

## 第一轮：需求分析与领域建模

### 对话 1.1 — 领域模型设计

> 我要设计一个 VOIP 费率计算引擎，使用 Java 开发。
> 核心接口是 calculateRate(CallContext context)，返回每分钟费率。
>
> 业务规则：
> 1. 基础费率按国家：CN(+86)=¥0.10, US(+1)=¥0.05, 其他=¥0.50
> 2. 客户折扣：VIP=0.9（留学生/华人卡打九折），NORMAL=1.0（不打折）
> 3. 夜间低谷：23:00-05:00，每分钟减¥0.02，费率不能为负
>
> 我想用 DDD（领域驱动设计）。帮我：
> 1. 定义通用语言（实体、值对象、领域服务）
> 2. 划分限界上下文
> 3. 设计纯领域模型，核心部分不依赖任何框架
>
> 使用 Java 17，领域模型要纯净，不要框架依赖。

### 对话 1.2 — 国家代码解析

> 国家代码解析怎么做？电话号码格式示例有：
> +8613812345678、8613812345678、+14155551234、14155551234、+441234567890
>
> 怎样设计一个纯净的、零框架依赖的解析方式？

### 对话 1.3 — 夜间时间边界处理

> 夜间低谷时段是 23:00 到次日 05:00。
> 需要把任意 LocalTime 分类为 NIGHT_OFF_PEAK 或 DAYTIME。
>
> 怎么处理边界？
> - 23:00:00 → 属于夜间低谷（包含）
> - 05:00:00 → 不属于夜间低谷（排除）
>
> 还要处理跨天的情况。给我一个干净的实现。

## 第二轮：核心领域服务实现

### 对话 2.1 — 引擎编排

> 我有三个独立的领域服务：
> - BaseRatePolicyService：根据 CountryCode 返回基础费率
> - CustomerDiscountPolicyService：根据 CustomerType 返回折扣系数
> - NightOffPeakPolicyService：如果时间是夜间，返回 ¥0.02 减免
>
> 现在需要 RateCalculationEngine 来编排它们。
> 计算公式：
> discountedRate = baseRate * discountFactor
> finalRate = max(0, discountedRate - offPeakReduction)
>
> 设计成纯领域服务，无副作用。
> 用构造器注入还是静态工厂方法？

### 对话 2.2 — 不可变结果对象

> 我需要 RateResult 类来承载计算结果，包含：
> - baseRate、discountFactor、offPeakReduction、finalRate（都是 BigDecimal）
> - currency（String）
> - 一个审计追踪对象 Breakdown
>
> 用 Java record 还是 Builder 模式的 class？
> 需要不可变性。

## 第三轮：Spring 集成与 API 层

### 对话 3.1 — REST API 设计

> 我要把费率计算暴露为 REST API。
> GET /api/rate，查询参数：callerNumber、calleeNumber、customerType、callStartTime（ISO-8601）
>
> 怎么做到：
> 1. 查询参数 → CallContext 领域对象的干净转换
> 2. 优雅的错误处理
> 3. 结构化的 JSON 响应，包含费率和计算过程
>
> 要避免"智能 UI 反模式"——保持控制器薄薄的一层。

### 对话 3.2 — 错误处理策略

> API 的错误响应格式应该怎么设计？
> 需要处理的场景：
> - 必填参数缺失 → 400
> - 日期格式不对 → 400
> - 内部计算异常 → 500
>
> 用 @ControllerAdvice 还是内联 @ExceptionHandler？

## 第四轮：TDD 测试策略

### 对话 4.1 — 引擎的测试策略

> 我想先写测试，再实现 RateCalculationEngine。
> 验收标准：
> 1. CN(+86)、NORMAL、白天 → ¥0.10/分钟
> 2. US(+1)、VIP、白天 → ¥0.045/分钟
> 3. US(+1)、VIP、夜间凌晨02:30 → ¥0.025/分钟
> 4. US(+1)、NORMAL、夜间23:45 → ¥0.03/分钟
> 5. OTHER、NORMAL、夜间 → ¥0.48/分钟
> 6. OTHER、VIP、白天 → ¥0.45/分钟
>
> 需要验证：
> - 每个费率规则独立工作
> - RateResult 中的审计链完整
> - 边界条件（23:00 包含，05:00 排除）
>
> 用 JUnit 5，不依赖 Spring 容器。

### 对话 4.2 — Mock 策略

> RateCalculationAppService 的单元测试需要 mock RateCalculationEngine。
> 选哪个？
> 1. @Mock 注解（Mockito）
> 2. Mockito.mock()
> 3. 手动依赖注入
>
> 最干净的方式是什么？

## 第五轮：Vue 前端界面

### 对话 5.1 — 极简黑白设计

> 给 VoIP 费率计算器做一个 Vue 3 前端。
> 要求：Apple 风格的极简黑白设计。
>
> 设计要求：
> - 主色调纯黑 (#000000) 和纯白 (#FFFFFF)
> - 使用 Google Fonts 的 Inter 字体
> - 卡片式布局，有轻微阴影
> - 数字和代码用等宽字体
> - 结果切换有平滑动画
> - 响应式两栏布局（左侧输入，右侧结果）
>
> 用 Tailwind 还是纯 CSS？尽量少依赖。

### 对话 5.2 — 表单验证

> Vue 3 Composition API 里的表单验证：
> - callerNumber：必填，非空
> - calleeNumber：必填，非空
> - customerType：必填，值为 NORMAL 或 VIP
> - callStartTime：必填，合法 ISO datetime
>
> 用 vee-validate 还是自己用 computed 属性写？
> 想尽量少引入依赖。

## 第六轮：代码审查与重构

### 对话 6.1 — 代码质量检查清单

> 帮我审查以下类的代码质量：
>
> 1. 有没有潜在的空指针风险？
> 2. BigDecimal 舍入处理正确吗？
> 3. 领域服务真的是无状态的吗？
> 4. 有没有不必要的框架耦合在领域层？
> 5. 包名符合 DDD 规范吗？
>
> 要审查的类：
> - RateCalculationEngine
> - CallContext
> - PhoneNumber
> - RateZoneResolver

### 对话 6.2 — Git 提交策略

> 我想展示一个清晰的 XP 风格提交历史，小步提交、原子化。
> 我的计划：
>
> 1. init：项目脚手架 + pom.xml
> 2. feat：领域模型实体（CountryCode, CustomerType, PhoneNumber, CallContext, RateResult）
> 3. feat：领域服务（3个策略 + 引擎）
> 4. feat：应用层
> 5. feat：REST 控制器
> 6. test：领域模型单元测试
> 7. test：引擎单元测试
> 8. feat：Vue 前端
> 9. docs：设计文档
>
> 这个提交结构合理吗？有没有需要拆分得更细的地方？