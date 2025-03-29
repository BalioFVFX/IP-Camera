package com.ipcamera.ui.screen.settings.fps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcamera.ui.nav.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FramesPerSecondViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FramesPerSecondUi>(
        FramesPerSecondUi(
            supportedFps = listOf(15f, 20f, 25f, 30f, 40f, 45f, 50f),
            initialFps = 15f,
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onSaveAction(fps: Float) {
        viewModelScope.launch {
            navigator.dismiss()
        }
    }
}