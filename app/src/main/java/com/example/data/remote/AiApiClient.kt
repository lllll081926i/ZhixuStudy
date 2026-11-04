package com.example.data.remote

import com.example.data.local.QuestionEntity
import com.example.data.local.listToJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object AiApiClient {

    private val jsonMedia = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Request real streaming/blocking AI smart explanation from OpenAI/DeepSeek compatible endpoint.
     */
    suspend fun requestSmartExplanation(
        baseUrl: String,
        apiKey: String,
        modelId: String,
        question: QuestionEntity,
        userWrongAnswer: String = ""
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = if (baseUrl.endsWith("/chat/completions")) baseUrl else "${baseUrl.trimEnd('/')}/chat/completions"
            val prompt = """
                你是一位亲切资深的专业学科辅导教师。请针对以下题目为学生生成深入浅出的错因剖析：
                【题目】${question.stem}
                【题型】${question.typeLabel}
                【选项】${question.options.joinToString(" | ")}
                【正确答案】${question.answer.joinToString(", ")}
                ${if (userWrongAnswer.isNotBlank()) "【学生错选】$userWrongAnswer" else ""}
                【官方解析】${question.explanation}
                
                请按以下结构简要输出（150字以内，重点突出）：
                1. 核心考点突破
                2. 常见易错陷阱
                3. 一句话秒记口诀
            """.trimIndent()

            val bodyJson = JSONObject().apply {
                put("model", modelId.ifBlank { "deepseek-chat" })
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", "你是知序Study智能助教，擅长通俗精炼地拆解考点和错题。")
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
                put("temperature", 0.3)
                put("max_tokens", 800)
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $apiKey")
                .post(bodyJson.toString().toRequestBody(jsonMedia))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
                }
                val respStr = response.body?.string() ?: ""
                val jsonObj = JSONObject(respStr)
                val text = jsonObj.optJSONArray("choices")
                    ?.optJSONObject(0)
                    ?.optJSONObject("message")
                    ?.optString("content") ?: ""

                if (text.isNotBlank()) Result.success(text.trim())
                else Result.failure(Exception("AI 返回内容为空"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Request real AI to generate a high-quality variant question based on the original.
     */
    suspend fun requestVariantQuestion(
        baseUrl: String,
        apiKey: String,
        modelId: String,
        question: QuestionEntity
    ): Result<QuestionEntity> = withContext(Dispatchers.IO) {
        try {
            val url = if (baseUrl.endsWith("/chat/completions")) baseUrl else "${baseUrl.trimEnd('/')}/chat/completions"
            val prompt = """
                请基于原题的核心考点，衍生出一道同等难度、换了场景或参数的高质量同类练习题。
                必须输出严格合法的纯 JSON 格式，不要包含任何 markdown 标记（如 ```json），字段说明如下：
                {
                  "stem": "新题干内容",
                  "options": ["A. ...", "B. ...", "C. ...", "D. ..."],
                  "answer": ["A"],
                  "explanation": "详尽解析"
                }
                原题如下：
                【题干】${question.stem}
                【选项】${question.options.joinToString(" | ")}
                【答案】${question.answer.joinToString(", ")}
            """.trimIndent()

            val bodyJson = JSONObject().apply {
                put("model", modelId.ifBlank { "deepseek-chat" })
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", "你是一位命题专家，只输出符合规范的纯 JSON 对象。")
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
                put("temperature", 0.5)
                put("max_tokens", 800)
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $apiKey")
                .post(bodyJson.toString().toRequestBody(jsonMedia))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}"))
                }
                val respStr = response.body?.string() ?: ""
                val jsonObj = JSONObject(respStr)
                val rawContent = jsonObj.optJSONArray("choices")
                    ?.optJSONObject(0)
                    ?.optJSONObject("message")
                    ?.optString("content") ?: ""

                val cleanJsonStr = rawContent.replace(Regex("^```json\\s*"), "")
                    .replace(Regex("^```\\s*"), "")
                    .replace(Regex("\\s*```$"), "")
                    .trim()

                val qObj = JSONObject(cleanJsonStr)
                val optsArr = qObj.optJSONArray("options")
                val ansArr = qObj.optJSONArray("answer")

                val optsList = if (optsArr != null) List(optsArr.length()) { optsArr.getString(it) } else question.options
                val ansList = if (ansArr != null) List(ansArr.length()) { ansArr.getString(it) } else question.answer

                val variantQ = question.copy(
                    id = "${question.id}_VAR_${System.currentTimeMillis() % 10000}",
                    stem = "【AI 衍生题】" + qObj.optString("stem", question.stem),
                    optionsJson = listToJson(optsList),
                    answersJson = listToJson(ansList),
                    explanation = qObj.optString("explanation", question.explanation),
                    masteryPercent = 0,
                    wrongCount = 0
                )
                Result.success(variantQ)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
