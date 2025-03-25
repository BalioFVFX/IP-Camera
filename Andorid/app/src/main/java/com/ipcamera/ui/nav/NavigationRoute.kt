package com.ipcamera.ui.nav

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    data object Main: NavigationRoute()

    @Serializable
    data object Streaming: NavigationRoute()

    @Serializable
    data object Settings: NavigationRoute()

    @Serializable
    data object FramesPerSecond: NavigationRoute()
}
