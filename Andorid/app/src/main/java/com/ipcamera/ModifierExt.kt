package com.ipcamera

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView

@Composable
fun Modifier.clickableWithSound(
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true,
    onClick: () -> Unit,
) : Modifier {
    val view = LocalView.current

    return clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onClick.invoke()
        }
    )
}