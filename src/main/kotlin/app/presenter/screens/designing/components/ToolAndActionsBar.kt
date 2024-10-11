package app.presenter.screens.designing.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.*
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
import app.domain.viewModels.designing.*

@Composable
fun ToolAndActionsBar(
    modifier: Modifier = Modifier,
    uiState: DesigningUiState,
    onUiAction: (DesigningUiAction) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton(
            icon = Icons.Filled.Add,
            actionText = "Add Component",
            onClick = {
                onUiAction(DesigningUiAction.AddComponent)
            }
        )
        Box(
            modifier = Modifier
                .size(1.dp, 32.dp)
                .background(Color.LightGray)
        )
        ModeButton(
            icon = Icons.Filled.PanTool,
            editMode = uiState.editMode,
            connectedEditMode = EditMode.SELECTOR,
            onClick = {
                onUiAction(DesigningUiAction.UpdateEditMode(EditMode.SELECTOR))
            }
        )
        ModeButton(
            icon = Icons.Filled.SyncAlt,
            editMode = uiState.editMode,
            connectedEditMode = EditMode.CONNECTOR,
            onClick = {
                onUiAction(DesigningUiAction.UpdateEditMode(EditMode.CONNECTOR))
            }
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            text = when (uiState.editMode) {
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
private fun ActionButton(
    icon: ImageVector,
    actionText: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TooltipOn(
        text = actionText
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(if (isPressed) ICON_BACKGROUND_COLOR_HIGHLIGHTED else ICON_BACKGROUND_COLOR))
                .border(1.dp, Color(if (isPressed) ACTION_ICON_IMAGE_COLOR_HIGHLIGHTED else ACTION_ICON_IMAGE_COLOR), CircleShape)
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
                tint = Color(if (isPressed) ACTION_ICON_IMAGE_COLOR_HIGHLIGHTED else ACTION_ICON_IMAGE_COLOR)
            )
        }
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