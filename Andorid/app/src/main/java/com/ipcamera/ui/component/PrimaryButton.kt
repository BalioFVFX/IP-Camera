package com.ipcamera.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipcamera.clickableWithSound
import com.ipcamera.ui.base.ActionColor
import com.ipcamera.ui.base.ActionTextColor

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .background(
                color = ActionColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickableWithSound(
                interactionSource = interactionSource,
                onClick = onClick,
            )
    ) {
        MediumText(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
            text = text,
            color = ActionTextColor
        )
    }
}

@Composable
@Preview
private fun PrimaryButtonPreview() {
    Box(
        modifier = Modifier.size(300.dp)
    ) {
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .align(Alignment.Center),
            text = "Primary button",
            onClick = {},
        )
    }
}