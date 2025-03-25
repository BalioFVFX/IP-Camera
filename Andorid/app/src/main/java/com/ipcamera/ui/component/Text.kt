package com.ipcamera.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ipcamera.ui.base.RobotoFontFamily
import com.ipcamera.ui.base.TextColor

@Composable
fun NormalText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = TextColor,
    fontSize: TextUnit = 16.sp,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontFamily = RobotoFontFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = fontSize,
    )
}

@Composable
fun MediumText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = TextColor,
    fontSize: TextUnit = 16.sp,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontFamily = RobotoFontFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = fontSize,
    )
}


@Composable
fun HelperText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = TextColor,
    fontSize: TextUnit = 12.sp,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontFamily = RobotoFontFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = fontSize,
    )
}

@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = TextColor,
    fontSize: TextUnit = 16.sp,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontFamily = RobotoFontFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
    )
}
@Preview
@Composable
fun NormalTextPreview() {
    NormalText(text = "Normal text")
}

@Preview
@Composable
fun MediumTextPreview() {
    MediumText(text = "Medium text")
}

@Preview
@Composable
fun HelperTextPreview() {
    HelperText(text = "Helper text")
}

@Preview
@Composable
fun HeaderTextPreview() {
    HeaderText(text = "Header text")
}