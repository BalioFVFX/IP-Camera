package com.ipcamera.ui.component

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun DiscreteSlider(
    modifier: Modifier = Modifier,
    value: Float,
    supportedValues: List<Float>,
    onValueChanged: (Float) -> Unit,
) {
    Slider(
        modifier = modifier,
        value = supportedValues.findIndexOf(value) + 1f,
        valueRange = 1f..supportedValues.size.toFloat(),
        steps = max(0, supportedValues.size - 2),
        onValueChange = { newValue ->
            onValueChanged.invoke(
                supportedValues[newValue.roundToInt() - 1]
            )
        }
    )
}

private fun List<Float>.findIndexOf(value: Float): Int {
    return indexOf(value).takeIf { it != -1 }
        ?: throw IllegalArgumentException("Value $value not found in the provided supportedValues")
}

@Composable
@Preview
private fun DiscreteSliderPreview() {
    DiscreteSlider(
        supportedValues = listOf(10f),
        value = 10f,
        onValueChanged = {}
    )
}