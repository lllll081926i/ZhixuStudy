package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans, FontWeight.ExtraBold)
)

fun buildSereneTypography(fontScaleFactor: Float = 1.0f, lineSpacingMode: String = "标准 (1.5x)"): Typography {
    val s = fontScaleFactor.coerceIn(0.70f, 1.35f)
    val lhMult = when {
        lineSpacingMode.contains("紧凑") -> 1.32f
        lineSpacingMode.contains("宽松") -> 1.78f
        else -> 1.52f
    }
    return Typography(
        displayLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (32 * s).sp,
            lineHeight = (32 * s * lhMult).sp,
            letterSpacing = (-0.5).sp
        ),
        displayMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (26 * s).sp,
            lineHeight = (26 * s * lhMult).sp,
            letterSpacing = (-0.3).sp
        ),
        headlineLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (22 * s).sp,
            lineHeight = (22 * s * lhMult).sp,
            letterSpacing = (-0.2).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (19 * s).sp,
            lineHeight = (19 * s * lhMult).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (17 * s).sp,
            lineHeight = (17 * s * lhMult).sp
        ),
        titleLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (18 * s).sp,
            lineHeight = (18 * s * lhMult).sp
        ),
        titleMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (16 * s).sp,
            lineHeight = (16 * s * lhMult).sp
        ),
        titleSmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (14 * s).sp,
            lineHeight = (14 * s * lhMult).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * s).sp,
            lineHeight = (16 * s * lhMult).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * s).sp,
            lineHeight = (14 * s * lhMult).sp
        ),
        bodySmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = (12 * s).sp,
            lineHeight = (12 * s * lhMult).sp
        ),
        labelLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (14 * s).sp,
            lineHeight = (14 * s * lhMult).sp
        ),
        labelMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = (12 * s).sp,
            lineHeight = (12 * s * lhMult).sp
        ),
        labelSmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (11 * s).sp,
            lineHeight = (11 * s * lhMult).sp
        )
    )
}

val Typography = buildSereneTypography(1.0f)
