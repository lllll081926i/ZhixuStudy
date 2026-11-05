<div align="center">

# 知序 Study · ZhiXu Study

**宁静克制 · 本地离线 · 间隔复习 · 智能解析 · 平板适配**

[![Android](https://img.shields.io/badge/Platform-Android%207.0%2B%20(API%2024%2B)-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.00-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Monet%20Dynamic%20Color-6750A4?logo=materialdesign&logoColor=white)](https://m3.material.io)
[![Room](https://img.shields.io/badge/Room-v3%20Indexed%20SQLite-4285F4?logo=sqlite&logoColor=white)](https://developer.android.com/training/data-storage/room)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

*专为自律学习者打造的高质感原生 Android 本地题库管理与间隔复习工作台*

</div>

---

## 🌟 核心设计哲学

> **“大道至简，宁静致远”**  
> 知序 Study 摒弃了现代学习软件泛滥的信息流、社区社交、喧嚣排行榜与烦人音效，回归学习本质：**纯本地离线隐私、科学间隔记忆、多格式智能提取、克制优雅的视觉质感。**

---

## ✨ 亮点特性

### 1. 🧠 科学认知记忆调度（SuperMemo-2 SM-2）
- **科学记忆曲线**：内置经典 SM-2（SuperMemo 2）间隔重复算法，综合考量重复次数（Repetitions）、间隔天数（Interval）、难易度因子（Ease Factor）与记忆质量评分（Again / Hard / Good / Easy）；
- **动态复习流水线**：首页每日任务与复习模块自动计算到期题量，错题即时进入复习池，答对逐步延期巩固，杜绝低效盲目刷题；
- **打卡里程碑彩蛋**：连续打卡达成时触发原生粒子物理彩带喷发动效（Confetti Effect），提供静谧克制但极具仪式感的正向反馈。

### 2. ⚡ 零第三方库依赖的文件流解析引擎（SmartImportEngine）
通过底层 `ZipInputStream` + `XmlPullParser` + 32KB 流式内存缓冲（Streaming Buffer），无需依赖臃肿的 Apache POI 或大型解析库，毫秒级解析多兆大文件且极低内存占用：
- **Excel 表格**（`.xlsx`）：支持多工作表、共享字符串池（SharedStrings）、自动提取列名与自适应字段映射；
- **Word 试卷**（`.docx`）：流式提取 XML 段落，智能分离选择题、判断题、解析与章节；
- **Anki 记忆库**（`.txt` / `.tsv`）：完美解析 `{{c1::填空内容}}` 完形挖空语法并自动转为专业填空题，清洗 HTML 标签；
- **Markdown 文档**（`.md`）：支持 `#` 标题题干、`- [ ]` 列表选项、`> 答案/解析` 块级引用语法；
- **教育规范格式**：支持 Moodle **GIFT** 格式（`{=正确 ~错误}`）、**Aiken** 格式（`ANSWER: A`）、**问卷星**与**学习通**导出试卷；
- **题干内嵌括号提取**：智能提取题干括号中的答案（如 `( A )` 或 `（正确）`），自动将括号挖空归一化为答题线；
- **三步向导与冲突审计**：格式匹配 ➔ 列映射与前导题号清洗 ➔ 逐题可视化预览（高亮预警重复题与答案冲突题）。

### 3. 🎯 沉浸式刷题与答题卡交互
- **水平平滑切题**：集成 `HorizontalPager`，支持丝滑的手势横滑翻题与微动效反馈；
- **即时选项划除**：长按或辅助点击选项可一键划除排查干扰项（中划线效果），还原真实考场推导体验；
- **悬浮答题卡**：全考场视图一键折叠/展开答题卡，实时标注未做、已做、标记关注与排除题数；
- **交卷前智能诊断**：交卷时自动检测漏答题目，提供直观的题号网格定位，杜绝误触交卷。

### 4. 🖨️ 错题打印与考前 A4 PDF 导出
- **脱机纯文本排版引擎**：基于 `WrongBookPrintHelper` 构建专为 A4 纸张定制的标准试卷排版 HTML；
- **卷面与答案分离**：正面输出无答案实战做题卷（留有标准作答留白），文末分页附带【参考答案与名师解析附录】；
- **Android 原生打印协议**：主线程 Looper 队列调度系统 `PrintManager`，完美联动系统打印服务并支持**一键另存为 PDF**。

### 5. 📐 原生 LaTeX 数学公式渲染
- **轻量本地化引擎**：无需加载庞大的 WebView 或外部 JS 库，内置 `SereneFormulaView`；
- **多语法支持**：支持 `\frac{分子}{分母}` 分数排版、`x_{ij}` 下标、`a^{2}` 上标、希腊字母（$\alpha, \beta, \gamma, \pi$）及常用微积分符号。

### 6. 📱 深度自适应设计系统
- **严格的大屏边界哲学**：
  - **平板/大屏横屏**（`maxWidth >= 760dp && 横屏`）：自动展开侧边导航与内容双栏（Master-Detail Split View）工作台；
  - **手机/折叠屏竖屏**：严格收缩为单栏全屏，保障单手握持时的极简沉浸；
- **Monet 动态主题**：在 Android 12+ 上无缝融入系统壁纸取色（Material You），同时支持紫蓝、墨绿、暖橙等预设主题与纯净深色模式；
- **非等比 DPI 保护**：字号缩放解耦布局密度（`LocalDensity` 解耦），大字号模式下只放大文字，绝对不拉伸卡片尺寸或破坏边距。

### 7. 🔒 完整业务数据闭环
- **全链路自动级联**：导入题目自动累加至题库题量；答题记录即时同步至题库正确率、今日答题计数器与 SM-2 时间戳；
- **GitHub 风格热力图**：主页与统计页集成一整年的学习习惯点阵图（`StudyHeatmapView`）；
- **拼音首字母检索**：内置高效率 GB2312 汉字首字母转换器（`PinyinMatcher`），支持全拼、简拼搜索；
- **完全离线备份**：支持全量 SQLite 题库与做题历史导出为格式化 JSON 文件，并支持文件选择器完整无损导入恢复。

---

## 🏗️ 架构与技术栈

```text
com.example/
├── data/
│   ├── local/            # Room 3 数据库、实体、DAO、DataStore 用户偏好
│   │   ├── AppDatabase.kt
│   │   ├── AppDao.kt
│   │   ├── Entities.kt
│   │   └── UserPreferencesRepository.kt
│   ├── parser/           # 零依赖流式解析器与算法
│   │   ├── SimpleXlsxReader.kt   # 32KB 流式 Excel 读取
│   │   ├── SimpleDocxReader.kt   # 流式 Word 读取
│   │   ├── SmartImportEngine.kt  # 核心题库清洗引擎
│   │   └── PinyinMatcher.kt      # GB2312 汉字简拼检索
│   ├── scheduler/        # 科学记忆调度
│   │   └── Sm2Scheduler.kt       # SuperMemo 2 间隔复习算法
│   └── remote/           # AI 辅助接口
│       └── AiApiClient.kt        # DeepSeek / OpenAI 兼容协议客户端
├── ui/
│   ├── components/       # 基础设计组件
│   │   ├── AppleFloatingDock.kt    # 悬浮胶囊底栏
│   │   ├── SereneCommonComponents.kt# 顶栏/卡片/徽标/进度条
│   │   ├── SereneFormulaView.kt    # 原生 LaTeX 渲染器
│   │   ├── SereneMarkdownText.kt   # Markdown 文本解析
│   │   ├── SereneConfettiEffect.kt # Canvas 物理彩带发射器
│   │   ├── StudyHeatmapView.kt     # 提交习惯热力图
│   │   └── SereneIllustrations.kt  # 矢量空态与插画
│   ├── screens/          # 各业务功能屏
│   │   ├── HomeAndStatsScreens.kt  # 首页与统计看板
│   │   ├── LibraryScreens.kt       # 题库中心、章节与详情
│   │   ├── LibrarySearchScreen.kt  # 全局沉浸式搜索
│   │   ├── QuizScreens.kt          # 做题、答题卡、诊断与结果
│   │   ├── ImportScreens.kt        # 三步流式导入向导
│   │   ├── SystemScreens.kt        # 设置中心、AI 服务、备份管理
│   │   └── WrongBookPrintHelper.kt # 错题集 A4 打印与另存为 PDF
│   ├── theme/            # 主题定义
│   │   ├── Color.kt
│   │   ├── Theme.kt      # Monet 动态色彩融合
│   │   └── Type.kt       # Plus Jakarta Sans 与自适应字高
│   └── viewmodel/        # 单向数据流状态机
│       └── StudyViewModel.kt
└── MainActivity.kt       # 单 Activity 顶层导航调度与平板分栏控制器
```

---

## 🛠️ 构建与运行说明

### 开发环境要求
- **操作系统**：Windows 10/11, macOS, Linux
- **JDK**：Java 17 或 21（推荐使用 Adoptium / Eclipse Temurin JDK 17）
- **Android SDK**：Android 15/16 SDK (API 35/36)，最低支持 Android 7.0 (API 24)
- **Gradle**：9.3.1 (已包含在 Gradle Wrapper 中)

### 命令行编译安装

```bash
# 1. 克隆本仓库
git clone https://github.com/lllll081926i/ZhixuStudy.git
cd ZhixuStudy

# 2. 编译 Debug APK
./gradlew assembleDebug

# 3. 运行自动化单元测试
./gradlew testDebugUnitTest

# 4. 一键安装并启动（连接 adb 设备后）
./deploy.ps1
```

> **提示**：工程内置自适应 Keystore 签名机制。当不存在云端生产签名密钥时，Gradle 会自动回退使用内置 Debug 签名配置，无需任何额外配置即可在任何开发机器上一键构建安装。

---

## 📄 开源协议

本项目采用 [Apache License 2.0](LICENSE) 协议开源。
欢迎提出 Issue 与 Pull Request 共同完善这款宁静专注的学习工具。
