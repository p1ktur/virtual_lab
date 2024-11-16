package app.presenter.components.common

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*

sealed interface TextFieldFilter {
    data object Default : TextFieldFilter
    data object OnlyNumbers : TextFieldFilter
}

@Composable
fun SingleLineTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    filter: TextFieldFilter = TextFieldFilter.Default,
    showFrame: Boolean = true,
    showEditIcon: Boolean = true,
    maxLength: Int = 72,
    textStyle: TextStyle,
    labelTextStyle: TextStyle,
    textColor: Color
) {
    var internalText by remember(text) { mutableStateOf(text) }

    Column(
        modifier = modifier
    ) {
        if (showFrame) {
            val offsetTarget by remember(internalText) {
                mutableStateOf(
                    if (internalText.isBlank()) {
                        Offset(
                            x = 10f,
                            y = textStyle.lineHeight.value + 12f
                        )
                    } else {
                        Offset.Zero
                    }
                )
            }
            val labelOffset by animateOffsetAsState(
                targetValue = offsetTarget,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = EaseInOut
                ),
                label = "Label offset animation"
            )

            val labelColorTarget by remember(internalText) {
                mutableStateOf(
                    if (internalText.isBlank()) {
                        textColor.copy(0.75f)
                    } else {
                        textColor
                    }
                )
            }
            val labelColor by animateColorAsState(
                targetValue = labelColorTarget,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = EaseInOut
                ),
                label = "Label color animation"
            )

            Text(
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .offset {
                        IntOffset(x = labelOffset.x.toInt(), y = labelOffset.y.toInt())
                    },
                text = label,
                style = labelTextStyle,
                color = labelColor
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .run {
                    if (showFrame) {
                        this.clip(RoundedCornerShape(6f))
                            .border(1.dp, textColor, RoundedCornerShape(6f))
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    } else {
                        this.padding(vertical = 4.dp)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showEditIcon) {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(10.dp),
                    imageVector = Icons.Default.ModeEdit,
                    contentDescription = "Edit icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            if (!showFrame) {
                Text(
                    text = label,
                    style = labelTextStyle,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .run {
                        if (showFrame) {
                            this
                        } else {
                            this.drawBehind {
                                drawLine(
                                    color = textColor,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 0.8f
                                )
                            }
                        }
                    },
                value = internalText,
                onValueChange = { newValue ->
                    if (newValue.length <= maxLength) {
                        internalText = when (filter) {
                            TextFieldFilter.Default -> newValue
                            TextFieldFilter.OnlyNumbers -> newValue.filter { it.isDigit() }
                        }
                        onValueChange(internalText)
                    }
                },
                singleLine = true,
                textStyle = textStyle.copy(color = textColor),
                cursorBrush = SolidColor(textColor)
            )
        }
    }
}

@Composable
fun MultiLineTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    filter: TextFieldFilter = TextFieldFilter.Default,
    showFrame: Boolean = true,
    maxLength: Int = 288,
    textStyle: TextStyle,
    labelTextStyle: TextStyle,
    placeholderTextStyle: TextStyle,
    textColor: Color
) {
    var internalText by remember(text) { mutableStateOf(text) }

    val offsetTarget by remember(internalText) {
        mutableStateOf(
            if (internalText.isBlank()) {
                Offset(
                    x = 10f,
                    y = textStyle.lineHeight.value + 12f
                )
            } else {
                Offset.Zero
            }
        )
    }
    val labelOffset by animateOffsetAsState(
        targetValue = offsetTarget,
        animationSpec = tween(
            durationMillis = 300,
            easing = EaseInOut
        ),
        label = "Label offset animation"
    )

    val labelColorTarget by remember(internalText) {
        mutableStateOf(
            if (internalText.isBlank()) {
                textColor.copy(0.75f)
            } else {
                textColor
            }
        )
    }
    val labelColor by animateColorAsState(
        targetValue = labelColorTarget,
        animationSpec = tween(
            durationMillis = 300,
            easing = EaseInOut
        ),
        label = "Label color animation"
    )

    val labelAlphaTarget by remember(internalText) {
        mutableStateOf(
            if (internalText.isBlank()) {
                0f
            } else {
                1f
            }
        )
    }
    val labelAlpha by animateFloatAsState(
        targetValue = labelAlphaTarget,
        animationSpec = tween(
            durationMillis = 300,
            easing = EaseInOut
        ),
        label = "Label color animation"
    )

    val placeholderAlphaTarget by remember(internalText) {
        mutableStateOf(
            if (internalText.isBlank()) {
                1f
            } else {
                0f
            }
        )
    }
    val placeholderAlpha by animateFloatAsState(
        targetValue = placeholderAlphaTarget,
        animationSpec = tween(
            durationMillis = 300,
            easing = EaseInOut
        ),
        label = "Label color animation"
    )

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 2.dp)
                .offset {
                    IntOffset(x = labelOffset.x.toInt(), y = labelOffset.y.toInt())
                }
                .alpha(labelAlpha),
            text = label,
            style = labelTextStyle,
            color = labelColor
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .run {
                    if (showFrame) {
                        this.clip(RoundedCornerShape(6f))
                            .border(1.dp, textColor, RoundedCornerShape(6f))
                            .padding(4.dp)
                    } else {
                        this.padding(vertical = 4.dp)
                    }
                }
        ) {
            BasicTextField(
                modifier = modifier.fillMaxWidth(),
                value = internalText,
                onValueChange = { newValue ->
                    if (newValue.length <= maxLength) {
                        internalText = when (filter) {
                            TextFieldFilter.Default -> newValue
                            TextFieldFilter.OnlyNumbers -> newValue.filter { it.isDigit() }
                        }
                        onValueChange(internalText)
                    }
                },
                textStyle = textStyle.copy(color = textColor),
                cursorBrush = SolidColor(textColor)
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .alpha(placeholderAlpha)
                    .align(Alignment.CenterStart),
                text = placeholder,
                style = placeholderTextStyle,
                color = textColor.copy(0.75f)
            )
        }
    }
}