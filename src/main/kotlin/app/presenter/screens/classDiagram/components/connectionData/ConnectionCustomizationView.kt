package app.presenter.screens.classDiagram.components.connectionData

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.canvas.arrows.ArrowHead.Companion.ARROW_HEAD_LENGTH
import app.presenter.canvas.arrows.*
import app.presenter.components.common.*
import app.presenter.theme.*

@Composable
fun ConnectionCustomizationView(
    reference: UMLClassConnection,
    commonCounter: Int,
    onUiAction: (ClassDiagramUiAction) -> Unit
) {
    DefaultTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        startValue = reference.startText,
        label = "Start text:",
        onValueChange = { newValue ->
            onUiAction(ClassDiagramUiAction.UpdateConnectionData {
                startText = newValue
            })
        },
        maxLength = 48,
        showEditIcon = false,
        textStyle = MaterialTheme.typography.bodySmall,
        labelTextStyle = MaterialTheme.typography.labelMedium,
        textColor = LocalAppTheme.current.primaryScreenText
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
                    mainColor = LocalAppTheme.current.primaryScreenText,
                    backgroundColor = LocalAppTheme.current.primaryScreenTextContainer,
                    highlightColor = LocalAppTheme.current.primaryScreenHighlightColor,
                    onClick = {
                        onUiAction(ClassDiagramUiAction.UpdateConnectionData {
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
                    mainColor = LocalAppTheme.current.primaryScreenText,
                    backgroundColor = LocalAppTheme.current.primaryScreenTextContainer,
                    highlightColor = LocalAppTheme.current.primaryScreenHighlightColor,
                    onClick = {
                        onUiAction(ClassDiagramUiAction.UpdateConnectionData {
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
                    mainColor = LocalAppTheme.current.primaryScreenText,
                    backgroundColor = LocalAppTheme.current.primaryScreenTextContainer,
                    highlightColor = LocalAppTheme.current.primaryScreenHighlightColor,
                    onClick = {
                        onUiAction(ClassDiagramUiAction.UpdateConnectionData {
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
            onUiAction(ClassDiagramUiAction.UpdateConnectionData {
                endText = newValue
            })
        },
        maxLength = 48,
        showEditIcon = false,
        textStyle = MaterialTheme.typography.bodySmall,
        labelTextStyle = MaterialTheme.typography.labelMedium,
        textColor = LocalAppTheme.current.primaryScreenText
    )
}