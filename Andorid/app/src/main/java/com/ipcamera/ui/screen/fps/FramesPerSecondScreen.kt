package com.ipcamera.ui.screen.fps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamera.R
import com.ipcamera.ui.component.NormalText
import java.nio.file.WatchEvent

@Composable
fun FramesPerSecondScreen() {
    FramesPerSecondScreenContent(
        content = FramesPerSecondUi(
            minimumFps = 1,
            maximumFps = 120,
            initialFps = 29,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FramesPerSecondScreenContent(
    content: FramesPerSecondUi,
) {

    var currentFps by remember { mutableIntStateOf(content.initialFps) }

    Column(
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        NormalText(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                )
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResource(R.string.frames_per_second)
        )

        BallFpsScene(
            modifier = Modifier
                .fillMaxWidth(),
            fps = currentFps,
        )

        NormalText(
            modifier = Modifier
                .align(alignment = Alignment.End)
                .padding(top = 4.dp),
            text = "FPS: $currentFps",
            fontSize = 12.sp,
        )

        Slider(
            modifier = Modifier,
            value = currentFps.toFloat(),
            valueRange = content.minimumFps.toFloat() .. content.maximumFps.toFloat() ,
            onValueChange = {
                currentFps = it.toInt()
            }
        )

        Spacer(modifier = Modifier.size(64.dp))
    }
}

@Composable
@Preview
private fun FramesPerSecondScreenPreview() {
    FramesPerSecondScreenContent(
        content = FramesPerSecondUi(
            minimumFps = 1,
            maximumFps = 120,
            initialFps = 29,
        )
    )
}