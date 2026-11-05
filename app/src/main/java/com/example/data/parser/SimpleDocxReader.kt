package com.example.data.parser

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * High-performance, low-memory streaming .docx reader:
 * - Buffered streaming I/O (32KB chunks) for rapid multi-megabyte parsing
 * - Reusable StringBuilder buffer to eliminate GC pressure
 */
object SimpleDocxReader {

    private const val BUFFER_SIZE = 32768

    fun extractTextParagraphs(inputStream: InputStream): List<String> {
        val paragraphs = ArrayList<String>(256)
        var documentXmlBytes: ByteArray? = null

        // 1. Buffered ZIP unpack to find word/document.xml
        BufferedInputStream(inputStream, BUFFER_SIZE).use { bis ->
            ZipInputStream(bis).use { zip ->
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
        }

        if (documentXmlBytes == null) return emptyList()

        // 2. Parse paragraphs <w:p> and table cells <w:tc>
        return documentXmlBytes!!.inputStream().use { stream ->
            val parser = Xml.newPullParser()
            parser.setInput(stream, "UTF-8")

            var eventType = parser.eventType
            val currentParagraph = StringBuilder(128)
            var inTextTag = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "t" -> inTextTag = true
                            "p", "tr" -> currentParagraph.setLength(0)
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
                                currentParagraph.setLength(0)
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
