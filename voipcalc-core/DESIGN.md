# VoIPCalc-Core — DDD 架构设计文档

## 1. 战略设计

### 1.1 问题空间分析

跨境 VOIP 话务系统的费率计算引擎，问题域其实很小很纯粹：

- **核心域**：费率计算（Rate Calculation）—— 这是系统存在的唯一价值
- **支撑域**：电话号码解析（Phone Number Resolution）—— 为核心域服务
- **通用域**：REST 接口暴露、日志记录 —— 纯技术实现

**战略结论**：把费率计算作为核心域，构建一个纯净的领域模型，让业务规则完全脱离基础设施框架的束缚。

### 1.2 限界上下文

```
┌─────────────────────────────────────────────────────────────┐
│  费率计算上下文（Rate Calculation Context）                    │
│                                                              │
│  CallContext ──► RateCalculationEngine ──► RateResult       │
│                     │                                         │
│         ┌──────────┼──────────┐                             │
│         ▼          ▼          ▼                             │
│  BaseRate    Discount    NightOffPeak                        │
│  Policy       Policy       Policy                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│  接口适配上下文（Interface Context）                          │
│                                                              │
│  RateController ──► RateCalculationAppService                  │
└─────────────────────────────────────────────────────────────┘
```

### 1.3 上下文映射

RateCalculationContext（核心）← 应用层 ← InterfaceContext

这是严格的"防腐层"模式：接口层 → 应用层 → 领域层，严格单向依赖，越往里越纯粹。

---

## 2. 战术设计

### 2.1 领域模型

#### 值对象（Value Objects）

| 值对象 | 属性 | 不变性 | 业务规则 |
|--------|------|--------|----------|
| `PhoneNumber` | rawNumber, countryCode | rawNumber 不可变 | +86→中国, +1→美国, 其他→OTHER |
| `CountryCode` | prefix, displayName, baseRate | 全部不可变 | 枚举定义三种国家 |
| `CustomerType` | description, discountFactor | 全部不可变 | VIP=0.9, NORMAL=1.0 |
| `RateZone` | 无状态枚举 | — | 23:00–05:00=夜间低谷，其他=白天 |

#### 实体（Entities）

| 实体 | 标识 | 核心行为 |
|------|------|----------|
| `CallContext` | caller+callee+callerType+time 组合 hash | 无状态，仅承载数据 |
| `RateResult` | 无标识（本质是值对象） | 不可变，包含完整审计链 |

#### 领域服务（Domain Services）

| 服务 | 职责 | 副作用 |
|------|------|--------|
| `BaseRatePolicyService` | 根据国家代码返回基础费率 | 无 |
| `CustomerDiscountPolicyService` | 根据客户身份返回折扣系数 | 无 |
| `NightOffPeakPolicyService` | 根据通话时间返回夜间减免金额 | 无 |
| `RateCalculationEngine` | 编排三大策略，计算最终费率 | 无 |

**设计原则**：所有领域服务都是**无状态、无副作用**的。这意味着：
1. 单元测试不需要 mock 任何内部依赖
2. 业务逻辑可以完全脱离 Spring 独立验证
3. 核心计算规则可以跨进程、跨服务复用

### 2.2 聚合（Aggregates）

```
聚合根: RateCalculationEngine
  │
  ├── BaseRatePolicyService（基础费率策略组件）
  ├── CustomerDiscountPolicyService（客户折扣策略组件）
  └── NightOffPeakPolicyService（夜间减免策略组件）

聚合内部值对象:
  ├── CallContext（输入聚合根）
  ├── RateResult（输出值对象）
  └── RateResult.Breakdown（审计追踪）
```

### 2.3 领域事件 — 扩展性预留

当前版本未使用领域事件，但为未来扩展预留了接口：

```java
public interface RateCalculationListener {
    void onRateCalculated(CallContext context, RateResult result);
}
```

---

## 3. 分层架构

### 3.1 依赖规则

```
┌────────────────────────────────────────────┐
│          接口层（Interface）                │  ← HTTP 请求入口
│   RateController, RateRequest DTOs          │
└─────────────────┬──────────────────────────┘
                  │ 依赖
                  ▼
┌────────────────────────────────────────────┐
│          应用层（Application）               │  ← 用例编排
│   RateCalculationAppService                  │
│   负责：请求参数 → 领域对象 的转换           │
└─────────────────┬──────────────────────────┘
                  │ 依赖
                  ▼
┌────────────────────────────────────────────┐
│          领域层（Domain）                   │  ← 业务规则核心
│   RateCalculationEngine + 3个策略服务        │
│   没有任何框架依赖                         │
└─────────────────┬──────────────────────────┘
                  │ 依赖
                  ▼
┌────────────────────────────────────────────┐
│          基础设施层（Infrastructure）       │  ← 技术实现
│   Spring Boot, REST 适配器, 配置           │
└────────────────────────────────────────────┘
```

### 3.2 包结构

```
com.voip.calc
├── domain/                      ← 核心资产，业务规则所在地
│   ├── model/                  ← 值对象和实体
│   │   ├── CountryCode.java    ← 国家代码枚举 + 基础费率
│   │   ├── CustomerType.java   ← 客户类型枚举 + 折扣系数
│   │   ├── PhoneNumber.java    ← 电话号码值对象
│   │   ├── CallContext.java   ← 通话上下文实体
│   │   ├── RateZone.java      ← 时段枚举（夜间/白天）
│   │   ├── RateZoneResolver.java ← 时段解析工具
│   │   └── RateResult.java    ← 费率结果（不可变 + 审计链）
│   ├── service/               ← 领域服务
│   │   ├── BaseRatePolicyService.java
│   │   ├── CustomerDiscountPolicyService.java
│   │   ├── NightOffPeakPolicyService.java
│   │   └── RateCalculationEngine.java ← 核心引擎
│   └── repository/            ← 预留扩展接口
├── application/               ← 应用层
│   ├── service/               ← 应用服务
│   │   └── RateCalculationAppService.java
│   └── dto/                   ← 应用层 DTO
│       ├── CallContextRequest.java
│       └── RateResponse.java
├── infrastructure/            ← 基础设施层
│   ├── adapter/              ← 外部适配器
│   └── config/               ← Spring 配置
├── interface/                 ← 接口层
│   ├── controller/           ← REST 控制器
│   │   └── RateController.java
│   └── dto/                  ← 接口层 DTO
│       └── RateRequest.java
└── VoipCalcApplication.java  ← Spring Boot 启动类
```

---

## 4. 核心计算公式

### 4.1 计算公式

```
折扣后费率 = 基础费率 × 客户折扣系数
最终费率 = max(0, 折扣后费率 − 夜间减免额)
```

### 4.2 边界条件处理

| 场景 | 输入 | 期望输出 | 处理方式 |
|------|------|----------|----------|
| 国家代码解析失败 | null / 空字符串 | OTHER（¥0.50） | 空值保护，返回兜底默认值 |
| 客户类型无效 | null / 空字符串 | NORMAL（1.0） | 空值保护，返回默认折扣 |
| 通话时间为空 | null | 无夜间减免 | 空值保护，返回 0 减免 |
| 夜间减免后为负数 | VIP + 中国 + 夜间 | ¥0.00 | max(0, result) 兜底 |

---

## 5. 测试策略

### 5.1 测试金字塔

```
         ┌──────────┐
         │ 接口测试  │  ← RateControllerTest（Spring MockMvc）
         │   (2)    │
         ├──────────┤
         │ 应用层   │  ← RateCalculationAppServiceTest（Mockito）
         │   (1)    │
         ├──────────┤
         │ 领域层   │  ← RateCalculationEngineTest（纯单元测试）
         │   (10)   │  ← 每个策略、每个边界条件独立测试
         ├──────────┤
         │ 模型层   │  ← CountryCodeTest, CustomerTypeTest
         │   (8)    │     RateZoneResolverTest（无任何依赖）
         └──────────┘
```

### 5.2 测试覆盖策略

所有测试都是**黑盒测试**，只验证输入-输出行为：
- 不测试实现细节，只测试业务规则
- 领域层测试完全不依赖 Spring 容器
- 每个业务规则对应一个明确的测试用例

---

## 6. 扩展性预留

### 6.1 未来扩展方向

1. **新增国家费率**：在 `CountryCode` 枚举中新增条目即可
2. **新增客户折扣**：在 `CustomerType` 中新增折扣系数
3. **新增时段减免**：在 `RateZone` 中新增时段枚举，扩展 `NightOffPeakPolicyService`
4. **费率变更历史**：通过 `RateResult.Breakdown` 的审计链天然支持

### 6.2 开闭原则保障

每个策略服务都遵循**开闭原则**：
- 对扩展开放：新增国家/折扣类型只需添加枚举值
- 对修改封闭：核心计算引擎 `RateCalculationEngine` 无需改动

---

## 7. 关键设计决策

| 决策 | 选项 | 选择 | 理由 |
|------|------|------|------|
| 领域模型类型 | Entity vs Value Object | 以 Value Object 为主 | 费率计算本质是函数，无状态实体 |
| 策略注入方式 | 构造器注入 | 构造器注入 | 显式依赖，无隐式耦合 |
| 折扣计算顺序 | 先折扣后减免 vs 先减免后折扣 | 先折扣后减免 | VIP 夜间费率 = ¥0.05×0.9-0.02 = ¥0.025 |
| 费率精度 | double vs BigDecimal | BigDecimal | 金融计算必须避免浮点误差 |
| 结果对象类型 | record vs class | Builder class | 便于扩展审计字段 |
