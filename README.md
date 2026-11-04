# 知序 Study (ZhiXu Study)

> **知序 Study** — 宁静专注的本地题库管理、智能清洗导入、间隔复习与多题型沉浸刷题工作台。基于 Kotlin + Jetpack Compose + Material 3 原生打造。

---

## 目录结构

```text
知序study/
├── app/                      # Android 原生主工程 (Kotlin + Jetpack Compose)
│   ├── src/main/
│   │   ├── java/com/example/
│   │   │   ├── data/
│   │   │   │   ├── local/    # Room 实体 (Entities)、DAO (AppDao)、数据库单例与种子数据
│   │   │   │   └── parser/   # 智能导入引擎 (SmartImportEngine)
│   │   │   ├── ui/
│   │   │   │   ├── components/ # 苹果级悬浮 Dock、通用卡片、状态胶囊、空态插画
│   │   │   │   ├── screens/    # 27 个界面 (学习/题库/复习/统计/导入/答题/体检/设置等)
│   │   │   │   ├── theme/      # Material 3 设计系统、Plus Jakarta Sans、语义色板
│   │   │   │   └── viewmodel/  # StudyViewModel 统一驱动单向数据流与状态机
│   │   │   └── MainActivity.kt # 应用程序入口、导航控制器、平板双栏响应式布局
│   │   └── res/              # 矢量图标、字体、明暗色彩资源
│   └── build.gradle.kts      # 模块依赖与构建脚本
├── docs/                     # 完整产品全书与交付规格
│   ├── docs/                 # 00-11 体系化规格（PRD、逐页设计、导入引擎、测试矩阵）
│   ├── reference/            # 交互式浏览器原型与设计对照图
│   └── samples/              # 题库样例 JSON / CSV 与规则预设模板
├── public/                   # 历史 Web 原型与界面截图备份
├── gradle/                   # Gradle Wrapper 依赖版本声明 (libs.versions.toml)
├── gradlew / gradlew.bat     # 跨平台 Gradle 命令行构建脚本
└── settings.gradle.kts       # 仓库与项目设置
```

---

## 核心特性

1. **纯本地隐私安全**：基于 Room 数据库与 DataStore，题库、刷题轨迹、错题本均存储在本地设备，零隐私泄露风险。
2. **智能导入与冲突审查引擎**：
   - 支持 CSV、JSON、结构化文本及剪贴板智能解析；
   - 自动清除前导题号（正则）、标准答案智能归一化（`√/对` 映射为 `正确`，多字母映射）；
   - 跨库重复去重与答案冲突检测（如跨库相同题干不同答案高亮预警并支持一键合并）。
3. **全题型支持与草稿纸**：
   - 单选题、多选题、判断题、填空题、综合计算分析题；
   - 答题界面内嵌手写手势草稿纸（支持撤销、橡皮擦与清空）。
4. **苹果风悬浮底栏（Apple-Style Floating Dock）**：
   - 磁吸悬浮流体圆角 Dock，自适应屏幕宽度与平板端布局；
   - 结合微震动触感（Haptic Feedback）与平滑转场动画。
5. **平板与横屏适配**：
   - 采用响应式双栏架构（Master-Detail Split View），在大屏/折叠屏设备上自动呈现侧边导航与内容详情联动。

---

## 本地运行与构建指南

### 前置条件
- **Android Studio**：Ladybug (2024.2.1) 或更新版本
- **JDK**：Java 17 或 21（项目基于 AGP 9.1.1 + Kotlin 2.2.10，编译级别为 Java 11/17 兼容）
- **Android SDK**：API Level 36 (Android 15/16)

### 快速开始

1. **克隆项目并打开**：
   在 Android Studio 中选择 `File -> Open`，定位至当前目录 `知序study`。
2. **本地环境签名兼容**：
   工程已配置自适应签名逻辑：当本地不存在 AI Studio 远程 keystore 时，自动回退使用 Android 开发者标准调试签名，无需手动修改代码即可本地直接编译安装。
3. **运行或调试**：
   连接 Android 模拟器或实体机（Android 7.0 / API 24 及以上），点击右上角 `Run 'app'` 即可。
4. **命令行构建**：
   ```bash
   # Linux / macOS
   ./gradlew assembleDebug

   # Windows
   .\gradlew.bat assembleDebug
   ```

---

## 审查与修复说明

本次项目审查已执行以下维护与修复：
- [x] **消除重载歧义**：整合 `StudyViewModel` 中重复声明的两个 `createNewBank` 方法，增加名称去除空格、空值校验与书名号标准化，修复调用点编译歧义。
- [x] **后台定时器泄漏防护**：优化 `StudyViewModel` 在退出答题界面时的 `quizTimerJob` 取消逻辑，避免后台空转唤醒。
- [x] **修正失效单元测试断言**：修正 `ExampleRobolectricTest` 中残留的 `"My Application"` 错误断言为 `"知序 Study"`。
- [x] **新增业务单元测试**：编写 `SmartImportEngineTest`，覆盖演示数据规范、判断题标准化、题号剥离规则、冲突检测及非法选项定位（覆盖 率大幅提升）。
- [x] **自适应构建脚本增强**：在 `app/build.gradle.kts` 中对 `debug.keystore` 和 `my-upload-key.jks` 增加文件存在性检查，彻底解决本地环境无法构建的问题。
- [x] **补齐跨平台 Wrapper 脚本**：补充缺失的 `gradlew` 与 `gradlew.bat`。
