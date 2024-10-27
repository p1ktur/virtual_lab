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
import app.domain.umlDiagram.editing.*
import app.presenter.components.tooltip.*

@Composable
fun EditModeButton(
    modifier: Modifier,
    icon: ImageVector,
    editMode: EditMode,
    connectedEditMode: EditMode,
    color: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    TooltipOn(
        text = when (connectedEditMode) {
            EditMode.SELECTOR -> "Selector Tool"
            EditMode.CONNECTOR -> "Connector Tool"
        }
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(onClick = onClick)
                .run {
                    if (editMode == connectedEditMode) {
                        this.border(0.5.dp, color, CircleShape)
                    } else this
                }
                .padding(4.dp)
                .border(0.5.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = icon,
                contentDescription = "Edit mode button",
                tint = color
            )
        }
    }
}