package app.domain.viewModels.diagrams.classDiagram

import androidx.compose.ui.geometry.*
import androidx.compose.ui.input.pointer.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.domain.umlDiagram.mouse.*

sealed interface ClassDiagramUiAction {
    // Common
    data object FetchData : ClassDiagramUiAction
    data object UpdateCommonCounter : ClassDiagramUiAction
    data object SaveChanges : ClassDiagramUiAction

    // Components
    data class ClickOnComponent(val index: Int, val containment: ComponentContainmentResult) : ClassDiagramUiAction
    data object AddComponent : ClassDiagramUiAction
    data class UpdateComponentData(val updater: UMLClassComponent.() -> Unit) : ClassDiagramUiAction
    data class DeleteComponent(val index: Int) : ClassDiagramUiAction
    data class DeleteField(val index: Int) : ClassDiagramUiAction
    data class DeleteFunction(val index: Int) : ClassDiagramUiAction

    // Connections
    data class ClickOnConnection(val index: Int, val containment: ConnectionContainmentResult) : ClassDiagramUiAction
    data class StartConnectionOn(val index: Int) : ClassDiagramUiAction
    data class CreateConnectionOn(val index: Int) : ClassDiagramUiAction
    data class UpdateConnectionData(val updater: UMLClassConnection.() -> Unit) : ClassDiagramUiAction
    data class UpdateConnectionStartRef(val refConnection: RefConnection) : ClassDiagramUiAction
    data class UpdateConnectionEndRef(val refConnection: RefConnection) : ClassDiagramUiAction
    data class DeleteConnection(val index: Int) : ClassDiagramUiAction

    // Edit mode
    data class UpdateEditMode(val editMode: EditMode) : ClassDiagramUiAction

    // Clearing
    data object ClearAllReferences : ClassDiagramUiAction
    data object ClearAllFocuses : ClassDiagramUiAction
    data object ClearComponentFocuses : ClassDiagramUiAction

    // Canvas actions
    data class UpdateCanvasOffset(val offset: Offset) : ClassDiagramUiAction
    data class UpdatePointerIcon(val pointerIcon: PointerIcon) : ClassDiagramUiAction
    data class UpdateCanvasSize(val size: Size) : ClassDiagramUiAction

    // Mouse actions
    data class MouseClick(val pointerInputChange: PointerInputChange) : ClassDiagramUiAction
    data class MouseMove(val pointerInputChange: PointerInputChange) : ClassDiagramUiAction
    data object MouseRelease : ClassDiagramUiAction
    data class MouseScroll(val pointerInputChange: PointerInputChange) : ClassDiagramUiAction
}