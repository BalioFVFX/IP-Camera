package com.ipcamera.ui.screen.settings.resolution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipcamera.R
import com.ipcamera.ui.base.ActionColor
import com.ipcamera.ui.base.ContentColor
import com.ipcamera.ui.base.ContentCornerRadius
import com.ipcamera.ui.component.NormalText
import com.ipcamera.ui.withDefaultClickSound

@Composable
fun ResolutionScreen(viewModel: ResolutionViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    ResolutionScreenContent(uiState = uiState)
}

@Composable
fun ResolutionScreenContent(uiState: ResolutionUi) {
    Column {
        NormalText(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                )
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResource(R.string.resolution)
        )

        LazyColumn(
            modifier = Modifier
                .background(
                    color = ContentColor,
                    shape = RoundedCornerShape(ContentCornerRadius)
                )
                .padding(vertical = 16.dp)
                .padding(top = 32.dp)
        ) {
            items(uiState.resolutions, key = { it.index }) { resolution ->
                Row(
                    modifier = Modifier
                        .withDefaultClickSound {
                            uiState.onResolutionClick.invoke(resolution)
                        }
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        )
                ) {
                    RadioButton(
                        selected = resolution.selected,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = ActionColor
                        ),
                        onClick = {
                            uiState.onResolutionClick.invoke(resolution)
                        },
                    )

                    NormalText(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        text = resolution.value,
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun ResolutionScreenPreview() {
    ResolutionScreenContent(
        uiState = ResolutionUi(
            resolutions = listOf(
                Resolution(
                    value = "1280x720",
                    selected = false,
                    index = 0,
                ),
                Resolution(
                    value = "1920x1080",
                    selected = true,
                    index = 1,
                ),
                Resolution(
                    value = "3840x2160",
                    selected = false,
                    index = 2,
                )
            ),
            onResolutionClick = {}
        )
    )
}