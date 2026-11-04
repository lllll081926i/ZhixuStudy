package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.SereneTheme
import kotlin.math.cos
import kotlin.math.sin

/**
 * 1. Study Hero Illustration — Warm Study Desk with Lamp, Open Book, Leaf Bookmark & Floating Pen
 */
@Composable
fun StudyHeroIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp
) {
    val colors = SereneTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "study_hero_anim")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -3.5f,
        targetValue = 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_offset"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.024f
        val lineColor = colors.primaryAccent
        val pageFill = colors.cardSurface

        // Soft warm desk lamp light cone
        val lampBeam = Path().apply {
            moveTo(w * 0.30f, h * 0.16f)
            lineTo(w * 0.08f, h * 0.78f)
            lineTo(w * 0.88f, h * 0.78f)
            lineTo(w * 0.44f, h * 0.16f)
            close()
        }
        drawPath(
            path = lampBeam,
            brush = Brush.verticalGradient(
                colors = listOf(
                    colors.amberBg.copy(alpha = glowAlpha),
                    colors.primarySoft.copy(alpha = 0.05f)
                )
            )
        )

        // Desk lamp arm & shade (top-left editorial line art)
        val lampArm = Path().apply {
            moveTo(w * 0.14f, h * 0.74f)
            quadraticTo(w * 0.10f, h * 0.32f, w * 0.28f, h * 0.15f)
        }
        drawPath(
            path = lampArm,
            color = lineColor.copy(alpha = 0.55f),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        // Lamp shade
        val shadePath = Path().apply {
            moveTo(w * 0.24f, h * 0.13f)
            lineTo(w * 0.42f, h * 0.11f)
            lineTo(w * 0.46f, h * 0.20f)
            lineTo(w * 0.22f, h * 0.20f)
            close()
        }
        drawPath(path = shadePath, color = colors.peachBg)
        drawPath(
            path = shadePath,
            color = lineColor,
            style = Stroke(width = stroke, join = StrokeJoin.Round)
        )
        // Warm bulb dot
        drawCircle(
            color = colors.amberText,
            radius = w * 0.032f,
            center = Offset(w * 0.34f, h * 0.21f)
        )

        // Bottom hardcover book base
        drawRoundRect(
            color = colors.primarySoft,
            topLeft = Offset(w * 0.14f, h * 0.70f),
            size = Size(w * 0.72f, h * 0.13f),
            cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
        )
        drawRoundRect(
            color = lineColor,
            topLeft = Offset(w * 0.14f, h * 0.70f),
            size = Size(w * 0.72f, h * 0.13f),
            cornerRadius = CornerRadius(w * 0.04f, w * 0.04f),
            style = Stroke(width = stroke)
        )
        // Bookmark ribbon hanging out
        val ribbon = Path().apply {
            moveTo(w * 0.50f, h * 0.65f)
            lineTo(w * 0.50f, h * 0.88f)
            lineTo(w * 0.54f, h * 0.84f)
            lineTo(w * 0.58f, h * 0.88f)
            lineTo(w * 0.58f, h * 0.65f)
        }
        drawPath(path = ribbon, color = colors.peachText)

        // Open floating textbook with curved pages
        val yShift = floatOffset
        val openBook = Path().apply {
            moveTo(w * 0.18f, h * 0.37f + yShift)
            quadraticTo(w * 0.34f, h * 0.31f + yShift, w * 0.50f, h * 0.38f + yShift)
            quadraticTo(w * 0.66f, h * 0.31f + yShift, w * 0.82f, h * 0.37f + yShift)
            lineTo(w * 0.82f, h * 0.66f + yShift)
            quadraticTo(w * 0.66f, h * 0.60f + yShift, w * 0.50f, h * 0.67f + yShift)
            quadraticTo(w * 0.34f, h * 0.60f + yShift, w * 0.18f, h * 0.66f + yShift)
            close()
        }
        drawPath(path = openBook, color = pageFill)
        drawPath(
            path = openBook,
            color = lineColor,
            style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Center spine
        drawLine(
            color = lineColor,
            start = Offset(w * 0.50f, h * 0.38f + yShift),
            end = Offset(w * 0.50f, h * 0.67f + yShift),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        // Left page text lines & highlight bar
        drawRoundRect(
            color = colors.mintBg,
            topLeft = Offset(w * 0.24f, h * 0.43f + yShift),
            size = Size(w * 0.19f, h * 0.055f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawLine(
            color = lineColor.copy(alpha = 0.65f),
            start = Offset(w * 0.25f, h * 0.53f + yShift),
            end = Offset(w * 0.43f, h * 0.53f + yShift),
            strokeWidth = stroke * 0.75f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = lineColor.copy(alpha = 0.45f),
            start = Offset(w * 0.25f, h * 0.59f + yShift),
            end = Offset(w * 0.38f, h * 0.59f + yShift),
            strokeWidth = stroke * 0.75f,
            cap = StrokeCap.Round
        )

        // Right page botanical sprout / checkmark badge
        drawCircle(
            color = colors.mintBg,
            radius = w * 0.085f,
            center = Offset(w * 0.66f, h * 0.51f + yShift)
        )
        val check = Path().apply {
            moveTo(w * 0.61f, h * 0.51f + yShift)
            lineTo(w * 0.65f, h * 0.55f + yShift)
            lineTo(w * 0.72f, h * 0.46f + yShift)
        }
        drawPath(
            path = check,
            color = colors.mintText,
            style = Stroke(width = stroke * 1.15f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Floating fountain pen on top-right
        rotate(degrees = 26f, pivot = Offset(w * 0.77f, h * 0.22f - yShift * 0.5f)) {
            val penBody = Path().apply {
                moveTo(w * 0.74f, h * 0.09f - yShift * 0.5f)
                lineTo(w * 0.80f, h * 0.09f - yShift * 0.5f)
                lineTo(w * 0.80f, h * 0.26f - yShift * 0.5f)
                lineTo(w * 0.77f, h * 0.33f - yShift * 0.5f)
                lineTo(w * 0.74f, h * 0.26f - yShift * 0.5f)
                close()
            }
            drawPath(path = penBody, color = colors.primaryBright)
            drawPath(
                path = penBody,
                color = lineColor,
                style = Stroke(width = stroke * 0.85f, join = StrokeJoin.Round)
            )
        }

        // Twinkling 4-point star
        drawFourPointStar(
            center = Offset(w * 0.60f, h * 0.18f - yShift * 0.4f),
            radius = w * 0.055f,
            color = colors.amberText
        )
    }
}

/**
 * 2. Review Curve Illustration — Memory Retention Wave with Animated Pulse & Stopwatch
 */
@Composable
fun ReviewCurveIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 84.dp
) {
    val colors = SereneTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "review_curve_anim")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.045f,
        targetValue = 0.085f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_radius"
    )

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.026f

        // Soft card backdrop with rounded corners
        drawRoundRect(
            color = colors.cardSurface,
            topLeft = Offset(w * 0.06f, h * 0.10f),
            size = Size(w * 0.88f, h * 0.80f),
            cornerRadius = CornerRadius(w * 0.18f, w * 0.18f)
        )
        drawRoundRect(
            color = colors.primaryAccent.copy(alpha = 0.35f),
            topLeft = Offset(w * 0.06f, h * 0.10f),
            size = Size(w * 0.88f, h * 0.80f),
            cornerRadius = CornerRadius(w * 0.18f, w * 0.18f),
            style = Stroke(width = stroke)
        )

        // Subtle horizontal dashed guide lines
        listOf(0.35f, 0.55f, 0.74f).forEach { yRatio ->
            drawLine(
                color = colors.borderSubtle,
                start = Offset(w * 0.16f, h * yRatio),
                end = Offset(w * 0.84f, h * yRatio),
                strokeWidth = stroke * 0.6f,
                cap = StrokeCap.Round
            )
        }

        // Memory boost curve with area gradient fill
        val fillPath = Path().apply {
            moveTo(w * 0.16f, h * 0.74f)
            lineTo(w * 0.16f, h * 0.32f)
            cubicTo(w * 0.25f, h * 0.56f, w * 0.32f, h * 0.62f, w * 0.38f, h * 0.64f)
            lineTo(w * 0.38f, h * 0.34f)
            cubicTo(w * 0.46f, h * 0.48f, w * 0.54f, h * 0.51f, w * 0.60f, h * 0.53f)
            lineTo(w * 0.60f, h * 0.29f)
            cubicTo(w * 0.69f, h * 0.35f, w * 0.76f, h * 0.36f, w * 0.84f, h * 0.32f)
            lineTo(w * 0.84f, h * 0.74f)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    colors.primarySoft.copy(alpha = 0.75f),
                    colors.primarySoft.copy(alpha = 0.05f)
                )
            )
        )

        val curvePath = Path().apply {
            moveTo(w * 0.16f, h * 0.32f)
            cubicTo(w * 0.25f, h * 0.56f, w * 0.32f, h * 0.62f, w * 0.38f, h * 0.64f)
            lineTo(w * 0.38f, h * 0.34f)
            cubicTo(w * 0.46f, h * 0.48f, w * 0.54f, h * 0.51f, w * 0.60f, h * 0.53f)
            lineTo(w * 0.60f, h * 0.29f)
            cubicTo(w * 0.69f, h * 0.35f, w * 0.76f, h * 0.36f, w * 0.84f, h * 0.32f)
        }
        drawPath(
            path = curvePath,
            color = colors.primaryAccent,
            style = Stroke(width = stroke * 1.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Glowing pulse on the latest review peak
        val peakPt = Offset(w * 0.60f, h * 0.29f)
        drawCircle(
            color = colors.mintText.copy(alpha = 0.25f),
            radius = w * pulseRadius,
            center = peakPt
        )

        listOf(
            Offset(w * 0.16f, h * 0.32f) to colors.peachText,
            Offset(w * 0.38f, h * 0.34f) to colors.primaryAccent,
            peakPt to colors.mintText,
            Offset(w * 0.84f, h * 0.32f) to colors.mintText
        ).forEach { (pt, dotColor) ->
            drawCircle(color = colors.cardSurface, radius = w * 0.052f, center = pt)
            drawCircle(color = dotColor, radius = w * 0.036f, center = pt)
        }
    }
}

/**
 * 3. Completion Trophy & Laurel Illustration — Animated Celebration Badge
 */
@Composable
fun CompletionVectorIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 104.dp
) {
    val colors = SereneTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "completion_anim")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_scale"
    )

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.025f

        // Dual soft halos
        drawCircle(
            color = colors.mintBg,
            radius = w * 0.43f,
            center = Offset(w * 0.5f, h * 0.52f)
        )
        drawCircle(
            color = colors.primarySoft.copy(alpha = 0.65f),
            radius = w * 0.32f,
            center = Offset(w * 0.5f, h * 0.52f)
        )

        // Trophy cup silhouette in 2D editorial line art
        val cupPath = Path().apply {
            moveTo(w * 0.32f, h * 0.26f)
            lineTo(w * 0.68f, h * 0.26f)
            lineTo(w * 0.64f, h * 0.48f)
            quadraticTo(w * 0.50f, h * 0.62f, w * 0.36f, h * 0.48f)
            close()
        }
        drawPath(path = cupPath, color = colors.cardSurface)
        drawPath(
            path = cupPath,
            color = colors.primaryAccent,
            style = Stroke(width = stroke, join = StrokeJoin.Round)
        )

        // Trophy handles
        drawArc(
            color = colors.primaryAccent,
            startAngle = 90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.22f, h * 0.29f),
            size = Size(w * 0.16f, h * 0.16f),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = colors.primaryAccent,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.62f, h * 0.29f),
            size = Size(w * 0.16f, h * 0.16f),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        // Trophy stem & pedestal
        drawLine(
            color = colors.primaryAccent,
            start = Offset(w * 0.50f, h * 0.56f),
            end = Offset(w * 0.50f, h * 0.72f),
            strokeWidth = stroke * 1.2f,
            cap = StrokeCap.Round
        )
        drawRoundRect(
            color = colors.peachBg,
            topLeft = Offset(w * 0.35f, h * 0.72f),
            size = Size(w * 0.30f, h * 0.08f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
            color = colors.primaryAccent,
            topLeft = Offset(w * 0.35f, h * 0.72f),
            size = Size(w * 0.30f, h * 0.08f),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(width = stroke)
        )

        // Checkmark inside trophy
        val check = Path().apply {
            moveTo(w * 0.42f, h * 0.39f)
            lineTo(w * 0.48f, h * 0.45f)
            lineTo(w * 0.59f, h * 0.33f)
        }
        drawPath(
            path = check,
            color = colors.mintText,
            style = Stroke(width = stroke * 1.4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Animated 4-point sparkles
        drawFourPointStar(
            center = Offset(w * 0.20f, h * 0.20f),
            radius = w * 0.055f * starScale,
            color = colors.amberText
        )
        drawFourPointStar(
            center = Offset(w * 0.80f, h * 0.22f),
            radius = w * 0.048f * (2f - starScale),
            color = colors.primaryAccent
        )
        drawCircle(
            color = colors.mintText,
            radius = w * 0.026f * starScale,
            center = Offset(w * 0.78f, h * 0.68f)
        )
    }
}

/**
 * 4. Audit Radar Illustration — Active Scanner with Rotating Sweep
 */
@Composable
fun AuditRadarIllustration(
    modifier: Modifier = Modifier,
    healthPercent: Int = 92,
    size: Dp = 88.dp
) {
    val colors = SereneTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "radar_anim")
    val radarAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_angle"
    )

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.075f

        // Outer track ring
        drawArc(
            color = colors.subtleSurface,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(stroke, stroke),
            size = Size(w - stroke * 2, h - stroke * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        // Inner radar circle
        drawCircle(
            color = colors.primarySoft.copy(alpha = 0.45f),
            radius = w * 0.28f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        // Active health progress arc
        val sweep = (healthPercent.coerceIn(0, 100) / 100f) * 360f
        drawArc(
            color = if (healthPercent >= 85) colors.mintText else colors.amberText,
            startAngle = -90f,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(stroke, stroke),
            size = Size(w - stroke * 2, h - stroke * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        // Rotating radar pointer
        rotate(degrees = radarAngle, pivot = Offset(w * 0.5f, h * 0.5f)) {
            drawLine(
                color = colors.primaryAccent.copy(alpha = 0.55f),
                start = Offset(w * 0.5f, h * 0.5f),
                end = Offset(w * 0.5f, h * 0.22f),
                strokeWidth = w * 0.025f,
                cap = StrokeCap.Round
            )
        }

        // Center shield dot
        drawCircle(
            color = colors.primaryAccent,
            radius = w * 0.055f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFourPointStar(
    center: Offset,
    radius: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        quadraticTo(center.x, center.y, center.x + radius, center.y)
        quadraticTo(center.x, center.y, center.x, center.y + radius)
        quadraticTo(center.x, center.y, center.x - radius, center.y)
        close()
    }
    drawPath(path = path, color = color)
}
