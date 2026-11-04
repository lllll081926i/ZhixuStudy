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

fun buildSereneTypography(fontScaleFactor: Float = 1.0f): Typography {
    val s = fontScaleFactor.coerceIn(0.85f, 1.35f)
    return Typography(
        displayLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (32 * s).sp,
            lineHeight = (40 * s).sp,
            letterSpacing = (-0.5).sp
        ),
        displayMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (26 * s).sp,
            lineHeight = (34 * s).sp,
            letterSpacing = (-0.3).sp
        ),
        headlineLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (22 * s).sp,
            lineHeight = (30 * s).sp,
            letterSpacing = (-0.2).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (19 * s).sp,
            lineHeight = (27 * s).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (17 * s).sp,
            lineHeight = (24 * s).sp
        ),
        titleLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = (18 * s).sp,
            lineHeight = (26 * s).sp
        ),
        titleMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (16 * s).sp,
            lineHeight = (23 * s).sp
        ),
        titleSmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (14 * s).sp,
            lineHeight = (20 * s).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * s).sp,
            lineHeight = (25 * s).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * s).sp,
            lineHeight = (21 * s).sp
        ),
        bodySmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = (12 * s).sp,
            lineHeight = (18 * s).sp
        ),
        labelLarge = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (14 * s).sp,
            lineHeight = (20 * s).sp
        ),
        labelMedium = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = (12 * s).sp,
            lineHeight = (16 * s).sp
        ),
        labelSmall = TextStyle(
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = (11 * s).sp,
            lineHeight = (15 * s).sp
        )
    )
}

val Typography = buildSereneTypography(1.0f)
