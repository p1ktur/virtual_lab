package app.domain.viewModels.designing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.input.pointer.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import app.domain.umlDiagram.mouse.*
import java.awt.*

data class DesigningUiState(
    // Common
    val commonCounter: Int = 0,
    // Components
    val classComponents: List<UMLClassComponent> = emptyList(),
    val componentInFocus: Boolean = false,
    val componentSideInFocus: SideDirection? = null,
    val componentVertexInFocus: VertexDirection? = null,
    // Connections
    val classConnections: List<UMLClassConnection> = emptyList(),
    val connectionInFocus: Boolean = false,
    val connectionSegmentInFocus: ConnectionSegment? = null,
    val creatingConnection: Boolean = false,
    // Edit mode
    val editMode: EditMode = EditMode.SELECTOR,
    // Canvas data
    val canvasUiState: CanvasUiState = CanvasUiState(),
    // Focus
    val focusUiState: FocusUiState = FocusUiState()
) {
    data class CanvasUiState(
        // Config
        val size: Size = Size.Zero,
        val center: Offset = Offset.Zero,
        val zoom: Float = 1f,
        val offset: Offset = Offset.Zero,
        val cachedOffset: Offset = Offset.Zero,
        // Mouse data
        val mouseClickEvent: PointerInputChange? = null,
        val mouseMoveEvent: PointerInputChange? = null,
        val cursorPointerIcon: PointerIcon = PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))
    )

    data class FocusUiState(
        // Component
        val focusedComponent: UMLClassComponent? = null,
        // Connection
        val focusedConnection: UMLClassConnection? = null
    )
}