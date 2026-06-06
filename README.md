# VoIPCalc-Core

跨境 VOIP 话务系统费率计算引擎 —— 展示 DDD、TDD 与 XP 研发范式的工程实践。

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![Vue](https://img.shields.io/badge/Vue-3-42b883)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 项目概述

VoIPCalc-Core 是一个跨境 VOIP 话务系统的计价引擎。它的职责只有一个：根据通话的目的地国家、客户身份和通话时段，计算出最终每分钟费率。

## 业务规则

| 规则 | 条件 | 结果 |
|------|------|------|
| 基础费率 | 拨打 +86（中国） | ¥0.10/分钟 |
| 基础费率 | 拨打 +1（美国） | ¥0.05/分钟 |
| 基础费率 | 拨打其他国家 | ¥0.50/分钟 |
| 客户折扣 | VIP（留学生/华人卡） | 打九折（系数 0.9） |
| 夜间低谷 | 23:00 – 05:00 发起通话 | 每分钟减 ¥0.02（最低 ¥0.00） |

## 架构图

```
领域层（纯粹、无状态、无副作用）
    │
    ├── CountryCode.java           ← 值对象：国家代码 + 基础费率
    ├── CustomerType.java          ← 值对象：客户类型 + 折扣系数
    ├── PhoneNumber.java          ← 值对象：电话号码
    ├── CallContext.java          ← 实体：通话上下文
    ├── RateResult.java           ← 不可变结果 + 审计链
    ├── RateZoneResolver.java     ← 纯工具：时段解析
    │
    └── RateCalculationEngine.java ← 核心引擎
        ├── BaseRatePolicyService（基础费率策略）
        ├── CustomerDiscountPolicyService（客户折扣策略）
        └── NightOffPeakPolicyService（夜间减免策略）

应用层
    └── RateCalculationAppService

接口层
    └── RateController（REST API）
```

## 快速开始

### 环境要求

- Java 17+
- Maven 3.8+
- Node.js 18+
- npm 9+

### 启动后端

```bash
cd voipcalc-core
mvn clean install
mvn spring-boot:run
# API 地址: http://localhost:8080/api/rate
```

### 启动前端

```bash
cd voipcalc-frontend
npm install
npm run dev
# 前端地址: http://localhost:5173
```

### API 示例

```bash
# 计算一个美国电话、VIP用户、凌晨02:30拨打的费率
curl "http://localhost:8080/api/rate?\
callerNumber=+8613812345678&\
calleeNumber=+14155551234&\
customerType=VIP&\
callStartTime=2024-06-06T02:30:00"
```

```json
{
  "baseRate": 0.05,
  "discountFactor": 0.9,
  "offPeakReduction": 0.02,
  "finalRate": 0.025,
  "currency": "CNY",
  "breakdown": {
    "countryCode": "+1",
    "countryName": "美国",
    "customerType": "VIP",
    "isNightOffPeak": true,
    "rateZone": "NIGHT_OFF_PEAK"
  }
}
```

### 运行测试

```bash
cd voipcalc-core
mvn test
```

---

## 代码洁癖细节

这个项目展示了一些严格的代码质量标准：

### 无 AI 噪音
- 无死代码、无占位注释、无未解决的 TODO
- 每个类、每个方法、每个字段都有清晰明确的存在理由
- 没有过度工程——领域模型里只有业务规则需要的东西

### 纯净的领域模型
- `domain/` 包**零** Spring、零 JUnit、零任何框架导入
- `RateCalculationEngine` 是一个普通的 Java 类——没有注解、没有继承框架类
- 三个策略服务都可以在 1 秒内独立完成测试，不依赖 Spring 容器

### 不可变优先
- `CallContext`、`PhoneNumber`、`RateResult`、`RateResult.Breakdown` 全部不可变
- `BigDecimal` 专用于货币计算——不用 `double` 也不用 `float`
- 所有构造器参数都用 `Objects.requireNonNull` 做校验

### 计算精度
```java
BigDecimal discountedRate = baseRate.multiply(discountFactor)
    .setScale(4, RoundingMode.HALF_UP);

BigDecimal finalRate = rawFinalRate.compareTo(BigDecimal.ZERO) < 0
    ? BigDecimal.ZERO : rawFinalRate;
```

### 显式优于隐式
- 没有魔法数字——`OFF_PEAK_REDUCTION = 0.02` 是具名常量
- 没有隐式转换——每个 `String → CountryCode` 转换都是显式的
- 没有隐藏状态——引擎没有在调用之间改变状态的实例字段

### AI 是工具，不是权威

AI 被用于：
- 样板代码生成（测试脚手架、CSS 结构）
- 模式确认（Builder vs Record、依赖注入策略）
- 设计讨论（分层、DDD 限界上下文）

AI **没有被允许**：
- 独自做架构决策
- 未经论证就引入框架或依赖
- 留下未经检查的"AI优化"模式

所有 AI 对话和提示词都保存在 `voipcalc-core/PROMPTs.md` 中，可以随时审查。

---

## 文档交付物

| 文件 | 用途 |
|------|------|
| `voipcalc-core/SPEC.md` | 验收标准和 API 契约 |
| `voipcalc-core/DESIGN.md` | DDD 分析和架构决策 |
| `voipcalc-core/PROMPTs.md` | 完整的 AI 对话历史记录 |
| `README.md` | 项目概述和代码质量说明 |

---

## Git 提交历史

提交历史遵循 XP 原则，小步原子化提交：

1. `init` — 项目脚手架 + Maven pom.xml
2. `feat(domain): 添加国家代码和客户类型枚举` — `CountryCode.java`、`CustomerType.java`、`RateZone.java`
3. `feat(domain): 添加电话号码值对象` — `PhoneNumber.java`
4. `feat(domain): 添加通话上下文实体` — `CallContext.java`
5. `feat(domain): 添加时段解析工具和费率结果` — `RateZoneResolver.java`、`RateResult.java`
6. `feat(domain): 添加三个定价策略服务`
7. `feat(domain): 添加费率计算引擎核心服务`
8. `feat(application): 添加应用层服务与 DTO`
9. `feat(interface): 添加 REST 控制器与 Spring Boot 启动类`
10. `test(domain): 添加引擎全面单元测试`
11. `feat(frontend): 添加 Vue 3 费率计算器界面`
12. `docs: 添加设计文档、AI对话记录和项目说明`
