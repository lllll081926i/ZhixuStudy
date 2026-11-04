# 知序 Study · Android 原生刷题应用 UI 交互原型

这是一个用于产品验收的**浏览器交互原型**，并不是已经编译的 Kotlin APK。

## 如何运行

- 最方便：直接打开交付的单文件 `知序Study_交互原型.html`。它内嵌 CSS、JavaScript 和矢量插画，无需网络和依赖。
- 开发阅读：本文件夹中分别提供 `index.html`、`style.css`、`app.js`、`design.md`，使用浏览器打开 `index.html` 即可。
- 在电脑上：左侧可直接打开全部 21 个页面；中间是 Android 手机模拟界面。
- 在手机上：页面按手机宽度自适应，主要通过底部四个导航入口进入其他流程。

## 推荐体验路线

1. 学习首页 → 导入题库 → 使用演示 Excel → 开始智能识别。
2. 识别与映射：修改字段映射，展开并切换预处理规则；可保存规则预设。
3. 预览与审查：检查一条无效答案 `A,Z`，打开编辑框改成 `A,C`；查看重复题与答案冲突。
4. 选择“跳过”“替换旧题”“保留新旧两个版本”等处理策略，确认导入。
5. 打开题库，查看已添加的题目；在“题库体检”查看跨题库重复项。
6. 学习首页 → 开始刷题 → 选择配置 → 答题 → 提交 → 解析 → 结果 → 复盘。
7. 查看复习、统计、历史、数据管理、设置、AI 服务等页面。

## 格式支持说明

| 类型 | 此 HTML 原型 | 计划中的 Android Kotlin 应用 |
| --- | --- | --- |
| CSV/TSV | 简单本地解析；支持含双引号的单元格和字段映射 | 生产级解析、编码和错误处理 |
| JSON | 支持题目数组或 `questions/items/data` 数组 | 按版本管理 JSON 结构 |
| TXT | 按空行区分题目；识别 A/B/C/D、答案和解析 | 更丰富的规则与置信度 |
| XLSX/XLS | **只演示操作流程，使用示例数据，并不真正读取内容** | 独立、低内存且可校验的 Excel 解析器 |
| DOCX/PDF | **只演示操作流程，并不真正提取文本** | 分阶段接入结构解析和 OCR |

数据存储于当前页面内存，刷新后会恢复演示数据。JSON 导出可以下载当前示例题目。完整备份、持久化、后台导入和 AI 网络连接均未在本 HTML 原型中实现；这些属于 Android 正式开发任务。

## 技术路线

Kotlin + Jetpack Compose + Material 3 / Material 3 Adaptive + Room + DataStore + Hilt + Coroutines/Flow + WorkManager + SAF。

当前页面是用于验证视觉设计、页面信息结构、触控热区和用户操作路径的前端原型。请以 `design.md` 中的状态、验收规则和导入流程作为后续 Kotlin 实现的产品契约。

## 项目扁平化结构与页面索引

项目已完成目录扁平化整理，所有子文件夹均已拆除，页面原型源码与截图直接归并于根目录下：

### 1. 核心应用文件
- `index.html`：多页面集成预览入口（含左侧导航与右侧说明）。
- `app.js`：原型交互与状态逻辑。
- `style.css`：手机模拟器与主题样式。
- `design.md`：全局设计规范与验收标准。
- `serene_study_1_DESIGN.md` / `serene_study_2_DESIGN.md`：设计系统 Token 规范定义。

### 2. 页面原型与截图清单（共 29 组）

| 编号 | 页面名称 | 页面源码 | 效果截图 |
| :--- | :--- | :--- | :--- |
| study_1 | 知序 Study - 学习 | `study_1_code.html` | `study_1_screen.png` |
| study_2 | Serene Study (知序) - 章节答题与即时解析 | `study_2_code.html` | `study_2_screen.png` |
| study_3 | 知序 Study - 智能导入预览与审查 | `study_3_code.html` | `study_3_screen.png` |
| study_4 | 知序 Study - 题库体检与审查 | `study_4_code.html` | `study_4_screen.png` |
| study_5 | 知序 Study - 编辑题目 | `study_5_code.html` | `study_5_screen.png` |
| study_6 | 知序 Study - 编辑题目 | `study_6_code.html` | `study_6_screen.png` |
| study_7 | 知序 Study - 刷题模式 | `study_7_code.html` | `study_7_screen.png` |
| study_8 | 知序 Study - 多选题作答 | `study_8_code.html` | `study_8_screen.png` |
| study_9 | 知序 Study - 刷题作答 | `study_9_code.html` | `study_9_screen.png` |
| study_10 | 知序 Study - 刷题训练 | `study_10_code.html` | `study_10_screen.png` |
| study_11 | 设置 - 知序 Study | `study_11_code.html` | `study_11_screen.png` |
| study_12 | 知序 Study · 题库 | `study_12_code.html` | `study_12_screen.png` |
| study_13 | 知序 Study - 题目列表与多维检索 | `study_13_code.html` | `study_13_screen.png` |
| study_14 | 题库详情 - 知序 Study | `study_14_code.html` | `study_14_screen.png` |
| study_15 | 知序 Study - 复习中心 | `study_15_code.html` | `study_15_screen.png` |
| study_16 | 知序 Study · 设置中心 | `study_16_code.html` | `study_16_screen.png` |
| study_17 | 知序 Study - 平板与大屏工作台 | `study_17_code.html` | `study_17_screen.png` |
| study_18 | 知序 Study - 全库体检与答案冲突对比审查工作台 | `study_18_code.html` | `study_18_screen.png` |
| study_19 | 知序 Study - 题库管理 | `study_19_code.html` | `study_19_screen.png` |
| study_20 | 知序 Study - 复习与学习统计 | `study_20_code.html` | `study_20_screen.png` |
| study_21 | 知序 Study - 题库详情 | `study_21_code.html` | `study_21_screen.png` |
| study_22 | 知序 Study - 题目编辑工作台 | `study_22_code.html` | `study_22_screen.png` |
| study_23 | 知序 Study - 智能导入审查与字段映射全景工作台 | `study_23_code.html` | `study_23_screen.png` |
| study_24 | 知序 Study - 平板大屏单选题刷题与深度解析联动工作台 | `study_24_code.html` | `study_24_screen.png` |
| study_25 | 知序 Study - 多选题作答工作台 | `study_25_code.html` | `study_25_screen.png` |
| study_26 | 知序 Study · 平板大屏综合计算工作台 | `study_26_code.html` | `study_26_screen.png` |
| study_27 | 知序 Study · 极速判断题作答工作台 | `study_27_code.html` | `study_27_screen.png` |
| study_ai_1 | 知序 Study · AI 服务与模型设置 | `study_ai_1_code.html` | `study_ai_1_screen.png` |
| study_ai_2 | 知序 Study - 系统设置与 AI 服务中心 | `study_ai_2_code.html` | `study_ai_2_screen.png` |

