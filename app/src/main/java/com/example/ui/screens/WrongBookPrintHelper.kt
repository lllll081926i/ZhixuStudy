package com.example.ui.screens

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.data.local.QuestionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WrongBookPrintHelper {

    fun printWrongQuestions(context: Context, questions: List<QuestionEntity>) {
        if (questions.isEmpty()) return

        val htmlContent = buildHtmlDocument(questions)
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
                val jobName = "知序错题集_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.CHINA).format(Date())}"
                val printAdapter = view.createPrintDocumentAdapter(jobName)
                val printAttributes = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build()
                printManager.print(jobName, printAdapter, printAttributes)
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun buildHtmlDocument(questions: List<QuestionEntity>): String {
        val nowStr = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA).format(Date())
        val sb = StringBuilder()

        sb.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <title>知序 Study · 错题巩固集</title>
                <style>
                    @page { size: A4; margin: 18mm 16mm; }
                    body { font-family: -apple-system, BlinkMacSystemFont, "PingFang SC", "Segoe UI", Roboto, sans-serif; color: #1a1c2e; line-height: 1.6; font-size: 13.5pt; }
                    .header { text-align: center; border-bottom: 2px solid #4950ad; padding-bottom: 12px; margin-bottom: 24px; }
                    .header h1 { font-size: 20pt; margin: 0 0 6px 0; color: #1a1c2e; letter-spacing: -0.5px; }
                    .header .meta { font-size: 10pt; color: #6b7088; }
                    .question-card { margin-bottom: 22px; page-break-inside: avoid; border-bottom: 1px dashed #d5d8e8; padding-bottom: 16px; }
                    .q-stem { font-weight: bold; margin-bottom: 10px; font-size: 14pt; }
                    .q-type { display: inline-block; padding: 2px 7px; background: #e8eaff; color: #4950ad; font-size: 9.5pt; font-weight: bold; border-radius: 4px; margin-right: 6px; }
                    .options { margin: 8px 0 12px 14px; }
                    .option-item { margin: 5px 0; font-size: 12.5pt; }
                    .answer-line { height: 28px; border-bottom: 1px dotted #a0a4c0; margin: 8px 0; }
                    .page-break { page-break-before: always; }
                    .appendix-title { text-align: center; border-bottom: 2px solid #216c4b; padding-bottom: 10px; margin: 30px 0 20px; font-size: 17pt; color: #216c4b; }
                    .ans-item { margin-bottom: 14px; page-break-inside: avoid; }
                    .ans-label { font-weight: bold; color: #216c4b; }
                    .explanation { color: #444a60; font-size: 11pt; margin-top: 3px; background: #f6f7fb; padding: 8px 12px; border-radius: 6px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>知序 Study · 考前错题巩固打印集</h1>
                    <div class="meta">导出时间：$nowStr · 共收录错题 ${questions.size} 道 · 宁静笃行 必有回响</div>
                </div>
        """.trimIndent())

        // 1. Question sheet without answer (for real paper test)
        questions.forEachIndexed { idx, q ->
            sb.append("<div class=\"question-card\">")
            sb.append("<div class=\"q-stem\"><span class=\"q-type\">${q.typeLabel}</span>第 ${idx + 1} 题. ${q.stem}</div>")
            if (q.options.isNotEmpty() && q.type != "judge") {
                sb.append("<div class=\"options\">")
                q.options.forEach { opt ->
                    sb.append("<div class=\"option-item\">$opt</div>")
                }
                sb.append("</div>")
            } else if (q.type == "judge") {
                sb.append("<div class=\"options\"><span class=\"option-item\">[ &nbsp; ] 正确 &nbsp;&nbsp;&nbsp;&nbsp; [ &nbsp; ] 错误</span></div>")
            } else {
                sb.append("<div class=\"answer-line\"></div>")
            }
            sb.append("</div>")
        }

        // 2. Appendix answers & explanation
        sb.append("<div class=\"page-break\"></div>")
        sb.append("<div class=\"appendix-title\">【参考答案与名师解析附录】</div>")

        questions.forEachIndexed { idx, q ->
            sb.append("<div class=\"ans-item\">")
            sb.append("<div><span class=\"ans-label\">第 ${idx + 1} 题标准答案：</span><b>${q.answer.joinToString(", ")}</b></div>")
            if (q.explanation.isNotBlank()) {
                sb.append("<div class=\"explanation\"><b>解析：</b>${q.explanation}</div>")
            }
            sb.append("</div>")
        }

        sb.append("</body></html>")
        return sb.toString()
    }
}
