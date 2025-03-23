package com.ipcamera.ui.screen.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipcamera.R
import com.ipcamera.ui.component.DetailedNavigationButton
import com.ipcamera.ui.component.HeaderText
import com.ipcamera.util.Text
import com.ipcamera.util.textResource
import androidx.compose.runtime.*

@Composable
fun SettingScreen() {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreenContent(content = uiState)
}

@Composable
fun SettingsScreenContent(
    content: SettingsUi,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(horizontal = 32.dp),
        ) {
            itemsIndexed(content.items) { index, item ->
                when (item) {
                    is SettingUiItem.Header -> {
                        val topPadding = if (index == 0) 0.dp else 24.dp

                        HeaderText(
                            modifier = Modifier.padding(top = topPadding),
                            text = textResource(item.title)
                        )
                    }
                    is SettingUiItem.Setting -> {
                        DetailedNavigationButton(
                            modifier = Modifier.padding(top = 16.dp),
                            title = textResource(item.title),
                            detail = textResource(item.description),
                            onClick = item.onClick
                        )
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        content = SettingsUi(
            items = listOf(

                SettingUiItem.Header(
                    title = Text.ResourceId(R.string.camera_settings)
                ),

                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.resolution),
                    description = Text.String("1280x720"),
                    onClick = {

                    }
                ),

                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.frames_per_second),
                    description = Text.ResourceId(R.string.fps, Text.String("30")),
                    onClick = {

                    }
                ),

                SettingUiItem.Header(
                    title = Text.ResourceId(R.string.server_settings)
                ),

                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.ip_address),
                    description = Text.String("192.168.0.101:4444"),
                    onClick = {

                    }
                ),
            )
        )
    )
}