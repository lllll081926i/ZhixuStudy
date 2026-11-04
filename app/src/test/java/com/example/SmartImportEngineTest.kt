package com.example

import com.example.data.local.QuestionEntity
import com.example.data.parser.ColumnMappingConfig
import com.example.data.parser.PreprocessRulesConfig
import com.example.data.parser.RawImportRow
import com.example.data.parser.SmartImportEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SmartImportEngineTest {

    @Test
    fun testDemoExcelRowsStructure() {
        val (headers, rows) = SmartImportEngine.getDemoExcelRows()
        assertEquals(10, headers.size)
        assertEquals(8, rows.size)
    }

    @Test
    fun testEvaluateImportRowsValidation() {
        val (_, rows) = SmartImportEngine.getDemoExcelRows()
        val mapping = ColumnMappingConfig()
        val rules = PreprocessRulesConfig()

        // 构造已有题库中的对比题目
        val existingQuestions = listOf(
            QuestionEntity(
                id = "Q001",
                bank = "《铁路运输组织基础》",
                chapter = "第二章",
                type = "single",
                stem = "铁路运输组织中列车运行图的基本作用是什么？",
                optionsJson = "[\"A. 全路组织列车运行的基础，综合性作业规划纲领\"]",
                answersJson = "[\"A\"]",
                explanation = "已有题库解析"
            ),
            QuestionEntity(
                id = "Q004",
                bank = "《铁路运输组织基础》",
                chapter = "第一章",
                type = "single",
                stem = "车站接发列车工作必须首先保证什么？",
                optionsJson = "[\"A. 行车安全与列车按运行图正点运行\"]",
                answersJson = "[\"A\"]",
                explanation = "已有题库解析"
            )
        )

        val evaluated = SmartImportEngine.evaluateImportRows(
            rows = rows,
            mapping = mapping,
            rules = rules,
            existingQuestions = existingQuestions
        )

        assertEquals(8, evaluated.size)

        // 检查第1题（判断题正确归一化）
        val row1 = evaluated[0]
        assertEquals("judge", row1.type)
        assertEquals(listOf("正确"), row1.answer)
        assertEquals("valid", row1.status)

        // 检查第2题（精确重复）
        val row2 = evaluated[1]
        assertEquals("duplicate", row2.status)
        assertEquals("Q001", row2.existingQuestion?.id)

        // 检查第4题（答案冲突：已有为 A，导入为 C）
        val row4 = evaluated[3]
        assertEquals("conflict", row4.status)
        assertEquals("Q004", row4.existingQuestion?.id)

        // 检查第5题（非法选项 Z）
        val row5 = evaluated[4]
        assertEquals("invalid", row5.status)
        assertTrue("应包含关于非法选项Z的错误提示", row5.statusMessage.contains("Z"))
    }

    @Test
    fun testSerialRemovalRule() {
        val mapping = ColumnMappingConfig()
        val rulesWithSerial = PreprocessRulesConfig(removeSerial = true)
        val rulesWithoutSerial = PreprocessRulesConfig(removeSerial = false)

        val rawRow = RawImportRow(
            rowNumber = 1,
            values = mapOf(
                "题干" to "12. 这是一个测试题目题干",
                "题型" to "单选题",
                "选项A" to "选项内容A",
                "选项B" to "选项内容B",
                "答案" to "A",
                "解析" to ""
            )
        )

        val evalRemoved = SmartImportEngine.evaluateImportRows(listOf(rawRow), mapping, rulesWithSerial, emptyList())
        assertEquals("这是一个测试题目题干", evalRemoved[0].stem)

        val evalKept = SmartImportEngine.evaluateImportRows(listOf(rawRow), mapping, rulesWithoutSerial, emptyList())
        assertEquals("12. 这是一个测试题目题干", evalKept[0].stem)
    }

    @Test
    fun testAnkiImportParsing() {
        // 1. 测试 Anki Cloze 完形填空卡片
        val ankiClozeText = "单线平行运行图周期包含 {{c1::纯运行时分}} 与 {{c2::车站间隔时间}}。"
        val (_, rows) = SmartImportEngine.parseAnkiCards(ankiClozeText)
        assertEquals(1, rows.size)
        val row = rows[0]
        assertEquals("填空题", row.values["题型"])
        assertEquals("单线平行运行图周期包含 [ 空位 1 ] 与 [ 空位 2 ]。", row.values["题干"])
        assertEquals("纯运行时分, 车站间隔时间", row.values["答案"])

        // 2. 测试 Anki Basic Tab 分隔卡片与 HTML 清洗
        val ankiBasicText = "<b>格林希尔治模型</b>中最大通行能力的密度是多少？\tKm = 0.5 Kj\t交通流理论"
        val (_, basicRows) = SmartImportEngine.parseAnkiCards(ankiBasicText)
        assertEquals(1, basicRows.size)
        assertEquals("格林希尔治模型中最大通行能力的密度是多少？", basicRows[0].values["题干"])
        assertEquals("Km = 0.5 Kj", basicRows[0].values["答案"])
        assertEquals("交通流理论", basicRows[0].values["章节"])
    }

    @Test
    fun testEmbeddedBracketAnswerAndInlineOptions() {
        val mapping = ColumnMappingConfig()
        val rules = PreprocessRulesConfig(removeSerial = true)

        // 测试从题干括号中剥离答案与内联选项提取
        val rawRow = RawImportRow(
            rowNumber = 1,
            values = mapOf(
                "题干" to "1. 铁路车站接发列车必须首先保证什么？（ A ） A. 行车安全 B. 装卸速度 C. 进路调整",
                "题型" to "",
                "选项A" to "",
                "选项B" to "",
                "选项C" to "",
                "选项D" to "",
                "答案" to "", // 空答案，期待引擎从题干括号中提取
                "解析" to "安全第一"
            )
        )

        val evaluated = SmartImportEngine.evaluateImportRows(listOf(rawRow), mapping, rules, emptyList())
        assertEquals(1, evaluated.size)
        val item = evaluated[0]
        assertEquals("铁路车站接发列车必须首先保证什么？（   ）", item.stem)
        assertEquals(listOf("A"), item.answer)
        assertEquals("single", item.type)
        assertEquals(3, item.options.size)
    }
}
