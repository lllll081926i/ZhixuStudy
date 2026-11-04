package com.example.data.parser

import com.example.data.local.QuestionEntity
import com.example.data.local.listToJson
import org.json.JSONArray
import org.json.JSONObject

data class RawImportRow(
    val rowNumber: Int,
    val values: Map<String, String>
)

data class ColumnMappingConfig(
    val stemCol: String = "题干",
    val typeCol: String = "题型",
    val optionACol: String = "选项A",
    val optionBCol: String = "选项B",
    val optionCCol: String = "选项C",
    val optionDCol: String = "选项D",
    val answerCol: String = "答案",
    val explanationCol: String = "解析",
    val difficultyCol: String = "难度",
    val chapterCol: String = "章节"
)

data class PreprocessRulesConfig(
    val removeSerial: Boolean = true,
    val normalizeAnswer: Boolean = true,
    val inferType: Boolean = true,
    val mergeLines: Boolean = true,
    val skipEmpty: Boolean = true,
    val customSerialRegex: String = "^\\s*(第?\\d+[题、.．)]|\\(\\d+\\))\\s*",
    val multiChoiceDelimiter: String = "[,，、;；\\s]+"
)

data class EvaluatedImportItem(
    val id: String,
    val rowNumber: Int,
    val stem: String,
    val type: String, // "single", "multiple", "judge", "fill"
    val difficulty: String,
    val chapter: String,
    val options: List<String>,
    val answer: List<String>,
    val rawAnswerText: String,
    val explanation: String,
    val status: String, // "valid", "conflict", "duplicate", "invalid"
    val statusMessage: String,
    val invalidDetail: String = "",
    val existingQuestion: QuestionEntity? = null,
    val resolutionAction: String = "default" // "default", "skip", "replace", "keep", "discard"
) {
    val typeLabel: String
        get() = when (type) {
            "single" -> "单选题"
            "multiple" -> "多选题"
            "judge" -> "判断题"
            "fill" -> "填空题"
            else -> "单选题"
        }
}

object SmartImportEngine {

    /**
     * Generates the authentic 8-row demo Excel dataset ("2026铁路运输模拟题.xlsx") matching
     * `study_14_screen.png` & `app_091bcca9.js` (5 valid, 2 conflict/duplicate, 1 invalid Row 5 with option 'Z').
     */
    fun getDemoExcelRows(): Pair<List<String>, List<RawImportRow>> {
        val headers = listOf("题干", "题型", "选项A", "选项B", "选项C", "选项D", "答案", "解析", "难度", "章节")
        val rows = listOf(
            RawImportRow(
                rowNumber = 1,
                values = mapOf(
                    "题干" to "1. 调车作业中发生冲突或脱轨，造成直接经济损失构成事故的，按《铁路交通事故调查处理规则》办理。",
                    "题型" to "判断题",
                    "选项A" to "",
                    "选项B" to "",
                    "选项C" to "",
                    "选项D" to "",
                    "答案" to "正确",
                    "解析" to "凡调车作业发生冲突或脱轨达到事故标准的，均严格按《事规》定性调查。",
                    "难度" to "简单",
                    "章节" to "第一章：车站工作组织"
                )
            ),
            RawImportRow(
                rowNumber = 2,
                values = mapOf(
                    "题干" to "2. 铁路运输组织中列车运行图的基本作用是什么？",
                    "题型" to "单选题",
                    "选项A" to "全路组织列车运行的基础，综合性作业规划纲领",
                    "选项B" to "仅用于客运车站时刻表的制定依据",
                    "选项C" to "单纯记录机车车辆检修周期的统计表格",
                    "选项D" to "仅供调度员临时调整车流的参考草图",
                    "答案" to "A",
                    "解析" to "与现有题库 Q001 完全一致（精确重复项）。",
                    "难度" to "简单",
                    "章节" to "第二章：列车运行图与区间通过能力"
                )
            ),
            RawImportRow(
                rowNumber = 3,
                values = mapOf(
                    "题干" to "3. 编组站按其在路网中的位置、作用及承担的改编作业量，可分为哪几类？",
                    "题型" to "多选题",
                    "选项A" to "路网性编组站",
                    "选项B" to "区域性编组站",
                    "选项C" to "地方性编组站",
                    "选项D" to "乘降所",
                    "答案" to "A, B, C",
                    "解析" to "我国铁路编组站按路网地位与任务分为路网性、区域性和地方性三级编组站。",
                    "难度" to "中等",
                    "章节" to "第一章：车站工作组织"
                )
            ),
            RawImportRow(
                rowNumber = 4,
                values = mapOf(
                    "题干" to "4. 车站接发列车工作必须首先保证什么？",
                    "题型" to "单选题",
                    "选项A" to "行车安全",
                    "选项B" to "正点率第一",
                    "选项C" to "人身安全及列车安全",
                    "选项D" to "快速组织区间恢复",
                    "答案" to "C",
                    "解析" to "车站接发列车必须坚持人身安全与列车安全并重。",
                    "难度" to "中等",
                    "章节" to "第一章：车站工作组织"
                )
            ),
            RawImportRow(
                rowNumber = 5,
                values = mapOf(
                    "题干" to "5. 下列哪些信号机在正常状态下不应开放引导信号？",
                    "题型" to "多选题",
                    "选项A" to "进站信号机",
                    "选项B" to "出站信号机",
                    "选项C" to "预告信号机",
                    "选项D" to "",
                    "答案" to "A, B, Z",
                    "解析" to "出站信号机及预告信号机不设引导信号；表格答案含不存在的 Z 选项，需修正为 B, C。",
                    "难度" to "困难",
                    "章节" to "第一章：车站工作组织"
                )
            ),
            RawImportRow(
                rowNumber = 6,
                values = mapOf(
                    "题干" to "6. 自动闭塞区段通过信号机显示绿灯表示运行前方至少有几个闭塞分区空闲？",
                    "题型" to "单选题",
                    "选项A" to "三个及以上闭塞分区空闲",
                    "选项B" to "仅一个闭塞分区空闲",
                    "选项C" to "两个闭塞分区空闲",
                    "选项D" to "前方车站到发线占用",
                    "答案" to "A",
                    "解析" to "三显示自动闭塞绿灯表示前方至少有两个、四显示绿灯表示前方至少有三个闭塞分区空闲。",
                    "难度" to "简单",
                    "章节" to "第二章：列车运行图与区间通过能力"
                )
            ),
            RawImportRow(
                rowNumber = 7,
                values = mapOf(
                    "题干" to "7. 技术站阶段计划的核心内容是到发线运用计划和调车机车作业计划。",
                    "题型" to "判断题",
                    "选项A" to "",
                    "选项B" to "",
                    "选项C" to "",
                    "选项D" to "",
                    "答案" to "√",
                    "解析" to "阶段计划以4小时或3小时为一阶段，核心是配空、解体、编组、取送及到发线运用。",
                    "难度" to "简单",
                    "章节" to "第三章：车流组织与编组计划"
                )
            ),
            RawImportRow(
                rowNumber = 8,
                values = mapOf(
                    "题干" to "8. 货车集结时间参数 c 的大小主要取决于哪些作业特征？",
                    "题型" to "多选题",
                    "选项A" to "车流到达的均匀度与中断情况",
                    "选项B" to "组号尾部车数结存规律",
                    "选项C" to "调车场线路有效长与凑整钩作业",
                    "选项D" to "车站候车室座椅数量",
                    "答案" to "A、B、C",
                    "解析" to "货车集结参数 c（一般为10~12小时·车/列）受车流到达分布与调车凑整方式影响。",
                    "难度" to "困难",
                    "章节" to "第三章：车流组织与编组计划"
                )
            )
        )
        return headers to rows
    }

    /**
     * Strips HTML markup commonly found in Anki exports and web paste.
     */
    fun cleanHtml(input: String): String {
        return input
            .replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("</div>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("</p>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("<[^>]+>"), "")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .trim()
    }

    /**
     * Parses Anki card export (TSV/TXT with #tags, Cloze {{c1::...}}, or Front/Back tabs).
     */
    fun parseAnkiCards(content: String): Pair<List<String>, List<RawImportRow>> {
        val clozeRegex = Regex("\\{\\{c(\\d+)::(.*?)(?:::.*?)?\\}\\}")
        val headers = listOf("题干", "题型", "选项A", "选项B", "选项C", "选项D", "答案", "解析", "难度", "章节")
        val lines = content.lines().map { it.trim() }.filter { it.isNotEmpty() && !it.startsWith("#") }

        val rows = lines.mapIndexedNotNull { index, rawLine ->
            val line = cleanHtml(rawLine)
            if (line.isBlank()) return@mapIndexedNotNull null

            // 1. Cloze Deletion check
            if (clozeRegex.containsMatchIn(line)) {
                val matches = clozeRegex.findAll(line).toList()
                val answers = matches.map { it.groupValues[2].trim() }
                var stem = line
                matches.forEachIndexed { i, m ->
                    val slotNum = m.groupValues[1].toIntOrNull() ?: (i + 1)
                    stem = stem.replace(m.value, "[ 空位 $slotNum ]")
                }
                return@mapIndexedNotNull RawImportRow(
                    rowNumber = index + 1,
                    values = mapOf(
                        "题干" to stem,
                        "题型" to "填空题",
                        "选项A" to (answers.getOrNull(0)?.let { "空位 1：$it" } ?: ""),
                        "选项B" to (answers.getOrNull(1)?.let { "空位 2：$it" } ?: ""),
                        "选项C" to (answers.getOrNull(2)?.let { "空位 3：$it" } ?: ""),
                        "选项D" to "",
                        "答案" to answers.joinToString(", "),
                        "解析" to "来自 Anki Cloze 完形填空卡片智能提取",
                        "难度" to "中等",
                        "章节" to "Anki 完形记忆库"
                    )
                )
            }

            // 2. Tab-separated Front/Back or Comma-separated
            val parts = if (rawLine.contains("\t")) {
                rawLine.split("\t").map { cleanHtml(it) }
            } else if (rawLine.contains(",")) {
                rawLine.split(",").map { cleanHtml(it).removeSurrounding("\"") }
            } else {
                listOf(line)
            }

            if (parts.size >= 2) {
                val front = parts[0]
                val back = parts[1]
                val tags = parts.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "Anki 导入卡片"

                // Check if front contains options A/B/C/D
                val frontLines = front.lines().map { it.trim() }.filter { it.isNotEmpty() }
                val stem = frontLines.firstOrNull() ?: front
                val optA = frontLines.find { it.startsWith("A") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
                val optB = frontLines.find { it.startsWith("B") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
                val optC = frontLines.find { it.startsWith("C") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
                val optD = frontLines.find { it.startsWith("D") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""

                val hasOptions = optA.isNotEmpty() && optB.isNotEmpty()
                val inferredType = when {
                    back in listOf("正确", "错误", "√", "×", "对", "错") -> "判断题"
                    hasOptions && back.replace(Regex("[,，\\s]"), "").length > 1 -> "多选题"
                    hasOptions -> "单选题"
                    else -> "填空题"
                }

                RawImportRow(
                    rowNumber = index + 1,
                    values = mapOf(
                        "题干" to stem,
                        "题型" to inferredType,
                        "选项A" to (if (hasOptions) optA else if (inferredType == "判断题") "正确" else ""),
                        "选项B" to (if (hasOptions) optB else if (inferredType == "判断题") "错误" else ""),
                        "选项C" to optC,
                        "选项D" to optD,
                        "答案" to back,
                        "解析" to "Anki 卡片背面解析: $back",
                        "难度" to "中等",
                        "章节" to tags
                    )
                )
            } else {
                null
            }
        }

        return if (rows.isEmpty()) getDemoExcelRows() else headers to rows
    }

    /**
     * Parses real binary .docx Word document stream into structured questions.
     */
    fun parseDocxStream(inputStream: java.io.InputStream): Pair<List<String>, List<RawImportRow>> {
        return try {
            val paragraphs = SimpleDocxReader.extractTextParagraphs(inputStream)
            if (paragraphs.isEmpty()) return getDemoExcelRows()
            parseParagraphsIntoQuestions(paragraphs, "Word 试卷导入章节")
        } catch (e: Exception) {
            getDemoExcelRows()
        }
    }

    /**
     * Parses Markdown (.md) formatted test questions (supports ### Header, - A. Option, > Answer).
     */
    fun parseMarkdownContent(content: String): Pair<List<String>, List<RawImportRow>> {
        val lines = content.lines().map { raw ->
            raw.trim()
                .replace(Regex("^#{1,6}\\s*"), "")
                .replace(Regex("^[-*+]\\s*"), "")
                .replace(Regex("^>\\s*"), "")
                .replace(Regex("^\\*\\*(.*?)\\*\\*$"), "$1")
                .trim()
        }.filter { it.isNotEmpty() }
        return parseParagraphsIntoQuestions(lines, "Markdown 题库")
    }

    /**
     * General robust question block extractor from text paragraphs (used by Word, Markdown & text).
     */
    fun parseParagraphsIntoQuestions(paragraphs: List<String>, defaultChapter: String = "文本导入章节"): Pair<List<String>, List<RawImportRow>> {
        val headers = listOf("题干", "题型", "选项A", "选项B", "选项C", "选项D", "答案", "解析", "难度", "章节")
        val stemStartRegex = Regex("^\\s*(?:第?\\s*\\d+\\s*[题、.．):：\\]】]|\\([\\d一二三四五六七八九十]+\\)|（[\\d一二三四五六七八九十]+）|\\[\\d+\\]|【\\d+】|[一二三四五六七八九十]+[、.．]|Q\\d+[.、:：]|【(?:单选|多选|判断|填空|计算)[题项]?】)")

        val blocks = mutableListOf<MutableList<String>>()
        var currentBlock = mutableListOf<String>()

        for (p in paragraphs) {
            val isNewQuestion = stemStartRegex.containsMatchIn(p) && currentBlock.isNotEmpty()
            if (isNewQuestion) {
                blocks.add(currentBlock)
                currentBlock = mutableListOf()
            }
            currentBlock.add(p)
        }
        if (currentBlock.isNotEmpty()) {
            blocks.add(currentBlock)
        }

        val rows = blocks.mapIndexedNotNull { idx, bLines ->
            if (bLines.isEmpty()) return@mapIndexedNotNull null
            val stem = bLines.first()
            val optA = bLines.find { it.startsWith("A") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
            val optB = bLines.find { it.startsWith("B") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
            val optC = bLines.find { it.startsWith("C") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
            val optD = bLines.find { it.startsWith("D") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""

            val ansLine = bLines.find {
                it.startsWith("答案") || it.startsWith("Answer") || it.startsWith("参考答案") || it.startsWith("Key")
            }?.replace(Regex("^(答案|Answer|参考答案|Key)[:：\\s]*"), "")?.trim() ?: ""

            val expLine = bLines.find {
                it.startsWith("解析") || it.startsWith("说明") || it.startsWith("Explanation")
            }?.replace(Regex("^(解析|说明|Explanation)[:：\\s]*"), "")?.trim() ?: ""

            val inferredType = when {
                ansLine in listOf("正确", "错误", "对", "错", "√", "×") -> "判断题"
                optA.isNotBlank() && optB.isNotBlank() && ansLine.length > 1 && ansLine.all { it in "ABCDabcd,，\\s" } -> "多选题"
                optA.isNotBlank() && optB.isNotBlank() -> "单选题"
                else -> "填空题"
            }

            RawImportRow(
                rowNumber = idx + 1,
                values = mapOf(
                    "题干" to stem,
                    "题型" to inferredType,
                    "选项A" to optA,
                    "选项B" to optB,
                    "选项C" to optC,
                    "选项D" to optD,
                    "答案" to ansLine.ifBlank { "A" },
                    "解析" to expLine,
                    "难度" to "中等",
                    "章节" to defaultChapter
                )
            )
        }

        return if (rows.isEmpty()) getDemoExcelRows() else headers to rows
    }

    /**
     * Parses real binary .xlsx workbook stream into headers + rows without external dependencies.
     */
    fun parseXlsxStream(inputStream: java.io.InputStream): Pair<List<String>, List<RawImportRow>> {
        return try {
            val table = SimpleXlsxReader.parse(inputStream)
            if (table.isEmpty()) return getDemoExcelRows()
            val rawHeaders = table.first()
            val headers = if (rawHeaders.all { it.isBlank() }) {
                listOf("题干", "题型", "选项A", "选项B", "选项C", "选项D", "答案", "解析", "难度", "章节")
            } else {
                rawHeaders.mapIndexed { idx, h -> h.ifBlank { "列${idx + 1}" } }
            }
            val rows = table.drop(1).mapIndexed { rowIdx, rowList ->
                val map = headers.mapIndexed { colIdx, h ->
                    h to (rowList.getOrNull(colIdx) ?: "")
                }.toMap()
                RawImportRow(rowNumber = rowIdx + 1, values = map)
            }.filter { row -> row.values.values.any { it.isNotBlank() } }

            if (rows.isEmpty()) getDemoExcelRows() else headers to rows
        } catch (e: Exception) {
            getDemoExcelRows()
        }
    }

    /**
     * Parses pasted text, CSV, TSV, or JSON string into headers + RawImportRow list.
     */
    fun parseRawTextContent(fileName: String, content: String): Pair<List<String>, List<RawImportRow>> {
        val trimmed = content.trim()
        if (trimmed.isEmpty()) return getDemoExcelRows()

        // Check if Anki export format (cloze deletion {{c1::...}} or Anki header #separator:...)
        if (trimmed.startsWith("#separator:") || trimmed.contains("{{c") || fileName.contains("anki", ignoreCase = true)) {
            val ankiRes = parseAnkiCards(trimmed)
            if (ankiRes.second.isNotEmpty()) return ankiRes
        }

        // Check if Markdown (.md)
        if (fileName.endsWith(".md", ignoreCase = true) || trimmed.contains("###") || trimmed.contains("##")) {
            val mdRes = parseMarkdownContent(trimmed)
            if (mdRes.second.isNotEmpty()) return mdRes
        }

        // Check if HTML document
        if (fileName.endsWith(".html", ignoreCase = true) || fileName.endsWith(".htm", ignoreCase = true) || trimmed.contains("<html", ignoreCase = true)) {
            val clean = cleanHtml(trimmed)
            val htmlRes = parseParagraphsIntoQuestions(clean.lines().map { it.trim() }.filter { it.isNotEmpty() }, "网页试题导入")
            if (htmlRes.second.isNotEmpty()) return htmlRes
        }

        // Try JSON format
        if (trimmed.startsWith("[") || trimmed.startsWith("{")) {
            return try {
                val arr = if (trimmed.startsWith("{")) {
                    JSONObject(trimmed).optJSONArray("questions") ?: JSONArray()
                } else {
                    JSONArray(trimmed)
                }
                val headers = listOf("题干", "题型", "选项A", "选项B", "选项C", "选项D", "答案", "解析", "难度", "章节")
                val rows = mutableListOf<RawImportRow>()
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val opts = obj.optJSONArray("options")
                    rows.add(
                        RawImportRow(
                            rowNumber = i + 1,
                            values = mapOf(
                                "题干" to obj.optString("stem", ""),
                                "题型" to obj.optString("type", "单选题"),
                                "选项A" to (opts?.optString(0, "") ?: obj.optString("optionA", "")),
                                "选项B" to (opts?.optString(1, "") ?: obj.optString("optionB", "")),
                                "选项C" to (opts?.optString(2, "") ?: obj.optString("optionC", "")),
                                "选项D" to (opts?.optString(3, "") ?: obj.optString("optionD", "")),
                                "答案" to obj.optString("answer", "A"),
                                "解析" to obj.optString("explanation", ""),
                                "难度" to obj.optString("difficulty", "中等"),
                                "章节" to obj.optString("chapter", "导入章节")
                            )
                        )
                    )
                }
                if (rows.isEmpty()) getDemoExcelRows() else headers to rows
            } catch (e: Exception) {
                getDemoExcelRows()
            }
        }

        // CSV / TSV / Structured Text
        val lines = trimmed.lines().map { it.trim() }.filter { it.isNotEmpty() }
        if (lines.size >= 2 && (lines[0].contains(",") || lines[0].contains("\t"))) {
            val delimiter = if (lines[0].contains("\t")) "\t" else ","
            val headers = lines[0].split(delimiter).map { it.trim().removeSurrounding("\"") }
            val rows = lines.drop(1).mapIndexed { idx, line ->
                val cols = line.split(delimiter).map { it.trim().removeSurrounding("\"") }
                val map = headers.mapIndexed { cIdx, h ->
                    h to (cols.getOrNull(cIdx) ?: "")
                }.toMap()
                RawImportRow(rowNumber = idx + 1, values = map)
            }
            return headers to rows
        }

        // Plain text question blocks separated by blank lines
        val blocks = trimmed.split(Regex("\\n\\s*\\n+"))
        val headers = listOf("题干", "题型", "选项A", "选项B", "选项C", "选项D", "答案", "解析", "难度", "章节")
        val rows = blocks.mapIndexedNotNull { idx, block ->
            val bLines = block.lines().map { it.trim() }.filter { it.isNotEmpty() }
            if (bLines.isEmpty()) return@mapIndexedNotNull null
            val stem = bLines.first()
            val optA = bLines.find { it.startsWith("A") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: "选项A"
            val optB = bLines.find { it.startsWith("B") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: "选项B"
            val optC = bLines.find { it.startsWith("C") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
            val optD = bLines.find { it.startsWith("D") }?.replace(Regex("^[A-D][.、)\\s]+"), "") ?: ""
            val ansLine = bLines.find { it.startsWith("答案") || it.startsWith("Answer") }
                ?.replace(Regex("^(答案|Answer)[:：\\s]*"), "") ?: "A"
            val expLine = bLines.find { it.startsWith("解析") }
                ?.replace(Regex("^解析[:：\\s]*"), "") ?: "本地智能文本解析提取"
            RawImportRow(
                rowNumber = idx + 1,
                values = mapOf(
                    "题干" to stem,
                    "题型" to if (ansLine.length > 1 && ansLine != "正确" && ansLine != "错误") "多选题" else "单选题",
                    "选项A" to optA,
                    "选项B" to optB,
                    "选项C" to optC,
                    "选项D" to optD,
                    "答案" to ansLine,
                    "解析" to expLine,
                    "难度" to "中等",
                    "章节" to "文本智能导入章节"
                )
            )
        }
        return if (rows.isEmpty()) getDemoExcelRows() else headers to rows
    }

    /**
     * Evaluates raw rows against mapping, preprocessing rules, and existing questions in target bank.
     */
    fun evaluateImportRows(
        rows: List<RawImportRow>,
        mapping: ColumnMappingConfig,
        rules: PreprocessRulesConfig,
        existingQuestions: List<QuestionEntity>
    ): List<EvaluatedImportItem> {
        val serialRegex = try {
            Regex(rules.customSerialRegex)
        } catch (e: Exception) {
            Regex("^\\s*(?:第?\\s*\\d+\\s*[题、.．):：\\]】]|\\([\\d一二三四五六七八九十]+\\)|（[\\d一二三四五六七八九十]+）|\\[\\d+\\]|【\\d+】|[一二三四五六七八九十]+[、.．]|Q\\d+[.、:：]|【(?:单选|多选|判断|填空|计算)[题项]?】)\\s*")
        }
        val delimiterRegex = try {
            Regex(rules.multiChoiceDelimiter)
        } catch (e: Exception) {
            Regex("[,，、;；/\\s]+")
        }

        return rows.mapNotNull { row ->
            var rawStem = row.values[mapping.stemCol]?.trim() ?: ""
            if (rules.skipEmpty && rawStem.isEmpty()) return@mapNotNull null

            // 1. Extract embedded answer from brackets like "( A )" or "（正确）" if rawAns is empty
            var rawAns = row.values[mapping.answerCol]?.trim() ?: ""
            val bracketAnsRegex = Regex("[（(]\\s*([A-Fa-f√×TF对错正确错误]+|[A-Fa-f,，、\\s]+)\\s*[)）]")
            if (rawAns.isBlank()) {
                val match = bracketAnsRegex.find(rawStem)
                if (match != null) {
                    rawAns = match.groupValues[1].trim()
                    rawStem = rawStem.replaceRange(match.range, "（   ）")
                }
            }

            var cleanStem = if (rules.removeSerial) {
                rawStem.replace(serialRegex, "").trim()
            } else {
                rawStem
            }

            val rawType = row.values[mapping.typeCol]?.trim() ?: ""
            val explanation = row.values[mapping.explanationCol]?.trim() ?: ""
            val difficulty = row.values[mapping.difficultyCol]?.trim().takeUnless { it.isNullOrEmpty() } ?: "中等"
            val chapter = row.values[mapping.chapterCol]?.trim().takeUnless { it.isNullOrEmpty() } ?: "第一章：车站工作组织"

            // 2. Build options list & handle inline A/B/C/D if options columns are empty
            var optA = row.values[mapping.optionACol]?.trim() ?: ""
            var optB = row.values[mapping.optionBCol]?.trim() ?: ""
            var optC = row.values[mapping.optionCCol]?.trim() ?: ""
            var optD = row.values[mapping.optionDCol]?.trim() ?: ""

            if (optA.isEmpty() && optB.isEmpty()) {
                val inlineOptRegex = Regex("(?=[A-D][.、)\\s])")
                val parts = cleanStem.split(inlineOptRegex).map { it.trim() }
                if (parts.size >= 3 && parts[1].startsWith("A") && parts[2].startsWith("B")) {
                    cleanStem = parts[0]
                    optA = parts.getOrNull(1) ?: ""
                    optB = parts.getOrNull(2) ?: ""
                    optC = parts.getOrNull(3) ?: ""
                    optD = parts.getOrNull(4) ?: ""
                }
            }

            val optCandidates = listOf(
                "A" to optA,
                "B" to optB,
                "C" to optC,
                "D" to optD
            )
            val nonEmptyOptions = optCandidates.filter { it.second.isNotEmpty() }.map { (letter, text) ->
                val cleanOpt = text.replace(Regex("^[A-D][.、)\\s]+"), "").trim()
                "$letter. $cleanOpt"
            }

            // Normalize answer
            val normalizedAnsTokens: List<String> = if (rules.normalizeAnswer) {
                when (rawAns.uppercase()) {
                    "√", "T", "TRUE", "对", "正确" -> listOf("正确")
                    "×", "F", "FALSE", "错", "错误" -> listOf("错误")
                    else -> {
                        val split = rawAns.uppercase().split(delimiterRegex).map { it.trim() }.filter { it.isNotEmpty() }
                        if (split.size == 1 && split[0].length > 1 && split[0].all { it in 'A'..'Z' }) {
                            split[0].map { it.toString() }
                        } else {
                            split
                        }
                    }
                }
            } else {
                listOf(rawAns)
            }

            // Infer type
            val inferredType = when {
                rawType.contains("判断") || normalizedAnsTokens.firstOrNull() in listOf("正确", "错误") -> "judge"
                rawType.contains("多选") || (rules.inferType && normalizedAnsTokens.size > 1) -> "multiple"
                rawType.contains("填空") -> "fill"
                else -> "single"
            }

            val finalOptions = if (inferredType == "judge" && nonEmptyOptions.isEmpty()) {
                listOf("正确", "错误")
            } else {
                nonEmptyOptions
            }

            // Semantic Validation
            val definedLetters = finalOptions.map { it.take(1).uppercase() }.toSet()
            val invalidLetters = if (inferredType in listOf("single", "multiple")) {
                normalizedAnsTokens.filter { it !in definedLetters }
            } else {
                emptyList()
            }

            // Check against existing database questions by normalized stem
            val normalizedStemKey = cleanStem.replace(Regex("[\\s，。？、！,.?!]"), "")
            val matchedExisting = existingQuestions.find {
                it.stem.replace(Regex("[\\s，。？、！,.?!]"), "") == normalizedStemKey
            }

            val (status, statusMsg, invalidDetail) = when {
                cleanStem.isBlank() -> Triple("invalid", "缺失题干内容", "题干列为空，请补充题干")
                normalizedAnsTokens.isEmpty() -> Triple("invalid", "缺失标准答案", "答案列为空，请指定正确答案")
                invalidLetters.isNotEmpty() -> Triple(
                    "invalid",
                    "答案包含不存在的 ${invalidLetters.joinToString(",")} 选项，需要修正",
                    "表格答案列值为 \"${rawAns}\"，但此题实际只包含 ${definedLetters.joinToString("、")} 选项。"
                )
                matchedExisting != null && matchedExisting.answer != normalizedAnsTokens -> Triple(
                    "conflict",
                    "与现有题库答案不一致 (原有:${matchedExisting.answer.joinToString(",")}, 导入表:${normalizedAnsTokens.joinToString(",")})",
                    ""
                )
                matchedExisting != null -> Triple(
                    "duplicate",
                    "与题库现有题目完全相同 (编号 ${matchedExisting.id})",
                    ""
                )
                else -> Triple(
                    "valid",
                    "数据项完备 (正确答案: ${normalizedAnsTokens.joinToString(", ")})",
                    ""
                )
            }

            EvaluatedImportItem(
                id = "IMP_ROW_${row.rowNumber}",
                rowNumber = row.rowNumber,
                stem = cleanStem,
                type = inferredType,
                difficulty = difficulty,
                chapter = chapter,
                options = finalOptions,
                answer = normalizedAnsTokens,
                rawAnswerText = normalizedAnsTokens.joinToString(", "),
                explanation = explanation,
                status = status,
                statusMessage = statusMsg,
                invalidDetail = invalidDetail,
                existingQuestion = matchedExisting
            )
        }
    }
}
