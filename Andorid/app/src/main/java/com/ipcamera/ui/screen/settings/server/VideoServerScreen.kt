package com.ipcamera.ui.screen.settings.server

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.ipcamera.ui.component.HelperText
import com.ipcamera.ui.component.NormalText
import com.ipcamera.ui.withDefaultClickSound
import com.ipcamera.util.Text
import com.ipcamera.util.textResource

@Composable
fun VideoServerScreen(
    viewModel: VideoServerViewModel = hiltViewModel<VideoServerViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()

    VideoServerContent(
        uiState = uiState,
        onServerOptionClick = viewModel::onServerOptionClick,
    )
}

@Composable
fun VideoServerContent(
    uiState: VideoServerUi,
    onServerOptionClick: (ServerOption) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                color = ContentColor,
                shape = RoundedCornerShape(ContentCornerRadius),
            )
    ) {
        NormalText(
            modifier = Modifier
                .padding(top = 24.dp)
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResource(R.string.video_server)
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp)
        ) {
            items(items = uiState.options, key = { it.index }) { serverOption ->
                if (serverOption.index != 0) {
                    Spacer(modifier = Modifier.size(16.dp))
                }
                Row(
                    modifier = Modifier
                        .withDefaultClickSound {
                            onServerOptionClick.invoke(serverOption)
                        }
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        )
                ) {
                    RadioButton(
                        selected = serverOption.selected,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = ActionColor
                        ),
                        onClick = {
                            onServerOptionClick.invoke(serverOption)
                        },
                    )

                    Column(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    ) {
                        NormalText(text = textResource(serverOption.title))
                        HelperText(text = textResource(serverOption.description))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun VideoServerScreenPreview() {
    VideoServerContent(
        uiState = VideoServerUi(
            listOf(
                ServerOption(
                    title = Text.ResourceId(R.string.phone_as_video_server),
                    description = Text.ResourceId(R.string.phone_as_video_server_description),
                    index = 0,
                    selected = true,
                ),
                ServerOption(
                    title = Text.ResourceId(R.string.external_device_as_video_server),
                    description = Text.ResourceId(R.string.external_device_as_video_server_description),
                    index = 1,
                    selected = false,
                )
            )
        ),
        onServerOptionClick = {},
    )
}