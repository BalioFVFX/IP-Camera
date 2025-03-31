package com.ipcamera.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class Text {

    class String(
        val stringValue: kotlin.String,
    ) : Text()

    class ResourceId(
        @StringRes val resourceId: Int,
        vararg val formatArgs: Text,
    ) : Text()
}

@Composable
fun textResource(text: Text): String {
    return when (text) {
        is Text.ResourceId -> {
            stringResource(
                id = text.resourceId,
                formatArgs = text.formatArgs.map { textResource(it) }.toTypedArray(),
            )
        }

        is Text.String -> {
            text.stringValue
        }
    }
}