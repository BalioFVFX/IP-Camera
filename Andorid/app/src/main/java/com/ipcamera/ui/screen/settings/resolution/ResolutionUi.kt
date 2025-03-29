package com.ipcamera.ui.screen.settings.resolution

data class ResolutionUi(
    val resolutions: List<Resolution>,
    val onResolutionClick: (Resolution) -> Unit,
)

data class Resolution(
    val value: String,
    val selected: Boolean,
    val index: Int,
)