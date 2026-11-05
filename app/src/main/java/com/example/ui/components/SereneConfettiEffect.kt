package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val width: Float,
    val height: Float,
    val rotationSpeed: Float
)

/**
 * Native Canvas Confetti Cannon Particle Effect.
 * Spawns celebration ribbons and paper squares falling across the screen upon hitting study milestones.
 */
@Composable
fun SereneConfettiEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 48,
    onFinished: () -> Unit = {}
) {
    val progress = remember { Animatable(0f) }

    val palette = listOf(
        Color(0xFF6269C8), // Violet
        Color(0xFF216C4B), // Mint
        Color(0xFFE56A54), // Peach
        Color(0xFFF4B23E), // Amber
        Color(0xFF5BA4E6), // Sky
        Color(0xFFE965A0)  // Rose
    )

    val particles = remember {
        val rand = Random(System.currentTimeMillis())
        List(particleCount) {
            val angle = rand.nextDouble(-Math.PI * 0.85, -Math.PI * 0.15)
            val speed = rand.nextDouble(380.0, 920.0).toFloat()
            ConfettiParticle(
                startX = rand.nextDouble(0.1, 0.9).toFloat(),
                startY = rand.nextDouble(0.05, 0.25).toFloat(),
                velocityX = (Math.cos(angle) * speed).toFloat(),
                velocityY = (Math.sin(angle) * speed).toFloat(),
                color = palette.random(rand),
                width = rand.nextDouble(14.0, 26.0).toFloat(),
                height = rand.nextDouble(8.0, 16.0).toFloat(),
                rotationSpeed = rand.nextDouble(180.0, 720.0).toFloat()
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = LinearEasing)
        )
        onFinished()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val t = progress.value
        val gravity = 980f * t * t * 0.5f

        particles.forEach { p ->
            val curX = (size.width * p.startX) + (p.velocityX * t * 0.45f)
            val curY = (size.height * p.startY) + (p.velocityY * t * 0.35f) + gravity
            val curAngle = p.rotationSpeed * t
            val alpha = (1f - (t * 1.05f)).coerceIn(0f, 1f)

            if (curY in -50f..size.height + 50f && curX in -50f..size.width + 50f) {
                rotate(degrees = curAngle, pivot = Offset(curX, curY)) {
                    drawRect(
                        color = p.color.copy(alpha = alpha),
                        topLeft = Offset(curX - p.width / 2f, curY - p.height / 2f),
                        size = Size(p.width, p.height)
                    )
                }
            }
        }
    }
}
