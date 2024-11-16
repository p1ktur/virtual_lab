package app.presenter.screens.classDiagram.components.connectionData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.theme.*

@Composable
fun FieldsChooseList(
    reference: UMLClassConnection,
    componentRef: UMLClassComponent,
    isStart: Boolean,
    commonCounter: Int,
    onUiAction: (ClassDiagramUiAction) -> Unit
) {
    if (componentRef.fields.isEmpty()) return

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        text = "Fields",
        style = MaterialTheme.typography.bodySmall,
        color = LocalAppTheme.current.text
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        componentRef.fields.forEachIndexed { index, field ->
            key(commonCounter) {
                val isChosen = remember(commonCounter) {
                    (if (isStart) reference.startRef else reference.endRef).run {
                        ((this as? RefConnection.ReferencedConnection)?.refType as? RefType.Field)?.let {
                            it.index == index
                        } ?: false
                    }
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isStart) {
                                val startRef = reference.startRef
                                val newConnection = if (startRef is RefConnection.ReferencedConnection &&
                                    startRef.refType is RefType.Field &&
                                    startRef.refType.index == index) {
                                    RefConnection.SimpleConnection(componentRef)
                                } else {
                                    RefConnection.ReferencedConnection(componentRef, RefType.Field(index))
                                }

                                onUiAction(ClassDiagramUiAction.UpdateConnectionStartRef(newConnection))
                            } else {
                                val endRef = reference.endRef
                                val newConnection = if (endRef is RefConnection.ReferencedConnection && endRef.refType.getTypeIndex() == index) {
                                    RefConnection.SimpleConnection(componentRef)
                                } else {
                                    RefConnection.ReferencedConnection(componentRef, RefType.Field(index))
                                }

                                onUiAction(ClassDiagramUiAction.UpdateConnectionEndRef(newConnection))
                            }
                        }
                        .background(
                            if (isChosen) {
                                LocalAppTheme.current.highlightColor.copy(alpha = 0.1f)
                            } else if (index % 2 == 1) {
                                Color.Black.copy(0.1f)
                            } else {
                                Color.Transparent
                            }
                        )
                        .padding(4.dp),
                    text = field.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = (if (isStart) reference.startRef else reference.endRef).run {
                        ((this as? RefConnection.ReferencedConnection)?.refType as? RefType.Field)?.let {
                            if (it.index == index) LocalAppTheme.current.highlightColor else LocalAppTheme.current.text
                        } ?: LocalAppTheme.current.text
                    },
                    textDecoration = if (field.isStatic) TextDecoration.Underline else null
                )
            }
        }
    }
}