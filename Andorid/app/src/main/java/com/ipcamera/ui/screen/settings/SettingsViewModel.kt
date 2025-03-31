package com.ipcamera.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcamera.R
import com.ipcamera.ui.nav.NavigationRoute
import com.ipcamera.ui.nav.Navigator
import com.ipcamera.util.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUi>(
        SettingsUi(
            items = listOf(
                SettingUiItem.Header(
                    title = Text.ResourceId(
                        R.string.camera_settings
                    )
                ),
                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.resolution),
                    description = Text.String("1280x720"),
                    onClick = {
                        viewModelScope.launch {
                            navigator.navigate(NavigationRoute.Resolution)
                        }
                    }
                ),
                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.frames_per_second),
                    description = Text.ResourceId(R.string.fps, Text.String("30")),
                    onClick = {
                        viewModelScope.launch {
                            navigator.navigate(NavigationRoute.FramesPerSecond)
                        }
                    }
                ),
                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.video_quality),
                    description = Text.ResourceId(R.string.video_quality_high, Text.String("95")),
                    onClick = {

                    }
                ),

                SettingUiItem.Header(
                    title = Text.ResourceId(
                        R.string.server_settings
                    )
                ),
                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.ip_address),
                    description = Text.String("192.168.0.101:4444"),
                    onClick = {
                        viewModelScope.launch {
                            navigator.navigate(NavigationRoute.ServerIpAddress)
                        }
                    }
                ),
                SettingUiItem.Setting(
                    title = Text.ResourceId(R.string.video_server),
                    description = Text.ResourceId(R.string.phone_as_video_server),
                    onClick = {

                    }
                ),
            )
        )
    )

    val uiState = _uiState.asStateFlow()
}