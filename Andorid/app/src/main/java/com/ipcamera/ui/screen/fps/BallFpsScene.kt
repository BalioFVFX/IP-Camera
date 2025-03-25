package com.ipcamera.ui.screen.fps

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.max

@Composable
fun BallFpsScene(
    modifier: Modifier = Modifier,
    fps: Int,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val ballPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                delayMillis = 0,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    var capturedBallPosition by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(fps) {
        while (isActive) {
            capturedBallPosition = ballPosition
            delay(max(1, 1000L / fps))
        }
    }

    Canvas(
        modifier = modifier
            .height(146.dp)
            .padding(top = 32.dp),
    ) {
        drawRect(
            color = Color.Black,
        )

        drawCircle(
            color = Color.White,
            radius = 32f,
            center = Offset(
                capturedBallPosition,
                size.height / 2,
            )
        )
    }
}