package com.ipcamera.ui.screen.settings.server

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcamera.R
import com.ipcamera.ui.nav.Navigator
import com.ipcamera.util.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoServerViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val options = mutableListOf<ServerOption>(
        ServerOption(
            title = Text.ResourceId(R.string.phone_as_video_server),
            description = Text.ResourceId(R.string.phone_as_video_server_description),
            index = 0,
            selected = true,
        ),
        ServerOption(
            title = Text.ResourceId(R.string.external_device_as_video_server),
            description = Text.ResourceId(R.string.external_device_as_video_server_description),
            index = 1,
            selected = false,
        )
    )

    private val _uiState = MutableStateFlow<VideoServerUi>(
        value = VideoServerUi(
            options = options.toList()
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onServerOptionClick(selectedOption: ServerOption) {
        if (selectedOption.selected) {
            return
        }

        viewModelScope.launch {
            val oldSelectedIndex = options.indexOfFirst { it.selected }

            options[oldSelectedIndex] = options[oldSelectedIndex].copy(selected = false)
            options[selectedOption.index] = selectedOption.copy(selected = true)

            _uiState.value = _uiState.value.copy(
                options = options.toList()
            )

            delay(100) // Await UI refresh feedback
            navigator.dismiss()
        }
    }
}