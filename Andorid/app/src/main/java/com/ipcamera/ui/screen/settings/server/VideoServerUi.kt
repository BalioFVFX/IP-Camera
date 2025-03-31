package com.ipcamera.ui.screen.settings.server

import com.ipcamera.util.Text

data class ServerOption(
    val title: Text,
    val description: Text,
    val index: Int,
    val selected: Boolean,
)

data class VideoServerUi(
    val options: List<ServerOption>,
)