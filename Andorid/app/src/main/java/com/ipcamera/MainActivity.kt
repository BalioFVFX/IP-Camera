package com.ipcamera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipcamera.ui.BackgroundColor
import com.ipcamera.ui.component.ComposeAppTheme
import com.ipcamera.ui.component.NavigationButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(BackgroundColor)
                ){
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(alignment = Alignment.Center)
        ) {
            NavigationButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Streaming"
            ) { }

            Spacer(modifier = Modifier.size(24.dp))

            NavigationButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Settings"
            ) { }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ComposeAppTheme {
        MainScreen()
    }
}