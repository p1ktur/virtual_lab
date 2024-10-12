package app.presenter.screens.designing.components.connectionData

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.connection.*
import app.domain.viewModels.designing.*
import app.presenter.canvas.*
import app.presenter.canvas.ArrowHead.Companion.ARROW_HEAD_LENGTH
import app.presenter.components.common.*

@Composable
fun ConnectionCustomizationView(
    reference: UMLClassConnection,
    commonCounter: Int,
    onUiAction: (DesigningUiAction) -> Unit
) {
    DefaultTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        startValue = reference.startText,
        label = "Start text:",
        onValueChange = { newValue ->
            onUiAction(DesigningUiAction.UpdateConnectionData {
                startText = newValue
            })
        },
        maxLength = 48,
        showEditIcon = false,
        textStyle = MaterialTheme.typography.bodySmall,
        labelTextStyle = MaterialTheme.typography.labelMedium,
        textColor = Color.Black
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ArrowHead.entries.forEach { head ->
            key(commonCounter) {
                DrawnArrowHead(
                    modifier = Modifier.padding(2.dp),
                    modifierSize = DpSize((ARROW_HEAD_LENGTH * 2 + 4).dp, (ARROW_HEAD_LENGTH * 2 + 4).dp),
                    arrowHead = head,
                    lookingUp = true,
                    isHighlighted = reference.startArrowHead == head,
                    onClick = {
                        onUiAction(DesigningUiAction.UpdateConnectionData {
                            startArrowHead = head
                        })
                    }
                )
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ArrowType.entries.forEach { type ->
            key(commonCounter) {
                DrawnArrow(
                    modifier = Modifier
                        .size(40.dp, 120.dp)
                        .padding(2.dp),
                    arrowType = type,
                    startArrowHead = reference.startArrowHead,
                    endArrowHead = reference.endArrowHead,
                    lookingUp = false,
                    isHighlighted = reference.arrowType == type,
                    onClick = {
                        onUiAction(DesigningUiAction.UpdateConnectionData {
                            arrowType = type
                        })
                    }
                )
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ArrowHead.entries.forEach { head ->
            key(commonCounter) {
                DrawnArrowHead(
                    modifier = Modifier.padding(2.dp),
                    modifierSize = DpSize((ARROW_HEAD_LENGTH * 2 + 4).dp, (ARROW_HEAD_LENGTH * 2 + 4).dp),
                    arrowHead = head,
                    lookingUp = false,
                    isHighlighted = reference.endArrowHead == head,
                    onClick = {
                        onUiAction(DesigningUiAction.UpdateConnectionData {
                            endArrowHead = head
                        })
                    }
                )
            }
        }
    }
    DefaultTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        startValue = reference.endText,
        label = "End text:",
        onValueChange = { newValue ->
            onUiAction(DesigningUiAction.UpdateConnectionData {
                endText = newValue
            })
        },
        maxLength = 48,
        showEditIcon = false,
        textStyle = MaterialTheme.typography.bodySmall,
        labelTextStyle = MaterialTheme.typography.labelMedium,
        textColor = Color.Black
    )
}