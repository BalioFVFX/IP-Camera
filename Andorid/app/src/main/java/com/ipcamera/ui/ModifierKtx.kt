package com.ipcamera.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalView

fun Modifier.withDefaultClickSound(
    onClick: () -> Unit,
): Modifier = composed {
    val view = LocalView.current
    this.clickable {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        onClick.invoke()
    }
}