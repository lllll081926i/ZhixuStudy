# 合成测试样本说明

这些样本专为验证规范字段、答案映射与异常处理而构造，并非来自真实考试或用户 Excel。

- `questions.example.json`：3 道正常题（单选、多选、判断），用于 JSON 模型/判分回归。
- `questions_import_sample.csv`：3 道输入，其中前 2 道应为合法题，第 3 道含答案 `A、Z`，故意触发 `INVALID_ANSWER_REF`。
- `import_rule_preset.example.json`：字段映射、题号清理、答案拆分的示例规则；V1 正式规则引擎的 schemaVersion 与字段要求可能经实际开发校准。

不要将本目录的合成示例用于验证铁路专业知识正确性。
