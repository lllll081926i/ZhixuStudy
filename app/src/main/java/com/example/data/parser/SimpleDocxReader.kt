package com.example.data.parser

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Lightweight, zero-dependency streaming .docx reader for Android.
 * Directly extracts text paragraphs and tables from word/document.xml.
 */
object SimpleDocxReader {

    fun extractTextParagraphs(inputStream: InputStream): List<String> {
        val paragraphs = mutableListOf<String>()
        var documentXmlBytes: ByteArray? = null

        // 1. Unpack ZIP and find word/document.xml
        ZipInputStream(inputStream).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                if (entry.name.equals("word/document.xml", ignoreCase = true)) {
                    documentXmlBytes = zip.readBytes()
                    break
                }
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }

        if (documentXmlBytes == null) return emptyList()

        // 2. Parse paragraphs <w:p> and table cells <w:tc>
        return documentXmlBytes!!.inputStream().use { stream ->
            val parser = Xml.newPullParser()
            parser.setInput(stream, "UTF-8")

            var eventType = parser.eventType
            val currentParagraph = StringBuilder()
            var inTextTag = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "t" -> inTextTag = true
                            "p", "tr" -> currentParagraph.clear()
                            "tab" -> currentParagraph.append("    ")
                            "br", "cr" -> currentParagraph.append("\n")
                        }
                    }
                    XmlPullParser.TEXT -> {
                        if (inTextTag) {
                            currentParagraph.append(parser.text)
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        when (parser.name) {
                            "t" -> inTextTag = false
                            "p", "tr" -> {
                                val text = currentParagraph.toString().trim()
                                if (text.isNotBlank()) {
                                    paragraphs.add(text)
                                }
                                currentParagraph.clear()
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
            paragraphs
        }
    }
}
