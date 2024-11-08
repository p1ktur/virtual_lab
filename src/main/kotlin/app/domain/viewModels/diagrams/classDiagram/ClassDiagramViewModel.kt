package app.domain.viewModels.diagrams.classDiagram

import androidx.compose.ui.geometry.*
import androidx.compose.ui.input.pointer.*
import app.data.fileManager.*
import app.data.server.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.list.*
import app.domain.util.numbers.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json.Default.decodeFromString
import moe.tlaster.precompose.viewmodel.*

class ClassDiagramViewModel(
    private val taskId: Int?,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClassDiagramUiState())
    val uiState = _uiState.asStateFlow()



    fun onUiAction(action: ClassDiagramUiAction) {
        viewModelScope.launch {
            when (action) {
                // Common
                ClassDiagramUiAction.FetchData -> Unit
                ClassDiagramUiAction.UpdateCommonCounter -> updateCommonCounter()
                ClassDiagramUiAction.SaveChanges -> Unit

                // Components
                is ClassDiagramUiAction.ClickOnComponent -> clickOnComponent(action.index, action.containment)
                ClassDiagramUiAction.AddComponent -> addComponent()
                is ClassDiagramUiAction.UpdateComponentData -> updateComponent(action.updater)
                is ClassDiagramUiAction.DeleteComponent -> deleteComponent(action.index)
                is ClassDiagramUiAction.DeleteField -> deleteComponentField(action.index)
                is ClassDiagramUiAction.DeleteFunction -> deleteComponentFunction(action.index)

                // Connections
                is ClassDiagramUiAction.ClickOnConnection -> clickOnConnection(action.index, action.containment)
                is ClassDiagramUiAction.StartConnectionOn -> startConnectionOn(action.index)
                is ClassDiagramUiAction.CreateConnectionOn -> createConnectionOn(action.index)
                is ClassDiagramUiAction.UpdateConnectionData -> updateConnection(action.updater)
                is ClassDiagramUiAction.UpdateConnectionStartRef -> updateConnectionStartRef(action.refConnection)
                is ClassDiagramUiAction.UpdateConnectionEndRef -> updateConnectionEndRef(action.refConnection)
                is ClassDiagramUiAction.DeleteConnection -> deleteConnection(action.index)

                // Edit mode
                is ClassDiagramUiAction.UpdateEditMode -> updateEditMode(action.editMode)

                // Clearing
                ClassDiagramUiAction.ClearAllReferences -> clearAllReferences()
                ClassDiagramUiAction.ClearAllFocuses -> clearAllFocuses()
                ClassDiagramUiAction.ClearComponentFocuses -> clearComponentFocuses()

                // Canvas actions
                is ClassDiagramUiAction.UpdateCanvasOffset -> updateCanvasOffset(action.offset)
                is ClassDiagramUiAction.UpdatePointerIcon -> updatePointerIcon(action.pointerIcon)
                is ClassDiagramUiAction.UpdateCanvasSize -> updateCanvasSize(action.size)

                // Mouse actions
                is ClassDiagramUiAction.MouseClick -> mouseClick(action.pointerInputChange)
                is ClassDiagramUiAction.MouseMove -> mouseMove(action.pointerInputChange)
                is ClassDiagramUiAction.MouseRelease -> mouseRelease()
                is ClassDiagramUiAction.MouseScroll -> mouseScroll(action.pointerInputChange)
            }
        }
    }

    fun getSaveData(): SaveData {
        return SaveData(
            components = uiState.value.classComponents,
            connections = uiState.value.classConnections.map {
                it.toSerializable(uiState.value.classComponents)
            }
        )
    }

    fun applySaveData(saveData: SaveData) {
        _uiState.update {
            it.copy(
                classComponents = saveData.components,
                classConnections = saveData.connections.map { c -> c.toOriginal(saveData.components) },
                focusUiState = ClassDiagramUiState.FocusUiState()
            )
        }

        updateCommonCounter()
    }

    //ACTIONS
    private fun fetchData() {
        taskId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val task = serverRepository.getTask(id)

                val saveData = ServerJson.get().decodeFromString<SaveData>(task.diagramJson)
                applySaveData(saveData)
            }
        }
    }

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
                focusUiState = it.focusUiState.copy(
                    focusedComponent = it.classComponents[index] to it.classComponents.lastIndex,
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
                    focusedComponent = it.classComponents.last().copy() to it.classComponents.lastIndex
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
                    con.startRef.getRefClass() == componentToDelete || con.endRef.getRefClass() == componentToDelete
                }
            )
        }

        updateCommonCounter()
    }

    private fun deleteComponentField(index: Int) {
        uiState.value.classComponents.last().fields.removeAt(index)

        uiState.value.classConnections.forEach { con ->
            if (((con.startRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Field)?.index == index) {
                con.startRef = RefConnection.SimpleConnection(con.startRef.getRefClass())
            } else if (((con.endRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Field)?.index == index) {
                con.endRef = RefConnection.SimpleConnection(con.endRef.getRefClass())
            }
        }

        updateCommonCounter()
    }

    private fun deleteComponentFunction(index: Int) {
        uiState.value.classComponents.last().functions.removeAt(index)

        uiState.value.classConnections.forEach { con ->
            if (((con.startRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Function)?.index == index) {
                con.startRef = RefConnection.SimpleConnection(con.startRef.getRefClass())
            } else if (((con.endRef as? RefConnection.ReferencedConnection)?.refType as? RefType.Function)?.index == index) {
                con.endRef = RefConnection.SimpleConnection(con.endRef.getRefClass())
            }
        }

        updateCommonCounter()
    }


    private fun clickOnConnection(index: Int, containment: ConnectionContainmentResult) {
        _uiState.update {
            it.copy(
                classConnections = it.classConnections.toMutableList().swapWithLast(index),
                focusUiState = it.focusUiState.copy(
                    focusedComponent = null,
                    focusedConnection = it.classConnections[index] to it.classConnections.lastIndex
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

        val newClassConnection = UMLClassConnection(
            startRef = RefConnection.SimpleConnection(ref = startRef),
            endRef = RefConnection.SimpleConnection(ref = uiState.value.classComponents[index])
        )

        _uiState.update {
            it.copy(
                classComponents = it.classComponents.toMutableList().swapWithLast(index),
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
                    focusedConnection = it.classConnections.last().copy() to it.classConnections.lastIndex
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
                    mouseMoveEvent = null,
                    cursorPointerIcon = if (!it.connectionInFocus && !it.componentInFocus) {
                        PointerIcon.Default
                    } else {
                        it.canvasUiState.cursorPointerIcon
                    }
                )
            )
        }

        onUiAction(ClassDiagramUiAction.ClearComponentFocuses)
    }

    private fun mouseScroll(pointerInputChange: PointerInputChange) {
        var delta = pointerInputChange.scrollDelta.x
        if (delta == 0f) delta = pointerInputChange.scrollDelta.y

        _uiState.update {
            it.copy(
                canvasUiState = it.canvasUiState.copy(
                   zoom = (it.canvasUiState.zoom - delta * 0.1f).limit(0.1f, 5f)
                )
            )
        }
    }
}