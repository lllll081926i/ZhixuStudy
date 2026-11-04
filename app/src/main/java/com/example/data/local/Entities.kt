package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray

@Entity(tableName = "question_banks")
data class QuestionBankEntity(
    @PrimaryKey val name: String,
    val subtitle: String,
    val domainTag: String, // "railway", "highway", "exam", "cs_math"
    val totalTargetCount: Int,
    val practicedCount: Int,
    val accuracyPercent: Int,
    val versionTag: String = "v2.4",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val bank: String,
    val chapter: String,
    val section: String = "核心考点",
    val type: String, // "single", "multiple", "judge", "fill", "calc"
    val difficulty: String = "中等", // "简单", "中等", "困难"
    val score: Double = 2.0,
    val stem: String,
    val optionsJson: String, // JSON array of strings e.g. ["A. ...", "B. ..."]
    val answersJson: String, // JSON array of strings e.g. ["A"] or ["A","B"] or ["正确"]
    val explanation: String,
    val trapAnalysis: String = "",
    val mnemonic: String = "",
    val formulaHint: String = "",
    val referenceTableJson: String = "",
    val wrongCount: Int = 0,
    val isStarred: Boolean = false,
    val isFlagged: Boolean = false,
    val masteryPercent: Int = 65,
    val lastPracticedLabel: String = "2天前",
    val lastWrongAnswer: String = "",
    val auditStatus: String = "normal", // "normal", "conflict_resolved", "manual_verified"
    val repetitions: Int = 0,
    val intervalDays: Int = 1,
    val easeFactor: Double = 2.5,
    val nextReviewTimestamp: Long = 0L,
    val updatedAt: Long = System.currentTimeMillis()
) {
    val options: List<String>
        get() = try {
            val arr = JSONArray(optionsJson)
            List(arr.length()) { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }

    val answer: List<String>
        get() = try {
            val arr = JSONArray(answersJson)
            List(arr.length()) { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }

    val typeLabel: String
        get() = when (type) {
            "single" -> "单选题"
            "multiple" -> "多选题"
            "judge" -> "判断题"
            "fill" -> "填空题"
            "calc" -> "综合计算题"
            else -> "单选题"
        }
}

@Entity(tableName = "practice_records")
data class PracticeRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val bankName: String,
    val modeLabel: String,
    val dateLabel: String,
    val scorePercent: Int,
    val totalCount: Int,
    val correctCount: Int,
    val durationSec: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "import_presets")
data class ImportPresetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val customSerialRegex: String,
    val multiChoiceDelimiter: String,
    val removeSerial: Boolean = true,
    val normalizeAnswer: Boolean = true,
    val inferType: Boolean = true,
    val mergeLines: Boolean = true,
    val skipEmpty: Boolean = true
)

@Entity(tableName = "import_logs")
data class ImportLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileName: String,
    val targetBank: String,
    val addedCount: Int,
    val replacedCount: Int,
    val skippedCount: Int,
    val invalidCount: Int,
    val dateLabel: String,
    val timestamp: Long = System.currentTimeMillis()
)

fun listToJson(list: List<String>): String {
    val arr = JSONArray()
    list.forEach { arr.put(it) }
    return arr.toString()
}
