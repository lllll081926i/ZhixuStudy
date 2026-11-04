package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SereneTheme

/**
 * Native Jetpack Compose Math Formula Renderer
 * Parses LaTeX-like fractions (\frac{A}{B}), sub/superscripts, and Greek letters into pixel-perfect math UI.
 */
@Composable
fun SereneFormulaCard(
    formula: String,
    modifier: Modifier = Modifier,
    label: String = "参考计算公式",
    fontSize: TextUnit = 16.sp
) {
    if (formula.isBlank()) return
    val colors = SereneTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.primarySoft.copy(alpha = 0.45f))
            .border(1.dp, colors.primaryAccent.copy(alpha = 0.20f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colors.primaryAccent)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "fx",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 11.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = colors.primaryAccent,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                SereneFormulaInline(formula = formula, fontSize = fontSize)
            }
        }
    }
}

@Composable
fun SereneFormulaInline(
    formula: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    color: Color = SereneTheme.colors.textPrimary
) {
    val cleanFormula = preprocessLatexSymbols(formula)

    // Regex to match \frac{numerator}{denominator}
    val fracRegex = Regex("\\\\frac\\{([^{}]+)\\}\\{([^{}]+)\\}")
    val matches = fracRegex.findAll(cleanFormula).toList()

    if (matches.isEmpty()) {
        Text(
            text = formatScriptText(cleanFormula, fontSize),
            color = color,
            modifier = modifier
        )
        return
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        var lastIndex = 0
        for (match in matches) {
            val range = match.range
            if (range.first > lastIndex) {
                val prefix = cleanFormula.substring(lastIndex, range.first)
                Text(text = formatScriptText(prefix, fontSize), color = color)
            }

            val numerator = match.groupValues[1]
            val denominator = match.groupValues[2]

            // Render fraction vertically with divider line
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = formatScriptText(numerator, fontSize * 0.88f),
                    color = color,
                    fontFamily = FontFamily.Serif
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(1.5.dp)
                        .background(color)
                )
                Box(
                    modifier = Modifier
                        .height(1.5.dp)
                        .fillMaxWidth(0.9f)
                        .background(color)
                )
                Text(
                    text = formatScriptText(denominator, fontSize * 0.88f),
                    color = color,
                    fontFamily = FontFamily.Serif
                )
            }

            lastIndex = range.last + 1
        }

        if (lastIndex < cleanFormula.length) {
            val suffix = cleanFormula.substring(lastIndex)
            Text(text = formatScriptText(suffix, fontSize), color = color)
        }
    }
}

private fun preprocessLatexSymbols(latex: String): String {
    return latex
        .replace("\\cdot", " · ")
        .replace("\\times", " × ")
        .replace("\\pm", " ± ")
        .replace("\\le", " ≤ ")
        .replace("\\leq", " ≤ ")
        .replace("\\ge", " ≥ ")
        .replace("\\geq", " ≥ ")
        .replace("\\approx", " ≈ ")
        .replace("\\ne", " ≠ ")
        .replace("\\neq", " ≠ ")
        .replace("\\alpha", "α")
        .replace("\\beta", "β")
        .replace("\\gamma", "γ")
        .replace("\\delta", "δ")
        .replace("\\tau", "τ")
        .replace("\\omega", "ω")
        .replace("\\Omega", "Ω")
        .replace("\\lambda", "λ")
        .replace("\\mu", "μ")
        .replace("\\sum", "∑")
        .replace("\\left(", "(")
        .replace("\\right)", ")")
        .replace("\\left[", "[")
        .replace("\\right]", "]")
        .replace("\\{", "{")
        .replace("\\}", "}")
        .replace(Regex("\\\\text\\{([^}]+)\\}"), "$1")
}

private fun formatScriptText(raw: String, baseFontSize: TextUnit) = buildAnnotatedString {
    var i = 0
    val len = raw.length
    while (i < len) {
        val c = raw[i]
        when (c) {
            '^' -> {
                i++
                if (i < len && raw[i] == '{') {
                    val close = raw.indexOf('}', i)
                    if (close != -1) {
                        val content = raw.substring(i + 1, close)
                        pushStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = baseFontSize * 0.72f, fontWeight = FontWeight.Bold))
                        append(content)
                        pop()
                        i = close + 1
                    } else {
                        append('^')
                    }
                } else if (i < len) {
                    pushStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = baseFontSize * 0.72f, fontWeight = FontWeight.Bold))
                    append(raw[i])
                    pop()
                    i++
                }
            }
            '_' -> {
                i++
                if (i < len && raw[i] == '{') {
                    val close = raw.indexOf('}', i)
                    if (close != -1) {
                        val content = raw.substring(i + 1, close)
                        pushStyle(SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = baseFontSize * 0.72f))
                        append(content)
                        pop()
                        i = close + 1
                    } else {
                        append('_')
                    }
                } else if (i < len) {
                    pushStyle(SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = baseFontSize * 0.72f))
                    append(raw[i])
                    pop()
                    i++
                }
            }
            else -> {
                append(c)
                i++
            }
        }
    }
}
