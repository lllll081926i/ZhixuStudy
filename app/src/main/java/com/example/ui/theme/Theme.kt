package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Immutable
data class SereneExtendedColors(
    val isDark: Boolean,
    val bgCanvas: Color,
    val cardSurface: Color,
    val subtleSurface: Color,
    val highSurface: Color,
    val borderSubtle: Color,
    val borderStrong: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val primaryAccent: Color,
    val primaryBright: Color,
    val primarySoft: Color,
    val primaryUltraSoft: Color,
    val mintBg: Color,
    val mintText: Color,
    val peachBg: Color,
    val peachText: Color,
    val amberBg: Color,
    val amberText: Color,
    val dangerBg: Color,
    val dangerText: Color
)

val LocalSereneColors = staticCompositionLocalOf {
    SereneExtendedColors(
        isDark = false,
        bgCanvas = SereneBgLight,
        cardSurface = SereneSurfaceLight,
        subtleSurface = SereneSurfaceSubtleLight,
        highSurface = SereneSurfaceHighLight,
        borderSubtle = SereneBorderLight,
        borderStrong = SereneBorderStrongLight,
        textPrimary = SereneTextPrimaryLight,
        textSecondary = SereneTextSecondaryLight,
        textMuted = SereneTextMutedLight,
        primaryAccent = SerenePrimaryLight,
        primaryBright = SerenePrimaryBrightLight,
        primarySoft = SerenePrimarySoftLight,
        primaryUltraSoft = SerenePrimaryUltraSoftLight,
        mintBg = SereneMintBgLight,
        mintText = SereneMintTextLight,
        peachBg = SerenePeachBgLight,
        peachText = SerenePeachTextLight,
        amberBg = SereneAmberBgLight,
        amberText = SereneAmberTextLight,
        dangerBg = SereneDangerBgLight,
        dangerText = SereneDangerTextLight
    )
}

private val LightColorScheme = lightColorScheme(
    primary = SerenePrimaryLight,
    onPrimary = Color.White,
    primaryContainer = SerenePrimarySoftLight,
    onPrimaryContainer = SerenePrimaryLight,
    secondary = SerenePrimaryBrightLight,
    onSecondary = Color.White,
    secondaryContainer = SerenePrimarySoftLight,
    onSecondaryContainer = SereneTextPrimaryLight,
    tertiary = SereneMintTextLight,
    onTertiary = Color.White,
    tertiaryContainer = SereneMintBgLight,
    onTertiaryContainer = SereneMintTextLight,
    error = SereneDangerTextLight,
    errorContainer = SereneDangerBgLight,
    onErrorContainer = SereneDangerTextLight,
    background = SereneBgLight,
    onBackground = SereneTextPrimaryLight,
    surface = SereneSurfaceLight,
    onSurface = SereneTextPrimaryLight,
    surfaceVariant = SereneSurfaceSubtleLight,
    onSurfaceVariant = SereneTextSecondaryLight,
    outline = SereneBorderStrongLight,
    outlineVariant = SereneBorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = SerenePrimaryDark,
    onPrimary = Color(0xFF13151F),
    primaryContainer = SerenePrimarySoftDark,
    onPrimaryContainer = SerenePrimaryDark,
    secondary = SerenePrimaryBrightDark,
    onSecondary = Color(0xFF13151F),
    secondaryContainer = SerenePrimarySoftDark,
    onSecondaryContainer = SereneTextPrimaryDark,
    tertiary = SereneMintTextDark,
    onTertiary = Color(0xFF0A2417),
    tertiaryContainer = SereneMintBgDark,
    onTertiaryContainer = SereneMintTextDark,
    error = SereneDangerTextDark,
    errorContainer = SereneDangerBgDark,
    onErrorContainer = SereneDangerTextDark,
    background = SereneBgDark,
    onBackground = SereneTextPrimaryDark,
    surface = SereneSurfaceDark,
    onSurface = SereneTextPrimaryDark,
    surfaceVariant = SereneSurfaceSubtleDark,
    onSurfaceVariant = SereneTextSecondaryDark,
    outline = SereneBorderStrongDark,
    outlineVariant = SereneBorderDark
)

val SereneShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun SereneStudyTheme(
    themeMode: String = "system", // "system", "light", "dark"
    dynamicColor: Boolean = false,
    fontScalePercent: Int = 100,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> systemDark
    }

    val context = LocalContext.current
    val baseColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) {
        SereneExtendedColors(
            isDark = true,
            bgCanvas = if (dynamicColor) baseColorScheme.background else SereneBgDark,
            cardSurface = if (dynamicColor) baseColorScheme.surface else SereneSurfaceDark,
            subtleSurface = if (dynamicColor) baseColorScheme.surfaceVariant else SereneSurfaceSubtleDark,
            highSurface = SereneSurfaceHighDark,
            borderSubtle = SereneBorderDark,
            borderStrong = SereneBorderStrongDark,
            textPrimary = SereneTextPrimaryDark,
            textSecondary = SereneTextSecondaryDark,
            textMuted = SereneTextMutedDark,
            primaryAccent = if (dynamicColor) baseColorScheme.primary else SerenePrimaryDark,
            primaryBright = if (dynamicColor) baseColorScheme.primary else SerenePrimaryBrightDark,
            primarySoft = if (dynamicColor) baseColorScheme.primaryContainer else SerenePrimarySoftDark,
            primaryUltraSoft = SerenePrimaryUltraSoftDark,
            mintBg = SereneMintBgDark,
            mintText = SereneMintTextDark,
            peachBg = SerenePeachBgDark,
            peachText = SerenePeachTextDark,
            amberBg = SereneAmberBgDark,
            amberText = SereneAmberTextDark,
            dangerBg = SereneDangerBgDark,
            dangerText = SereneDangerTextDark
        )
    } else {
        SereneExtendedColors(
            isDark = false,
            bgCanvas = if (dynamicColor) baseColorScheme.background else SereneBgLight,
            cardSurface = if (dynamicColor) baseColorScheme.surface else SereneSurfaceLight,
            subtleSurface = if (dynamicColor) baseColorScheme.surfaceVariant else SereneSurfaceSubtleLight,
            highSurface = SereneSurfaceHighLight,
            borderSubtle = SereneBorderLight,
            borderStrong = SereneBorderStrongLight,
            textPrimary = SereneTextPrimaryLight,
            textSecondary = SereneTextSecondaryLight,
            textMuted = SereneTextMutedLight,
            primaryAccent = if (dynamicColor) baseColorScheme.primary else SerenePrimaryLight,
            primaryBright = if (dynamicColor) baseColorScheme.primary else SerenePrimaryBrightLight,
            primarySoft = if (dynamicColor) baseColorScheme.primaryContainer else SerenePrimarySoftLight,
            primaryUltraSoft = SerenePrimaryUltraSoftLight,
            mintBg = SereneMintBgLight,
            mintText = SereneMintTextLight,
            peachBg = SerenePeachBgLight,
            peachText = SerenePeachTextLight,
            amberBg = SereneAmberBgLight,
            amberText = SereneAmberTextLight,
            dangerBg = SereneDangerBgLight,
            dangerText = SereneDangerTextLight
        )
    }

    val typography = buildSereneTypography(fontScalePercent / 100f)

    CompositionLocalProvider(LocalSereneColors provides extendedColors) {
        MaterialTheme(
            colorScheme = baseColorScheme,
            typography = typography,
            shapes = SereneShapes,
            content = content
        )
    }
}

object SereneTheme {
    val colors: SereneExtendedColors
        @Composable
        get() = LocalSereneColors.current
}
