package app.presenter.screens.designing.components.connectionData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import app.domain.viewModels.designing.*

@Composable
fun FieldsChooseList(
    reference: UMLClassConnection,
    componentRef: UMLClassComponent,
    isStart: Boolean,
    commonCounter: Int,
    onUiAction: (DesigningUiAction) -> Unit
) {
    if (componentRef.fields.isEmpty()) return

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        text = "Fields",
        style = MaterialTheme.typography.bodySmall,
        color = Color.Black
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

                                onUiAction(DesigningUiAction.UpdateConnectionStartRef(newConnection))
                            } else {
                                val endRef = reference.endRef
                                val newConnection = if (endRef is RefConnection.ReferencedConnection && endRef.refType.index == index) {
                                    RefConnection.SimpleConnection(componentRef)
                                } else {
                                    RefConnection.ReferencedConnection(componentRef, RefType.Field(index))
                                }

                                onUiAction(DesigningUiAction.UpdateConnectionEndRef(newConnection))
                            }
                        }
                        .background(if (isChosen) Color.LightGray else Color.Transparent)
                        .padding(4.dp),
                    text = field.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    textDecoration = if (field.isStatic) TextDecoration.Underline else null
                )
            }
        }
    }
}