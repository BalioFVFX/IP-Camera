package com.ipcamera.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipcamera.R
import com.ipcamera.ui.ContentBorderColor
import com.ipcamera.ui.ContentColor

@Composable
fun NavigationButtonContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .border(
                width = 0.1.dp,
                brush = SolidColor(ContentBorderColor),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = ContentColor,
                shape = RoundedCornerShape(8.dp)
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
            Text(
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
                Text(
                    text = title,
                    )

                Spacer(modifier = Modifier.size(6.dp))

                Text(detail)
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