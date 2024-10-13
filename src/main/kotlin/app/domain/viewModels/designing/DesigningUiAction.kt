package app.domain.viewModels.designing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.input.pointer.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import app.domain.umlDiagram.mouse.*

sealed interface DesigningUiAction {
    // Common
    data object UpdateCommonCounter : DesigningUiAction

    // Components
    data class ClickOnComponent(val index: Int, val containment: ComponentContainmentResult) : DesigningUiAction
    data object AddComponent : DesigningUiAction
    data class UpdateComponentData(val updater: UMLClassComponent.() -> Unit) : DesigningUiAction
    data class DeleteComponent(val index: Int) : DesigningUiAction
    data class DeleteField(val index: Int) : DesigningUiAction
    data class DeleteFunction(val index: Int) : DesigningUiAction

    // Connections
    data class ClickOnConnection(val index: Int, val containment: ConnectionContainmentResult) : DesigningUiAction
    data class StartConnectionOn(val index: Int) : DesigningUiAction
    data class CreateConnectionOn(val index: Int) : DesigningUiAction
    data class UpdateConnectionData(val updater: UMLClassConnection.() -> Unit) : DesigningUiAction
    data class UpdateConnectionStartRef(val refConnection: RefConnection) : DesigningUiAction
    data class UpdateConnectionEndRef(val refConnection: RefConnection) : DesigningUiAction
    data class DeleteConnection(val index: Int) : DesigningUiAction

    // Edit mode
    data class UpdateEditMode(val editMode: EditMode) : DesigningUiAction

    // Clearing
    data object ClearAllReferences : DesigningUiAction
    data object ClearAllFocuses : DesigningUiAction
    data object ClearComponentFocuses : DesigningUiAction

    // Canvas actions
    data class UpdateCanvasOffset(val offset: Offset) : DesigningUiAction
    data class UpdatePointerIcon(val pointerIcon: PointerIcon) : DesigningUiAction
    data class UpdateCanvasSize(val size: Size) : DesigningUiAction

    // Mouse actions
    data class MouseClick(val pointerInputChange: PointerInputChange) : DesigningUiAction
    data class MouseMove(val pointerInputChange: PointerInputChange) : DesigningUiAction
    data object MouseRelease : DesigningUiAction
    data class MouseScroll(val pointerInputChange: PointerInputChange) : DesigningUiAction
}