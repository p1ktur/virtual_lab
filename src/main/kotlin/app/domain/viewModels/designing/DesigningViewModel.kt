package app.domain.viewModels.designing

import androidx.compose.ui.geometry.*
import androidx.compose.ui.input.pointer.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.fileManager.*
import app.domain.util.list.*
import app.domain.util.numbers.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class DesigningViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DesigningUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            addComponent()
            addComponent()
            startConnectionOn(0)
            createConnectionOn(0)
            updateCommonCounter()
        }
    }

    fun onUiAction(action: DesigningUiAction) {
        viewModelScope.launch {
            when (action) {
                // Common
                DesigningUiAction.UpdateCommonCounter -> updateCommonCounter()

                // Components
                is DesigningUiAction.ClickOnComponent -> clickOnComponent(action.index, action.containment)
                DesigningUiAction.AddComponent -> addComponent()
                is DesigningUiAction.UpdateComponentData -> updateComponent(action.updater)
                is DesigningUiAction.DeleteComponent -> deleteComponent(action.index)
                is DesigningUiAction.DeleteField -> deleteComponentField(action.index)
                is DesigningUiAction.DeleteFunction -> deleteComponentFunction(action.index)

                // Connections
                is DesigningUiAction.ClickOnConnection -> clickOnConnection(action.index, action.containment)
                is DesigningUiAction.StartConnectionOn -> startConnectionOn(action.index)
                is DesigningUiAction.CreateConnectionOn -> createConnectionOn(action.index)
                is DesigningUiAction.UpdateConnectionData -> updateConnection(action.updater)
                is DesigningUiAction.UpdateConnectionStartRef -> updateConnectionStartRef(action.refConnection)
                is DesigningUiAction.UpdateConnectionEndRef -> updateConnectionEndRef(action.refConnection)
                is DesigningUiAction.DeleteConnection -> deleteConnection(action.index)

                // Edit mode
                is DesigningUiAction.UpdateEditMode -> updateEditMode(action.editMode)

                // Clearing
                DesigningUiAction.ClearAllReferences -> clearAllReferences()
                DesigningUiAction.ClearAllFocuses -> clearAllFocuses()
                DesigningUiAction.ClearComponentFocuses -> clearComponentFocuses()

                // Canvas actions
                is DesigningUiAction.UpdateCanvasOffset -> updateCanvasOffset(action.offset)
                is DesigningUiAction.UpdatePointerIcon -> updatePointerIcon(action.pointerIcon)
                is DesigningUiAction.UpdateCanvasSize -> updateCanvasSize(action.size)

                // Mouse actions
                is DesigningUiAction.MouseClick -> mouseClick(action.pointerInputChange)
                is DesigningUiAction.MouseMove -> mouseMove(action.pointerInputChange)
                is DesigningUiAction.MouseRelease -> mouseRelease()
                is DesigningUiAction.MouseScroll -> mouseScroll(action.pointerInputChange)
            }
        }
    }

    fun getSaveData(): SaveData {
        return SaveData(
            components = uiState.value.classComponents,
            connections = uiState.value.classConnections
        )
    }

    fun applySaveData(saveData: SaveData) {
        _uiState.update {
            it.copy(
                classComponents = saveData.components,
                classConnections = saveData.connections
            )
        }

        uiState.value.classConnections.forEach {
            it.findAndApplyCorrectReferences(uiState.value.classComponents)
        }

        updateCommonCounter()
    }

    //ACTIONS
    private fun updateCommonCounter() {
        _uiState.update {
            it.copy(
                commonCounter = it.commonCounter + 1
            )
        }
    }

    private fun clickOnComponent(index: Int, containment: ComponentContainmentResult) {
        _uiState.update {
            it.copy(
                classComponents = it.classComponents.toMutableList().swapWithLast(index),
            )
        }

        _uiState.update {
            it.copy(
                focusUiState = it.focusUiState.copy(
                    focusedComponent = it.classComponents.last(),
                    focusedConnection = null
                ),
                componentInFocus = true,
                componentSideInFocus = (containment as? ComponentContainmentResult.Side)?.direction,
                componentVertexInFocus = (containment as? ComponentContainmentResult.Vertex)?.direction,
                commonCounter = it.commonCounter + 1,
                canvasUiState = it.canvasUiState.copy(
                    mouseMoveEvent = null
                )
            )
        }
    }

    private fun addComponent() {
        var futurePos = uiState.value.canvasUiState.center - Offset(UMLClassComponent.MIN_WIDTH / 2, UMLClassComponent.MIN_HEIGHT / 2)
        val occupiedPositions = uiState.value.classComponents.map { it.position }

        while (occupiedPositions.contains(futurePos)) {
            futurePos += Offset(16f, 16f)
        }

        val newClassComponent = UMLClassComponent(
            position = futurePos
        )

        _uiState.update {
            it.copy(
                classComponents = it.classComponents + newClassComponent,
                commonCounter = it.commonCounter + 1
            )
        }
    }

    private fun updateComponent(updater: UMLClassComponent.() -> Unit) {
        uiState.value.classComponents.last().apply(updater)

        _uiState.update {
            it.copy(
                focusUiState = it.focusUiState.copy(
                    focusedComponent = it.classComponents.last().copy()
                )
            )
        }

        updateCommonCounter()
    }

    private fun deleteComponent(index: Int) {
        val componentToDelete = uiState.value.classComponents[index]

        _uiState.update {
            it.copy(
                focusUiState = it.focusUiState.copy(
                    focusedComponent = if (index == uiState.value.classComponents.lastIndex) null else it.focusUiState.focusedComponent
                ),
                classComponents = it.classComponents.toMutableList().apply { removeAt(index) },
                classConnections = it.classConnections.filterNot { con ->
                    con.startRef.ref == componentToDelete || con.endRef.ref == componentToDelete
                }
            )
        }

        updateCommonCounter()
    }

    private fun deleteComponentField(index: Int) {
        uiState.value.classComponents.last().fields.removeAt(index)

        uiState.value.classConnections.forEach { con ->
            if (((con.startRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Field)?.index == index) {
                con.startRef = RefConnection.SimpleConnection(con.startRef.ref)
            } else if (((con.endRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Field)?.index == index) {
                con.endRef = RefConnection.SimpleConnection(con.endRef.ref)
            }
        }

        updateCommonCounter()
    }

    private fun deleteComponentFunction(index: Int) {
        uiState.value.classComponents.last().functions.removeAt(index)

        uiState.value.classConnections.forEach { con ->
            if (((con.startRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Function)?.index == index) {
                con.startRef = RefConnection.SimpleConnection(con.startRef.ref)
            } else if (((con.endRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Function)?.index == index) {
                con.endRef = RefConnection.SimpleConnection(con.endRef.ref)
            }
        }

        updateCommonCounter()
    }


    private fun clickOnConnection(index: Int, containment: ConnectionContainmentResult) {
        _uiState.update {
            it.copy(
                classConnections = it.classConnections.toMutableList().swapWithLast(index),
            )
        }

        _uiState.update {
            it.copy(
                focusUiState = it.focusUiState.copy(
                    focusedComponent = null,
                    focusedConnection = it.classConnections.last()
                ),
                connectionInFocus = true,
                connectionSegmentInFocus = when (containment) {
                    ConnectionContainmentResult.None -> null
                    ConnectionContainmentResult.Whole -> null
                    is ConnectionContainmentResult.FirstSegment -> ConnectionSegment.FIRST
                    is ConnectionContainmentResult.SecondSegment -> ConnectionSegment.SECOND
                    is ConnectionContainmentResult.ThirdSegment -> ConnectionSegment.THIRD
                },
                commonCounter = it.commonCounter + 1,
                canvasUiState = it.canvasUiState.copy(
                    mouseMoveEvent = null
                )
            )
        }
    }

    private fun startConnectionOn(index: Int) {
        _uiState.update {
            it.copy(
                classComponents = it.classComponents.toMutableList().swapWithLast(index),
                componentInFocus = true,
                creatingConnection = true,
                canvasUiState = it.canvasUiState.copy(
                    mouseMoveEvent = null
                )
            )
        }
    }

    private fun createConnectionOn(index: Int) {
        val startRef = uiState.value.classComponents.last()

        _uiState.update {
            it.copy(
                classComponents = it.classComponents.toMutableList().swapWithLast(index),
            )
        }

        val newClassConnection = UMLClassConnection(
            startRef = RefConnection.SimpleConnection(ref = startRef),
            endRef = RefConnection.SimpleConnection(ref = uiState.value.classComponents.last())
        )

        _uiState.update {
            it.copy(
                classConnections = it.classConnections + newClassConnection,
                componentInFocus = true,
                creatingConnection = false,
                canvasUiState = it.canvasUiState.copy(
                    mouseMoveEvent = null
                )
            )
        }
    }

    private fun updateConnection(updater: UMLClassConnection.() -> Unit) {
        uiState.value.classConnections.last().apply(updater)

        _uiState.update {
            it.copy(
                focusUiState = it.focusUiState.copy(
                    focusedConnection = it.classConnections.last().copy()
                )
            )
        }

        updateCommonCounter()
    }

    private fun updateConnectionStartRef(refConnection: RefConnection) {
        updateConnection {
            startRef = refConnection
            forcedType = if (refConnection is RefConnection.ReferencedConnection) {
                UMLClassConnection.Type.HVH
            } else null
        }
    }

    private fun updateConnectionEndRef(refConnection: RefConnection) {
        updateConnection {
            endRef = refConnection
            forcedType = if (refConnection is RefConnection.ReferencedConnection) {
                UMLClassConnection.Type.HVH
            } else null
        }
    }

    private fun deleteConnection(index: Int) {
        _uiState.update {
            it.copy(
                focusUiState = it.focusUiState.copy(
                    focusedConnection = if (index == uiState.value.classConnections.lastIndex) null else it.focusUiState.focusedConnection
                ),
                classConnections = it.classConnections.toMutableList().apply { removeAt(index) }
            )
        }

        updateCommonCounter()
    }

    private fun updateEditMode(editMode: EditMode) {
        _uiState.update {
            it.copy(
                editMode = editMode
            )
        }
    }

    private fun clearAllReferences() {
        clearAllFocuses()

        _uiState.update {
            it.copy(
                focusUiState = it.focusUiState.copy(
                    focusedComponent = null,
                    focusedConnection = null
                )
            )
        }
    }

    private fun clearAllFocuses() {
        uiState.value.classComponents.forEach { it.clearHighlight() }
        uiState.value.classConnections.forEach { it.clearHighlight() }

        _uiState.update {
            it.copy(
                componentInFocus = false,
                componentSideInFocus = null,
                componentVertexInFocus = null,
                connectionInFocus = false,
                connectionSegmentInFocus = null,
                creatingConnection = false
            )
        }

        when (uiState.value.editMode) {
            EditMode.SELECTOR -> {
                _uiState.update {
                    it.copy(
                        creatingConnection = false
                    )
                }
            }
            EditMode.CONNECTOR -> Unit
        }
    }

    private fun clearComponentFocuses() {
        _uiState.update {
            it.copy(
                componentInFocus = false,
                componentSideInFocus = null,
                componentVertexInFocus = null
            )
        }
    }

    private fun updateCanvasOffset(offset: Offset) {
        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                    offset = offset
                )
            )
        }
    }

    private fun updatePointerIcon(pointerIcon: PointerIcon) {
        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                    cursorPointerIcon = pointerIcon
                )
            )
        }
    }

    private fun updateCanvasSize(size: Size) {
        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                    size = Size(size.width, size.height),
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            )
        }
    }

    private fun mouseClick(pointerInputChange: PointerInputChange) {
        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                    cachedOffset = it.canvasUiState.offset,
                    mouseClickEvent = pointerInputChange
                )
            )
        }
    }

    private fun mouseMove(pointerInputChange: PointerInputChange) {
        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                    mouseMoveEvent = pointerInputChange
                )
            )
        }
    }

    private fun mouseRelease() {
        uiState.value.classComponents.lastOrNull()?.applyResizing()

        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                    mouseClickEvent = null,
                    mouseMoveEvent = null
                )
            )
        }

        onUiAction(DesigningUiAction.ClearComponentFocuses)
    }

    private fun mouseScroll(pointerInputChange: PointerInputChange) {
        var delta = pointerInputChange.scrollDelta.x
        if (delta == 0f) delta = pointerInputChange.scrollDelta.y

        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                   zoom = (it.canvasUiState.zoom - delta * 0.1f).limit(0.1f, 10f)
                )
            )
        }
    }
}