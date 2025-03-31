package com.ipcamera.ui.screen.settings.ip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipcamera.R
import com.ipcamera.ui.base.ContentColor
import com.ipcamera.ui.component.Chip
import com.ipcamera.ui.component.NormalText
import com.ipcamera.ui.component.OutlinedTextInput
import com.ipcamera.ui.component.PrimaryButton

@Composable
fun ServerIpAddressScreen(
    viewModel: ServerIpAddressViewModel = hiltViewModel<ServerIpAddressViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()

    ServerIpAddressContent(
        uiState = uiState,
        onSaveAction = { viewModel.onSaveIpAddress(it) },
        onCheckServerConnection = { viewModel.onCheckServerConnection() },
        onScan = { viewModel.onScan() }
    )
}

@Composable
fun ServerIpAddressContent(
    uiState: ServerIpAddressUi,
    onSaveAction: (String) -> Unit,
    onCheckServerConnection: () -> Unit,
    onScan: () -> Unit,
) {
    var text by remember { mutableStateOf(uiState.ipAddress) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = ContentColor)
    ) {
        NormalText(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                )
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResource(R.string.ip_address)
        )

        OutlinedTextInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(top = 32.dp),
            value = text,
            placeholder = uiState.placeholder,
            onValueChange = { text = it },
        )

        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 32.dp)
                .align(alignment = Alignment.End)
        ) {
            Chip(
                text = stringResource(R.string.check_server_connection),
                onClick = onCheckServerConnection,
            )

            Spacer(
                modifier = Modifier.size(size = 12.dp)
            )

            Chip(
                text = stringResource(R.string.scan_for_server),
                onClick = onScan,
            )
        }

        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(top = 34.dp, bottom = 14.dp),
            text = stringResource(R.string.save)
        ) {
            onSaveAction.invoke(text)
        }
    }
}

@Preview
@Composable
private fun ServerIpAddressPreview() {
    ServerIpAddressContent(
        uiState = ServerIpAddressUi(
            ipAddress = "",
            placeholder = "192.168.0.101:4444",
        ),
        onSaveAction = {},
        onCheckServerConnection = {},
        onScan = {},
    )
}