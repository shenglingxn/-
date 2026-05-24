# 🏫 CampusTrade · 校园二手交易平台

> 基于 Spring Boot + JPA + JWT 的校园二手交易平台  
> 专为高校学生打造的安全、便捷、专属的闲置物品流转平台

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-11-blue" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0-orange" alt="MySQL">
  <img src="https://img.shields.io/badge/License-MIT-green" alt="License">
</p>

---

## ✨ 功能特性

### 👤 用户模块
- 手机号注册 + 短信验证码
- 用户名/手机号 + 角色选择登录
- JWT Token 认证（支持记住我 7 天有效）
- 连续错误锁定机制（5 次失败锁定 10 分钟）
- 同一账号最多 3 个终端在线

### 🎓 校园认证
- 学号 + 真实姓名双重验证
- 模拟学校统一认证接口
- 学号以 `2024` 开头自动认证通过
- 认证后解锁全部功能

### 📦 商品管理
- 商品发布（标题、分类、价格、描述）
- 多分类浏览（教材教辅、电子数码、生活用品等）
- 关键字搜索
- 按分类筛选
- 商品详情查看

### 📋 订单管理
- 创建订单
- 我买到的 / 我卖出的 双视角
- 订单状态追踪（待处理、已付款、已收货等）

### 💬 即时消息
- 站内信系统
- 未读消息计数
- 双向对话

### ⭐ 信用评价
- 交易完成后可评价
- 评分系统（1-5 星）
- 用户信用分自动计算

### ⚙️ 管理后台
- 管理员专用账号
- 系统管理功能

---

## 🚀 快速开始

### 方法一：本地运行

#### 前置条件

| 工具 | 版本 |
|------|------|
| JDK | 11+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |

#### 步骤

```bash
# 1. 克隆项目
git clone https://github.com/your-username/campus-trade.git
cd campus-trade

# 2. 创建数据库
mysql -u root -p < sql/init.sql

# 3. 修改数据库密码
# 编辑 src/main/resources/application.yml → spring.datasource.password

# 4. 编译运行
mvn spring-boot:run
```

#### 访问

浏览器打开 **http://localhost:8080**

### 方法二：Docker 部署（推荐）

```bash
docker-compose up -d
```

会自动启动 MySQL 8.0 + 应用，首次启动自动建表。

---

## 🧪 测试账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 👑 管理员 | `admin` | `admin123456` | 系统管理员 |
| 👤 测试用户 A | `alice` | `abc123456` | 已认证，有发布商品 |
| 👤 测试用户 B | `bob` | `abc123456` | 未认证 |

### 万能验证码

注册时验证码输入 **`000000`** 即可通过。

---

## 📸 界面预览

```
┌────────────────────────────┐
│  🏫 校园二手               │
│  安全·便捷·校园专属        │
│  ┌──────────────────┐      │
│  │ 🔍 搜索商品...    │      │
│  └──────────────────┘      │
│                            │
│ [全部][教材][数码][生活...] │
│                            │
│ ┌────────────────────────┐ │
│ │ 📚 高等数学第七版  ¥25 │ │
│ │ 九成新 考研用书        │ │
│ └────────────────────────┘ │
│ ┌────────────────────────┐ │
│ │ 💻 惠普计算器     ¥60  │ │
│ │ 工科必备 功能完好      │ │
│ └────────────────────────┘ │
│                            │
│ [🏠][💬][📦][📋][👤]     │
└────────────────────────────┘
```

---

## 🏗 技术架构

```
┌─────────────────────────────────┐
│          Frontend (SPA)         │
│    HTML5 + CSS3 + Vanilla JS    │
├─────────────────────────────────┤
│     Spring Boot 2.7 (REST)      │
│  ├── Spring Security (JWT)      │
│  ├── Spring Data JPA            │
│  └── Spring WebSocket           │
├─────────────────────────────────┤
│         MySQL 8.0               │
└─────────────────────────────────┘
```

### 模块结构

```
com.campus.trade
├── config/          # 安全、Web、数据初始化配置
├── security/        # JWT 令牌生成与验证
├── common/          # 通用响应类
├── user/            # 用户注册/登录/认证
├── goods/           # 商品发布/浏览/搜索
├── order/           # 订单创建/管理
├── evaluation/      # 信用评价
└── message/         # 站内消息
```

### API 接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/user/register` | 用户注册 | ❌ |
| POST | `/api/user/login` | 用户登录 | ❌ |
| POST | `/api/user/send-code` | 发送验证码 | ❌ |
| POST | `/api/user/logout` | 退出登录 | ✅ |
| GET | `/api/user/me` | 获取当前用户信息 | ✅ |
| POST | `/api/user/auth` | 提交校园认证 | ✅ |
| GET | `/api/goods/list` | 商品列表（支持分类/搜索） | ❌ |
| GET | `/api/goods/{id}` | 商品详情 | ❌ |
| POST | `/api/goods/create` | 发布商品 | ✅ |
| GET | `/api/goods/mine` | 我的发布 | ✅ |
| POST | `/api/order/create` | 创建订单 | ✅ |
| GET | `/api/order/buy` | 我买到的订单 | ✅ |
| GET | `/api/order/sell` | 我卖出的订单 | ✅ |
| POST | `/api/evaluation/create` | 提交评价 | ✅ |
| POST | `/api/message/send` | 发送消息 | ✅ |
| GET | `/api/message/conversation` | 获取对话 | ✅ |

---

## 📄 许可证

本项目基于 MIT 许可证开源。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

<p align="center">
  Made with ❤️ for Campus Life
</p>
