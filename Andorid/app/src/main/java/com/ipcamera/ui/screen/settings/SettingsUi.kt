package com.ipcamera.ui.screen.settings

import com.ipcamera.util.Text

sealed class SettingUiItem {

    data class Header(
        val title: Text,
    ) : SettingUiItem()

    data class Setting(
        val title: Text,
        val description: Text,
        val onClick: () -> Unit,
    ) : SettingUiItem()
}

data class SettingsUi(
    val items: List<SettingUiItem>,
)
