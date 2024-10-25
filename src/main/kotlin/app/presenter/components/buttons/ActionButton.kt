package app.presenter.components.buttons

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.unit.*
import app.presenter.components.tooltip.*

@Composable
fun ActionButton(
    modifier: Modifier,
    icon: ImageVector,
    actionText: String,
    backgroundShown: Boolean = true,
    onClick: () -> Unit
) {
    TooltipOn(
        text = actionText
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .run {
                    if (backgroundShown) {
                        this.background(Color.White)
                            .border(0.5.dp, Color.Black, CircleShape)
                    } else {
                        this
                    }
                }
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = icon,
                contentDescription = "Action button",
                tint = Color.Black
            )
        }
    }
}