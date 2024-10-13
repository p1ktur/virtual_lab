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
fun FunctionsChooseList(
    reference: UMLClassConnection,
    componentRef: UMLClassComponent,
    isStart: Boolean,
    commonCounter: Int,
    onUiAction: (DesigningUiAction) -> Unit
) {
    if (componentRef.functions.isEmpty()) return

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        text = "Functions",
        style = MaterialTheme.typography.bodySmall,
        color = Color.Black
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        componentRef.functions.forEachIndexed { index, functions ->
            key(commonCounter) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isStart) {
                                val startRef = reference.startRef
                                val newConnection = if (startRef is RefConnection.ReferencedConnection &&
                                    startRef.refType is RefType.Function &&
                                    startRef.refType.index == index) {
                                    RefConnection.SimpleConnection(componentRef)
                                } else {
                                    RefConnection.ReferencedConnection(componentRef, RefType.Function(index))
                                }

                                onUiAction(DesigningUiAction.UpdateConnectionStartRef(newConnection))
                            } else {
                                val endRef = reference.endRef
                                val newConnection = if (endRef is RefConnection.ReferencedConnection && endRef.refType.index == index) {
                                    RefConnection.SimpleConnection(componentRef)
                                } else {
                                    RefConnection.ReferencedConnection(componentRef, RefType.Function(index))
                                }

                                onUiAction(DesigningUiAction.UpdateConnectionEndRef(newConnection))
                            }
                        }
                        .padding(4.dp),
                    text = functions.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = (if (isStart) reference.startRef else reference.endRef).run {
                        ((this as? RefConnection.ReferencedConnection)?.refType as? RefType.Function)?.let {
                            if (it.index == index) Color.Blue else Color.Black
                        } ?: Color.Black
                    },
                    textDecoration = if (functions.isStatic) TextDecoration.Underline else null
                )
            }
        }
    }
}