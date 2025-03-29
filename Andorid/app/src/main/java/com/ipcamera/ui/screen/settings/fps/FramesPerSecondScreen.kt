package com.ipcamera.ui.screen.settings.fps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipcamera.R
import com.ipcamera.ui.base.BottomSheetCornerRadius
import com.ipcamera.ui.base.ContentColor
import com.ipcamera.ui.component.DiscreteSlider
import com.ipcamera.ui.component.NormalText
import com.ipcamera.ui.component.PrimaryButton

@Composable
fun FramesPerSecondScreen(
    viewModel: FramesPerSecondViewModel = hiltViewModel<FramesPerSecondViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()

    FramesPerSecondScreenContent(
        content = uiState,
        onSaveAction = { viewModel.onSaveAction(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FramesPerSecondScreenContent(
    content: FramesPerSecondUi,
    onSaveAction: (Float) -> Unit,
) {
    var currentFps by remember { mutableFloatStateOf(content.initialFps) }

    Column(
        modifier = Modifier
            .background(
                color = ContentColor,
                shape = RoundedCornerShape(
                    topStart = BottomSheetCornerRadius,
                    topEnd = BottomSheetCornerRadius,
                )
            )
            .padding(horizontal = 32.dp)
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
            fps = currentFps.toInt(),
        )

        NormalText(
            modifier = Modifier
                .align(alignment = Alignment.End)
                .padding(top = 4.dp),
            text = "FPS: $currentFps",
            fontSize = 12.sp,
        )

        DiscreteSlider(
            modifier = Modifier,
            value = currentFps,
            supportedValues = content.supportedFps,
            onValueChanged = {
                currentFps = it
            },
        )

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.save)
        ) {
            onSaveAction.invoke(currentFps)
        }

        Spacer(modifier = Modifier.size(14.dp))
    }
}

@Composable
@Preview
private fun FramesPerSecondScreenPreview() {
    FramesPerSecondScreenContent(
        content = FramesPerSecondUi(
            supportedFps = listOf(15f, 20f, 24f, 30f),
            initialFps = 20f,
        ),
        onSaveAction = {}
    )
}