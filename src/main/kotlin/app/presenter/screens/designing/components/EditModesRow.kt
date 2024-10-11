package app.presenter.screens.designing.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.editing.*
import app.presenter.components.tooltip.*
import app.presenter.screens.designing.*

@Composable
fun EditModesRow(
    modifier: Modifier = Modifier,
    editMode: MutableState<EditMode>
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModeButton(
            icon = Icons.Filled.PanTool,
            editMode = editMode.value,
            connectedEditMode = EditMode.SELECTOR,
            onClick = {
                editMode.value = EditMode.SELECTOR
            }
        )
        ModeButton(
            icon = Icons.Filled.SyncAlt,
            editMode = editMode.value,
            connectedEditMode = EditMode.CONNECTOR,
            onClick = {
                editMode.value = EditMode.CONNECTOR
            }
        )
        Text(
            modifier = Modifier.weight(1f),
            text = when (editMode.value) {
                EditMode.SELECTOR -> "Selector Tool"
                EditMode.CONNECTOR -> "Connector Tool"
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            color = Color.Black
        )
    }
}

@Composable
private fun ModeButton(
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
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(if (editMode == connectedEditMode) ICON_BACKGROUND_COLOR_HIGHLIGHTED else ICON_BACKGROUND_COLOR))
                .border(1.dp, Color(if (editMode == connectedEditMode) ICON_IMAGE_COLOR_HIGHLIGHTED else ICON_IMAGE_COLOR), CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = icon,
                contentDescription = "Edit mode button",
                tint = Color(if (editMode == connectedEditMode) ICON_IMAGE_COLOR_HIGHLIGHTED else ICON_IMAGE_COLOR)
            )
        }
    }
}