package app.presenter.components.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

@Composable
fun HorizontalDivider(
    width: Dp = 32.dp,
    color: Color = Color.LightGray,
    fillMaxWidth: Float = 0f,
    horizontalPadding: Dp = 0.dp
) {
    Box(
        modifier = Modifier
            .height(1.dp)
            .run {
                if (fillMaxWidth != 0f && fillMaxWidth in 0f..1f) {
                    fillMaxWidth(fillMaxWidth)
                } else {
                    width(width)
                }
            }
            .padding(horizontal = horizontalPadding)
            .background(color)
    )
}

@Composable
fun VerticalDivider(
    height: Dp = 32.dp,
    color: Color = Color.LightGray,
    fillMaxHeight: Float = 0f,
    verticalPadding: Dp = 0.dp
) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .run {
                if (fillMaxHeight != 0f && fillMaxHeight in 0f..1f) {
                    fillMaxHeight()
                } else {
                    height(height)
                }
            }
            .padding(vertical = verticalPadding)
            .background(color)
    )
}