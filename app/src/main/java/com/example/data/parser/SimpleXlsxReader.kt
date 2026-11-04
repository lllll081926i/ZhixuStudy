package com.example.data.parser

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Lightweight, zero-dependency streaming .xlsx reader for Android.
 * Directly unpacks OpenXML Office spreadsheet parts:
 * - xl/sharedStrings.xml
 * - xl/worksheets/sheet1.xml
 */
object SimpleXlsxReader {

    fun parse(inputStream: InputStream): List<List<String>> {
        val sharedStrings = mutableListOf<String>()
        var sheetBytes: ByteArray? = null

        // 1. First pass: extract shared strings and sheet1 XML bytes from the ZIP stream
        ZipInputStream(inputStream).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                when {
                    entry.name.equals("xl/sharedStrings.xml", ignoreCase = true) -> {
                        sharedStrings.addAll(parseSharedStrings(zip))
                    }
                    entry.name.equals("xl/worksheets/sheet1.xml", ignoreCase = true) ||
                        (entry.name.startsWith("xl/worksheets/sheet", ignoreCase = true) && sheetBytes == null) -> {
                        sheetBytes = zip.readBytes()
                    }
                }
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }

        if (sheetBytes == null) return emptyList()

        // 2. Second pass: parse worksheet rows and resolve shared string references
        return sheetBytes!!.inputStream().use { stream ->
            parseSheetRows(stream, sharedStrings)
        }
    }

    private fun parseSharedStrings(stream: InputStream): List<String> {
        val list = mutableListOf<String>()
        val parser = Xml.newPullParser()
        parser.setInput(stream, "UTF-8")

        var eventType = parser.eventType
        var inText = false
        val currentText = StringBuilder()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.name == "t") inText = true
                    if (parser.name == "si") currentText.clear()
                }
                XmlPullParser.TEXT -> {
                    if (inText) currentText.append(parser.text)
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "t") inText = false
                    if (parser.name == "si") list.add(currentText.toString().trim())
                }
            }
            eventType = parser.next()
        }
        return list
    }

    private fun parseSheetRows(stream: InputStream, sharedStrings: List<String>): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        val parser = Xml.newPullParser()
        parser.setInput(stream, "UTF-8")

        var eventType = parser.eventType
        val currentRow = mutableMapOf<Int, String>()
        var currentCellCol = 0
        var isStringRef = false
        var isInlineString = false
        var currentText = StringBuilder()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "row" -> currentRow.clear()
                        "c" -> {
                            val r = parser.getAttributeValue(null, "r") ?: ""
                            currentCellCol = columnRefToIndex(r)
                            val t = parser.getAttributeValue(null, "t")
                            isStringRef = (t == "s")
                            isInlineString = (t == "inlineStr")
                            currentText.clear()
                        }
                        "v", "t" -> currentText.clear()
                    }
                }
                XmlPullParser.TEXT -> {
                    currentText.append(parser.text)
                }
                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        "v" -> {
                            val raw = currentText.toString().trim()
                            val value = if (isStringRef) {
                                val idx = raw.toIntOrNull()
                                if (idx != null && idx in sharedStrings.indices) sharedStrings[idx] else raw
                            } else {
                                raw
                            }
                            if (currentCellCol >= 0) currentRow[currentCellCol] = value
                        }
                        "t" -> {
                            if (isInlineString && currentCellCol >= 0) {
                                currentRow[currentCellCol] = currentText.toString().trim()
                            }
                        }
                        "row" -> {
                            if (currentRow.isNotEmpty()) {
                                val maxCol = currentRow.keys.maxOrNull() ?: -1
                                val rowList = List(maxCol + 1) { currentRow[it] ?: "" }
                                rows.add(rowList)
                            }
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        return rows
    }

    private fun columnRefToIndex(cellRef: String): Int {
        val colLetters = cellRef.takeWhile { it.isLetter() }.uppercase()
        if (colLetters.isEmpty()) return -1
        var idx = 0
        for (c in colLetters) {
            idx = idx * 26 + (c - 'A' + 1)
        }
        return idx - 1
    }
}
