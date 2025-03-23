package com.ipcamera.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcamera.ui.nav.NavigationRoute
import com.ipcamera.ui.nav.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigator: Navigator,
): ViewModel() {

    fun onStreaming() {
        viewModelScope.launch {
            navigator.navigate(NavigationRoute.Streaming)
        }
    }

    fun onSettings() {
        viewModelScope.launch {
            navigator.navigate(NavigationRoute.Settings)
        }
    }
}