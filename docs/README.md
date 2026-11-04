# 知序 Study · Android 原生智能刷题 APP 完整产品文档

**版本**：V1.0 开发基线草案（2026-11-04）  
**目标**：Kotlin + Jetpack Compose + Material 3 原生 Android 应用  
**文档性质**：产品与研发交付基线；**不是**已实现 APK。  

## 下载后先看什么

1. `docs/00_阅读指南_决策摘要.md`：一页先看版，确定优先级和交付边界。
2. `docs/01_产品需求PRD.md`：业务目标、用户故事、P0/P1/P2 功能及流程。
3. `docs/02_信息架构与逐页规格.md`：21 个页面的导航结构、按钮入口、交互与状态。
4. `docs/03_视觉设计系统design.md`：完整 Material 3 Design System，配色、排版、插画、动画与无障碍。
5. `docs/04_智能导入与规则引擎.md`：文件类型、列映射、格式规则、审查/冲突/去重、事务。
6. `docs/05_答题考试复习与统计.md`：作答判分、会话状态机、错题复习、统计口径。
7. `docs/06_数据模型与备份协议.md`：Room 实体、版本快照、来源溯源、导出备份恢复。
8. `docs/07_技术架构与实现约束.md`：Kotlin/Compose 工程划分、SAF/Room/WorkManager、安全。
9. `docs/08_测试矩阵与验收标准.md`：可转成 issue 的 60+ 条验收用例。
10. `docs/09_迭代计划与开发任务.md`：M0–M7 路线和 DEV-xxx 任务清单。
11. `docs/10_资料来源与原型对照.md`：Android 官方依据、开源项目、HTML 原型局限。
12. `docs/11_待决策事项与风险清单.md`：决策日志、样本待验证点和安全风险。

## 可编辑总览

- `知序Study_完整产品设计与开发文档.docx`：42 页完整 Word 汇编版，包含全部专项文档正文，排版经渲染检查。
- `design.md`：方便 Android 工程根目录直接使用的设计系统副本。

## 配套资产

- `reference/interactive_prototype.html`：之前制作的 21 页面浏览器原型（可离线双击打开，供视觉和动线评审）。
- `reference/five_screens_preview.png`：五个代表性页面预览。
- `samples/questions.example.json`：规范化题库格式样例，合成测试数据。
- `samples/questions_import_sample.csv`：一份中文表头的 CSV，含**故意错误**答案 `A、Z`。
- `samples/import_rule_preset.example.json`：自定义规则预设示例。
- `知序Study_产品文档全书.md`：全部文档按逻辑顺序合并的可搜索 Markdown。

## 使用与开发次序

先以 P0 建立 **Room 数据/题型/会话**，接着跑通 **CSV/JSON/TXT 规则化导入与审查**，然后攻克 **XLSX 多 Sheet/公式缓存/内存**，最后完成复习、统计、备份和无障碍。来自用户的真实 Excel 样本将直接推动 `04` 和 `08` 的规则与金标更新。

**注意**：真实 Android 解析、Room 持久化、AI 连接和 APK 交付尚未实现；这里提供的是可执行开发规格与原型参考。若未来某个功能延期，生产 APP 中不可保留无法操作的假按钮。
