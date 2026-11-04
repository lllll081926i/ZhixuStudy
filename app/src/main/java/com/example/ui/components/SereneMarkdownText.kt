package com.example.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.example.ui.theme.SereneTheme

/**
 * Lightweight native Markdown parser for Jetpack Compose:
 * - **bold** or __bold__ -> Bold font weight
 * - *italic* or _italic_ -> Italic font style
 * - `code` -> Monospace + subtle background
 * - <u>underline</u> -> Underline decoration
 */
object SereneMarkdownParser {

    fun parse(
        text: String,
        primaryAccent: Color,
        textColor: Color,
        codeBg: Color = primaryAccent.copy(alpha = 0.12f)
    ): AnnotatedString = buildAnnotatedString {
        val regex = Regex("(\\*\\*.*?\\*\\*|__.*?__|`.*?`|\\*.*?\\*|_.*?_|<u>.*?</u>)")
        var lastIdx = 0

        val matches = regex.findAll(text).toList()
        for (match in matches) {
            val start = match.range.first
            val end = match.range.last + 1

            if (start > lastIdx) {
                append(text.substring(lastIdx, start))
            }

            val token = match.value
            when {
                // **bold** or __bold__
                (token.startsWith("**") && token.endsWith("**")) || (token.startsWith("__") && token.endsWith("__")) -> {
                    val inner = token.substring(2, token.length - 2)
                    pushStyle(SpanStyle(fontWeight = FontWeight.Black, color = textColor))
                    append(inner)
                    pop()
                }
                // `code`
                token.startsWith("`") && token.endsWith("`") -> {
                    val inner = token.substring(1, token.length - 1)
                    pushStyle(
                        SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = primaryAccent,
                            background = codeBg
                        )
                    )
                    append(" $inner ")
                    pop()
                }
                // <u>underline</u>
                token.startsWith("<u>") && token.endsWith("</u>") -> {
                    val inner = token.substring(3, token.length - 4)
                    pushStyle(SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold))
                    append(inner)
                    pop()
                }
                // *italic* or _italic_
                (token.startsWith("*") && token.endsWith("*")) || (token.startsWith("_") && token.endsWith("_")) -> {
                    val inner = token.substring(1, token.length - 1)
                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(inner)
                    pop()
                }
                else -> append(token)
            }

            lastIdx = end
        }

        if (lastIdx < text.length) {
            append(text.substring(lastIdx))
        }
    }
}

@Composable
fun SereneMarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = SereneTheme.colors.textPrimary,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val colors = SereneTheme.colors
    val annotated = remember(text, colors.primaryAccent, color) {
        SereneMarkdownParser.parse(
            text = text,
            primaryAccent = colors.primaryAccent,
            textColor = color,
            codeBg = colors.primarySoft.copy(alpha = 0.5f)
        )
    }

    Text(
        text = annotated,
        modifier = modifier,
        color = color,
        style = style,
        fontSize = fontSize,
        lineHeight = lineHeight,
        maxLines = maxLines,
        overflow = overflow
    )
}
