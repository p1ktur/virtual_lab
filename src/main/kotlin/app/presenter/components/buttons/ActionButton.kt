package app.presenter.components.buttons

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.ripple.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.unit.*
import app.presenter.components.tooltip.*
import app.presenter.screens.designing.*

@Composable
fun ActionButton(
    modifier: Modifier,
    icon: ImageVector,
    actionText: String,
    backgroundShown: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TooltipOn(
        text = actionText
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .run {
                    if (backgroundShown) {
                        this.background(Color(if (isPressed) ICON_BACKGROUND_COLOR_HIGHLIGHTED else ICON_BACKGROUND_COLOR))
                            .border(1.dp, Color(if (isPressed) ACTION_ICON_IMAGE_COLOR_HIGHLIGHTED else ACTION_ICON_IMAGE_COLOR), CircleShape)
                    } else {
                        this
                    }
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = icon,
                contentDescription = "Edit mode button",
                tint = Color(
                    if (isPressed) {
                        ACTION_ICON_IMAGE_COLOR_HIGHLIGHTED
                    } else if (backgroundShown) {
                        ACTION_ICON_IMAGE_COLOR
                    } else {
                        0xFF000000
                    }
                )
            )
        }
    }
}