package com.ipcamera.ui.screen.settings.resolution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcamera.ui.nav.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResolutionViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val resolutions = mutableListOf<Resolution>(
        Resolution(
            value = "1280x720",
            selected = false,
            index = 0,
        ),
        Resolution(
            value = "1920x1080",
            selected = true,
            index = 1,
        ),
        Resolution(
            value = "3840x2160",
            selected = false,
            index = 2,
        )
    )

    private val _uiState = MutableStateFlow<ResolutionUi>(
        value = ResolutionUi(
            resolutions = resolutions.toList(),
            onResolutionClick = {
                onResolutionSelected(it)
            }
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onResolutionSelected(selectedResolution: Resolution) {
        if (selectedResolution.selected) {
            return
        }

        viewModelScope.launch {
            val oldSelectedIndex = resolutions.indexOfFirst { it.selected }

            resolutions[oldSelectedIndex] = resolutions[oldSelectedIndex].copy(selected = false)
            resolutions[selectedResolution.index] = selectedResolution.copy(selected = true)

            _uiState.value = _uiState.value.copy(
                resolutions = resolutions.toList()
            )

            delay(100) // Await UI refresh feedback
            navigator.dismiss()
        }
    }
}