package app.presenter.screens.designing.components

import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.model.components.*
import app.domain.umlDiagram.model.connections.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.geometry.*
import app.domain.util.list.*
import app.presenter.canvas.*
import java.awt.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DiagramCanvas(
    modifier: Modifier = Modifier,
    classComponents: SnapshotStateList<UMLClassComponent>,
    classConnections: SnapshotStateList<UMLClassConnection>,
    diagramInFocus: MutableState<Boolean>,
    diagramSideInFocus: MutableState<SideDirection?>,
    diagramVertexInFocus: MutableState<VertexDirection?>,
    focusedDiagramReference: MutableState<UMLClassComponent?>,
    editMode: MutableState<EditMode>,
    updateCounter: MutableState<Int>
) {
    // Canvas data
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val canvasCenter = remember(canvasSize) { Offset(canvasSize.width / 2f, canvasSize.height / 2f) }

    var canvasZoom by remember { mutableFloatStateOf(1f) }

    var cachedCanvasOffset by remember { mutableStateOf(Offset.Zero) }
    var canvasOffset by remember { mutableStateOf(Offset.Zero) }

    val textMeasurer = rememberTextMeasurer()
    val umlClassTextStyle = MaterialTheme.typography.bodyMedium

    // Mouse data
    var mouseClickEvent by remember { mutableStateOf<PointerInputChange?>(null) }

    var mouseMoveEvent by remember { mutableStateOf<PointerInputChange?>(null) }

    var cursorPointerIcon by remember { mutableStateOf(PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))) }

    fun Offset.scaledAndTranslated(canvasOffset: Offset, canvasCenter: Offset, canvasZoom: Float): Offset = unZoom(canvasCenter, canvasZoom) - canvasOffset

    // Connection Data

    var creatingConnection by remember { mutableStateOf(false) }

    LaunchedEffect(editMode.value) {
        classComponents.forEach { it.clearHighlight() }
        diagramInFocus.value = false
        diagramSideInFocus.value = null
        diagramVertexInFocus.value = null

        when (editMode.value) {
            EditMode.SELECTOR -> {
                creatingConnection = false
            }
            EditMode.CONNECTOR -> Unit
        }
    }

    LaunchedEffect(mouseClickEvent) {
        mouseClickEvent?.let { event ->
            classComponents.forEachReversedIndexed { index, it ->
                when (val containment = it.containsMouse(event.position.scaledAndTranslated(canvasOffset, canvasCenter, canvasZoom), true)) {
                    ContainmentResult.None -> Unit
                    else -> {
                        when (editMode.value) {
                            EditMode.SELECTOR -> {
                                mouseMoveEvent = null

                                classComponents.swapWithLast(index)
                                focusedDiagramReference.value = classComponents.last()

                                diagramInFocus.value = true

                                if (containment is ContainmentResult.Side) {
                                    diagramSideInFocus.value = containment.direction
                                } else if (containment is ContainmentResult.Vertex) {
                                    diagramVertexInFocus.value = containment.direction
                                }

                                updateCounter.value++
                                return@LaunchedEffect
                            }
                            EditMode.CONNECTOR -> {
                                mouseMoveEvent = null

                                classComponents.swapWithLast(index)
                                diagramInFocus.value = true

                                creatingConnection = true

                                updateCounter.value++
                                return@LaunchedEffect
                            }
                        }
                    }
                }
            }

            classComponents.forEach { it.clearHighlight() }
            diagramInFocus.value = false
            focusedDiagramReference.value = null
            diagramSideInFocus.value = null
            diagramVertexInFocus.value = null

            creatingConnection = false
        }
    }

    LaunchedEffect(diagramInFocus, mouseMoveEvent) {
        if (classComponents.isEmpty()) return@LaunchedEffect

        when (editMode.value) {
            EditMode.SELECTOR -> mouseMoveEvent?.let { event ->
                if (mouseClickEvent != null) mouseClickEvent?.let { clickEvent ->
                    if (diagramInFocus.value) {
                        when {
                            diagramSideInFocus.value != null -> {
                                when (diagramSideInFocus.value) {
                                    SideDirection.LEFT -> {
                                        classComponents.last().resizeLeftBy((event.position.x - clickEvent.position.x).div(canvasZoom))
                                        updateCounter.value++
                                    }
                                    SideDirection.RIGHT -> {
                                        classComponents.last().resizeRightBy((event.position.x - clickEvent.position.x).div(canvasZoom))
                                        updateCounter.value++
                                    }
                                    else -> Unit
                                }
                            }
                            diagramVertexInFocus.value != null -> Unit
                            else -> {
                                classComponents.last().moveTo(event.position.scaledAndTranslated(canvasOffset, canvasCenter, canvasZoom))
                                updateCounter.value++
                            }
                        }
                    } else {
                        canvasOffset = cachedCanvasOffset + Offset(
                            event.position.x - clickEvent.position.x,
                            event.position.y - clickEvent.position.y
                        ).div(canvasZoom)
                        updateCounter.value++
                    }
                } else {
                    classComponents.forEachReversed {
                        when (val containment = it.containsMouse(event.position.scaledAndTranslated(canvasOffset, canvasCenter, canvasZoom), false)) {
                            ContainmentResult.None -> {
                                cursorPointerIcon = PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))
                            }
                            ContainmentResult.Whole -> {
                                cursorPointerIcon = PointerIcon(Cursor(Cursor.MOVE_CURSOR))

                                updateCounter.value++
                                return@LaunchedEffect
                            }
                            is ContainmentResult.Side -> {
                                if (containment.direction == SideDirection.LEFT || containment.direction == SideDirection.RIGHT) {
                                    cursorPointerIcon = PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))
                                } else {
                                    cursorPointerIcon = PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))
                                }

                                updateCounter.value++
                                return@LaunchedEffect
                            }
                            is ContainmentResult.Vertex -> {
                                cursorPointerIcon = PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))

                                updateCounter.value++
                                return@LaunchedEffect
                            }
                        }
                    }

                    updateCounter.value++
                }
            }
            EditMode.CONNECTOR -> {
                mouseMoveEvent?.let { event ->
                    classComponents.forEachReversed {
                        it.containsMouse(event.position.scaledAndTranslated(canvasOffset, canvasCenter, canvasZoom), false)
                        updateCounter.value++
                    }
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .pointerHoverIcon(cursorPointerIcon)
            .onPointerEvent(PointerEventType.Press) { event ->
                cachedCanvasOffset = canvasOffset
                mouseClickEvent = event.changes.last()
            }
            .onPointerEvent(PointerEventType.Move) { event ->
                mouseMoveEvent = event.changes.last()
            }
            .onPointerEvent(PointerEventType.Release) {
                classComponents.last().applyResizing()

                diagramInFocus.value = false
                diagramSideInFocus.value = null
                diagramVertexInFocus.value = null

                mouseClickEvent = null
                mouseMoveEvent = null
            }
            .onPointerEvent(PointerEventType.Scroll) { event ->
                var delta = event.changes.last().scrollDelta.x
                if (delta == 0f) delta = event.changes.last().scrollDelta.y
                canvasZoom -= delta * 0.1f
                canvasZoom = kotlin.math.max(0.1f, kotlin.math.min(10f, canvasZoom))
            }
            .onSizeChanged { size ->
                canvasSize = Size(size.width.toFloat(), size.height.toFloat())
            }
    ) {
        updateCounter.value.let {
            drawGrid(
                step = 24f * canvasZoom,
                canvasZoom = canvasZoom,
                canvasOffset = canvasOffset
            )
            scale(canvasZoom) {
                translate(canvasOffset.x, canvasOffset.y) {
                    drawCenterHelper(
                        squareSize = 24f / canvasZoom
                    )

                    classComponents.forEach {
                        it.drawOn(this@Canvas, textMeasurer, umlClassTextStyle)
                    }

                    if (creatingConnection) classComponents.lastOrNull()?.let { component ->
                        val origin = component.position.plus(Offset(component.size.width / 2, component.size.height / 2))
                        drawArrowFromTo(
                            from = origin,
                            to = mouseMoveEvent?.position?.scaledAndTranslated(canvasOffset, canvasCenter, canvasZoom) ?: origin,
                            color = Color(UMLClassComponent.HIGHLIGHT_COLOR)
                        )
                    }
                }
            }
        }
    }
}