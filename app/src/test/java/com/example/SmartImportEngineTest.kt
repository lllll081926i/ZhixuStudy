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
}
