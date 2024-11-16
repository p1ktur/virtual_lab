package app.presenter.components.buttons

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.unit.*
import app.presenter.components.tooltip.*

@Composable
fun IconButton(
    modifier: Modifier,
    icon: ImageVector,
    actionText: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = Color.Transparent,
    shape: Shape = RectangleShape,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .run {
                if (backgroundColor != Color.Transparent) {
                    this.background(backgroundColor)
                        .border(0.5.dp, color, shape)
                } else {
                    this
                }
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        TooltipOn(
            text = actionText
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                imageVector = icon,
                contentDescription = "Action button",
                tint = color
            )
        }
    }
}