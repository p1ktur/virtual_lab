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
import app.presenter.screens.designing.*

@Composable
fun ModeButton(
    modifier: Modifier,
    icon: ImageVector,
    editMode: EditMode,
    connectedEditMode: EditMode,
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
                .background(Color(if (editMode == connectedEditMode) ICON_BACKGROUND_COLOR_HIGHLIGHTED else ICON_BACKGROUND_COLOR))
                .border(1.dp, Color(if (editMode == connectedEditMode) EDIT_ICON_IMAGE_COLOR_HIGHLIGHTED else EDIT_ICON_IMAGE_COLOR), CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = icon,
                contentDescription = "Edit mode button",
                tint = Color(if (editMode == connectedEditMode) EDIT_ICON_IMAGE_COLOR_HIGHLIGHTED else EDIT_ICON_IMAGE_COLOR)
            )
        }
    }
}