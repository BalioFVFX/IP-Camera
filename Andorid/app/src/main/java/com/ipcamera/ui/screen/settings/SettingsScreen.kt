package com.ipcamera.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipcamera.util.Text

@Composable
fun SettingScreen() {
    val viewModel = hiltViewModel<SettingsViewModel>()
}

@Composable
fun SettingsScreenContent(
    content: SettingsUi,
) {

}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        content = SettingsUi(
            items = listOf(

                SettingUiItem.Header(
                    title = Text.String("Camera settings")
                ),

                SettingUiItem.Setting(
                    title = Text.String("")
                )
            )
        )
    )
}