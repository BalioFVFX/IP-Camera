package com.ipcamera.ui.nav

sealed class NavigationEvent {
    data class NavigateToRoute(val route: NavigationRoute): NavigationEvent()
    object Dismiss: NavigationEvent()
}