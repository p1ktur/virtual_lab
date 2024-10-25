package app.presenter.screens.designing.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.editing.*
import app.domain.viewModels.designing.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*
import app.test.*

@Composable
fun ToolAndActionsBar(
    modifier: Modifier = Modifier,
    uiState: DesigningUiState,
    onUiAction: (DesigningUiAction) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ActionButton(
            modifier = Modifier
                .size(28.dp)
                .addTestTag("Add Component Button"),
            icon = Icons.Filled.Add,
            actionText = "Add Component",
            onClick = {
                onUiAction(DesigningUiAction.AddComponent)
            }
        )
        VerticalDivider(height = 32.dp)
        EditModeButton(
            modifier = Modifier
                .size(32.dp)
                .addTestTag("Selector Tool Button"),
            icon = Icons.Outlined.PanTool,
            editMode = uiState.editMode,
            connectedEditMode = EditMode.SELECTOR,
            onClick = {
                onUiAction(DesigningUiAction.UpdateEditMode(EditMode.SELECTOR))
            }
        )
        EditModeButton(
            modifier = Modifier
                .size(32.dp)
                .addTestTag("Connector Tool Button"),
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
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End,
            color = Color.Black
        )
    }
}