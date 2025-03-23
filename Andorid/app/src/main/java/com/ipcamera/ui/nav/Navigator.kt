package com.ipcamera.ui.nav

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class Navigator @Inject constructor() {

    val navigationEvent: Flow<NavigationRoute>
        get() = _navigationEvent.receiveAsFlow()

    private val _navigationEvent = Channel<NavigationRoute>()


    suspend fun navigate(route: NavigationRoute) {
        _navigationEvent.send(route)
    }
}