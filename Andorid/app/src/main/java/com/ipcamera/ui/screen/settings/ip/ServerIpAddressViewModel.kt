package com.ipcamera.ui.screen.settings.ip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcamera.ui.nav.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerIpAddressViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServerIpAddressUi>(
        value = ServerIpAddressUi(
            ipAddress = "",
            placeholder = "192.168.0.101:4444",
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onSaveIpAddress(string: String) {
        viewModelScope.launch {
            navigator.dismiss()
        }
    }

    fun onCheckServerConnection() {

    }

    fun onScan() {

    }
}