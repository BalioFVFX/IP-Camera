package com.ipcamera.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipcamera.ui.base.ActionColor
import com.ipcamera.ui.base.RobotoFontFamily
import com.ipcamera.ui.base.TextColor
import com.ipcamera.ui.base.UnfocusedColor

private val focusedTextStyle = TextStyle.Default.copy(
    color = TextColor,
    fontFamily = RobotoFontFamily,
    fontSize = 14.sp,
)

private val unfocusedTextStyle = focusedTextStyle.copy(
    color = UnfocusedColor,
)

@Composable
fun OutlinedTextInput(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onValueChange: (String) -> Unit,
) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = ActionColor,
        backgroundColor = ActionColor.copy(alpha = 0.1f)
    )

    val focused = interactionSource.collectIsFocusedAsState().value

    val textStyle = if (focused) focusedTextStyle else unfocusedTextStyle

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            modifier = modifier,
            value = value,
            textStyle = textStyle,
            cursorBrush = SolidColor(value = ActionColor),
            interactionSource = interactionSource,
            onValueChange = { onValueChange.invoke(it) },
            decorationBox = { textField ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 0.2.dp,
                            color = ActionColor,
                            shape = RoundedCornerShape(size = 4.dp),
                        )
                        .padding(all = 12.dp)
                ) {
                    if (value.isBlank() && placeholder?.isNotBlank() == true) {
                        Text(
                            text = placeholder,
                            style = unfocusedTextStyle,
                        )
                    }

                    textField.invoke()
                }
            }
        )
    }
}

@Preview
@Composable
private fun OutlinedTextInputPreview() {
    OutlinedTextInput(
        value = "Text input",
        onValueChange = {},
    )
}