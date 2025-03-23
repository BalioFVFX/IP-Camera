package com.ipcamera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> CollectOnce(flow: Flow<T>, collector: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(
        key1 = Unit,
    ) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                collector.invoke(it)
            }
        }
    }
}