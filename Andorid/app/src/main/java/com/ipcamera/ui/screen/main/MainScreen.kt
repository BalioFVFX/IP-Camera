package com.ipcamera.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipcamera.R
import com.ipcamera.ui.component.ComposeAppTheme
import com.ipcamera.ui.component.NavigationButton

@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()
    MainScreenContent(
        onStreaming = { viewModel.onStreaming() },
        onSettings = { viewModel.onSettings() }
    )
}

@Composable
fun MainScreenContent(
    onStreaming: () -> Unit,
    onSettings: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(alignment = Alignment.Center)
        ) {
            NavigationButton(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.streaming)
            ) {
                onStreaming.invoke()
            }

            Spacer(modifier = Modifier.size(24.dp))

            NavigationButton(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.settings)
            ) {
                onSettings.invoke()
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ComposeAppTheme {
        MainScreenContent(
            onStreaming = {},
            onSettings =  {},
        )
    }
}
