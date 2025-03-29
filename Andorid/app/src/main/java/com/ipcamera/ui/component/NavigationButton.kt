package com.ipcamera.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipcamera.R
import com.ipcamera.clickableWithSound
import com.ipcamera.ui.base.ButtonCornerRadius
import com.ipcamera.ui.base.ContentBorderColor
import com.ipcamera.ui.base.ContentColor
import com.ipcamera.ui.base.ContentCornerRadius

@Composable
fun NavigationButtonContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .border(
                width = 0.1.dp,
                brush = SolidColor(ContentBorderColor),
                shape = RoundedCornerShape(ButtonCornerRadius)
            )
            .background(
                color = ContentColor,
                shape = RoundedCornerShape(ButtonCornerRadius)
            )
            .clickableWithSound(
                interactionSource = interactionSource,
                onClick = { onClick.invoke() }
            )
            .padding(
                all = 12.dp
            )

    ) {
        content.invoke()
    }
}

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    NavigationButtonContainer(
        modifier = modifier,
        onClick = onClick
    ) {
        Row {
            NormalText(
                modifier = Modifier.weight(1f),
                text = title,
            )

            Spacer(modifier = Modifier.size(12.dp))

            Image(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically),
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun DetailedNavigationButton(
    modifier: Modifier = Modifier,
    title: String,
    detail: String,
    onClick: () -> Unit,
) {
    NavigationButtonContainer(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                NormalText(text = title)

                Spacer(modifier = Modifier.size(6.dp))

                HelperText(text = detail)
            }

            Spacer(modifier = Modifier.size(12.dp))

            Image(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun NavigationButtonPreview() {
    NavigationButton(
        title = "Camera Settings",
        onClick = {},
    )
}

@Preview
@Composable
private fun DetailedNavigationButtonPreview() {
    DetailedNavigationButton(
        title = "Camera Settings",
        detail = "Details",
        onClick = {},
    )
}