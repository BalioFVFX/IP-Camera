package com.ipcamera.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.ipcamera.ui.nav.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

}