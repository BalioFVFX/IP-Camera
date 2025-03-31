package com.ipcamera.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipcamera.clickableWithSound
import com.ipcamera.ui.base.ButtonCornerRadius
import com.ipcamera.ui.base.ChipColor
import com.ipcamera.ui.base.ChipFocusedColor
import com.ipcamera.ui.base.TextColor

@Composable
fun Chip(
    text: String,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val pressed = interactionSource.collectIsPressedAsState().value

    Box(
        modifier = Modifier
            .clickableWithSound(
                interactionSource = interactionSource,
                onClick = onClick,
            )
            .background(
                color = if (pressed) ChipFocusedColor else ChipColor,
                shape = RoundedCornerShape(size = ButtonCornerRadius)
            )
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        SmallText(
            text = text,
            color = TextColor,
        )
    }
}

@Preview
@Composable
private fun ChipPreview() {
    Chip(
        text = "Scan",
        onClick = {},
    )
}