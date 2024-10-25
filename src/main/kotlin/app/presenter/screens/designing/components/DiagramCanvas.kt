package app.presenter.screens.designing.components

import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.geometry.*
import app.domain.util.list.*
import app.domain.viewModels.designing.*
import app.presenter.canvas.*
import app.presenter.canvas.arrows.*
import app.test.*
import java.awt.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DiagramCanvas(
    modifier: Modifier = Modifier,
    uiState: DesigningUiState,
    onUiAction: (DesigningUiAction) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val umlComponentNameTextStyle = MaterialTheme.typography.bodyMedium
    val umlComponentContentTextStyle = MaterialTheme.typography.bodySmall
    val umlConnectionTextStyle = MaterialTheme.typography.bodySmall

    fun Offset.scaledAndTranslated(): Offset = unZoom(uiState.canvasUiState.center, uiState.canvasUiState.zoom) - uiState.canvasUiState.offset

    // EDIT MODE CHANGE PROCESS
    LaunchedEffect(uiState.editMode) {
        onUiAction(DesigningUiAction.ClearAllFocuses)
    }

    // MOUSE CLICK PROCESS
    LaunchedEffect(uiState.canvasUiState.mouseClickEvent) {
        uiState.canvasUiState.mouseClickEvent?.let { event ->
            uiState.classComponents.forEachReversedIndexed { index, it ->
                when (val containment = it.containsMouse(event.position.scaledAndTranslated(), true)) {
                    ComponentContainmentResult.None -> Unit
                    else -> {
                        when (uiState.editMode) {
                            EditMode.SELECTOR -> {
                                onUiAction(DesigningUiAction.ClickOnComponent(index, containment))
                                return@LaunchedEffect
                            }
                            EditMode.CONNECTOR -> {
                                if (uiState.creatingConnection && index != uiState.classComponents.lastIndex) {
                                    onUiAction(DesigningUiAction.CreateConnectionOn(index))
                                } else {
                                    onUiAction(DesigningUiAction.StartConnectionOn(index))
                                }

                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                                return@LaunchedEffect
                            }
                        }
                    }
                }
            }

            uiState.classConnections.forEachReversedIndexed { index, it ->
                when (val containment = it.containsMouse(event.position.scaledAndTranslated())) {
                    ConnectionContainmentResult.None -> Unit
                    else -> {
                        onUiAction(DesigningUiAction.ClickOnConnection(index, containment))
                        return@LaunchedEffect
                    }
                }
            }

            onUiAction(DesigningUiAction.ClearAllFocuses)
        }
    }

    // MOUSE MOVEMENT PROCESS
    LaunchedEffect(uiState.componentInFocus, uiState.connectionInFocus, uiState.canvasUiState.mouseMoveEvent) {
        when (uiState.editMode) {
            EditMode.SELECTOR -> uiState.canvasUiState.mouseMoveEvent?.let { event ->
                if (uiState.canvasUiState.mouseClickEvent != null) uiState.canvasUiState.mouseClickEvent.let { clickEvent ->
                    // MOUSE CLICK AND MOVE
                    when {
                        uiState.componentInFocus && uiState.classComponents.isNotEmpty() -> when {
                            uiState.componentSideInFocus != null -> {
                                when (uiState.componentSideInFocus) {
                                    SideDirection.LEFT -> {
                                        uiState.classComponents.last().resizeLeftBy(
                                            (event.position.x - clickEvent.position.x).div(uiState.canvasUiState.zoom).smoothen()
                                        )
                                        onUiAction(DesigningUiAction.UpdateCommonCounter)
                                    }
                                    SideDirection.RIGHT -> {
                                        uiState.classComponents.last().resizeRightBy(
                                            (event.position.x - clickEvent.position.x).div(uiState.canvasUiState.zoom).smoothen()
                                        )
                                        onUiAction(DesigningUiAction.UpdateCommonCounter)
                                    }
                                    else -> Unit
                                }
                            }
                            uiState.componentVertexInFocus != null -> Unit
                            else -> {
                                uiState.classComponents.last().moveTo(event.position.scaledAndTranslated())
                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                            }
                        }
                        uiState.connectionInFocus && uiState.classConnections.isNotEmpty() -> when {
                            uiState.connectionSegmentInFocus != null -> {
                                when (uiState.connectionSegmentInFocus) {
                                    ConnectionSegment.FIRST -> {
                                        val segmentOffset = uiState.classConnections.last().calculateSegmentOffset(
                                            ConnectionSegment.FIRST,
                                            event.position.scaledAndTranslated().smoothen()
                                        )

                                        uiState.classConnections.last().setSegmentOffset(ConnectionSegment.FIRST, segmentOffset)
                                        onUiAction(DesigningUiAction.UpdateCommonCounter)
                                    }
                                    ConnectionSegment.SECOND -> {
                                        val segmentOffset = uiState.classConnections.last().calculateSegmentOffset(
                                            ConnectionSegment.SECOND,
                                            event.position.scaledAndTranslated().smoothen()
                                        )

                                        uiState.classConnections.last().setSegmentOffset(ConnectionSegment.SECOND, segmentOffset)
                                        onUiAction(DesigningUiAction.UpdateCommonCounter)
                                    }
                                    ConnectionSegment.THIRD -> {
                                        val segmentOffset = uiState.classConnections.last().calculateSegmentOffset(
                                            ConnectionSegment.THIRD,
                                            event.position.scaledAndTranslated().smoothen()
                                        )

                                        uiState.classConnections.last().setSegmentOffset(ConnectionSegment.THIRD, segmentOffset)
                                        onUiAction(DesigningUiAction.UpdateCommonCounter)
                                    }
                                }
                            }
                            else -> Unit
                        }
                        else -> {
                            onUiAction(DesigningUiAction.UpdateCanvasOffset(
                                offset = uiState.canvasUiState.cachedOffset + Offset(
                                    event.position.x - clickEvent.position.x,
                                    event.position.y - clickEvent.position.y
                                ).div(uiState.canvasUiState.zoom)
                            ))
                            onUiAction(DesigningUiAction.UpdateCommonCounter)
                        }
                    }
                } else {
                    // MOUSE MOVE AND NOT CLICKED
                    uiState.classComponents.forEachReversedIndexed { index, it ->
                        when (val containment = it.containsMouse(event.position.scaledAndTranslated(), false)) {
                            ComponentContainmentResult.None -> {
                                onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))))
                            }
                            ComponentContainmentResult.Whole -> {
                                onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.MOVE_CURSOR))))

                                uiState.classComponents.forEachIndexed { i, com -> if (i != index) com.clearHighlight() }
                                uiState.classConnections.forEach { con -> con.clearHighlight() }
                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                                return@LaunchedEffect
                            }
                            is ComponentContainmentResult.Side -> {
                                if (containment.direction == SideDirection.LEFT || containment.direction == SideDirection.RIGHT) {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))))
                                } else {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))))
                                }

                                uiState.classComponents.forEachIndexed { i, com -> if (i != index) com.clearHighlight() }
                                uiState.classConnections.forEach { con -> con.clearHighlight() }
                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                                return@LaunchedEffect
                            }
                            is ComponentContainmentResult.Vertex -> {
                                onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))))

                                uiState.classComponents.forEachIndexed { i, com -> if (i != index) com.clearHighlight() }
                                uiState.classConnections.forEach { con -> con.clearHighlight() }
                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                                return@LaunchedEffect
                            }
                        }
                    }

                    uiState.classConnections.forEachReversedIndexed { index, it ->
                        when (val containment = it.containsMouse(event.position.scaledAndTranslated())) {
                            ConnectionContainmentResult.None -> {
                                onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))))
                            }
                            ConnectionContainmentResult.Whole -> {
                                onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.DEFAULT_CURSOR))))
                            }
                            is ConnectionContainmentResult.FirstSegment -> {
                                if (containment.direction == SegmentDirection.VERTICAL) {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))))
                                } else {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))))
                                }

                                uiState.classConnections.forEachIndexed { i, con -> if (i != index) con.clearHighlight() }
                                uiState.classComponents.forEach { com -> com.clearHighlight() }
                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                                return@LaunchedEffect
                            }
                            is ConnectionContainmentResult.SecondSegment -> {
                                if (containment.direction == SegmentDirection.VERTICAL) {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))))
                                } else {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))))
                                }

                                uiState.classConnections.forEachIndexed { i, con -> if (i != index) con.clearHighlight() }
                                uiState.classComponents.forEach { com -> com.clearHighlight() }
                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                                return@LaunchedEffect
                            }
                            is ConnectionContainmentResult.ThirdSegment -> {
                                if (containment.direction == SegmentDirection.VERTICAL) {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))))
                                } else {
                                    onUiAction(DesigningUiAction.UpdatePointerIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))))
                                }

                                uiState.classConnections.forEachIndexed { i, con -> if (i != index) con.clearHighlight() }
                                uiState.classComponents.forEach { com -> com.clearHighlight() }
                                onUiAction(DesigningUiAction.UpdateCommonCounter)
                                return@LaunchedEffect
                            }
                        }
                    }

                    onUiAction(DesigningUiAction.UpdateCommonCounter)
                }
            }
            EditMode.CONNECTOR -> {
                uiState.canvasUiState.mouseMoveEvent?.let { event ->
                    uiState.classComponents.forEachReversed {
                        it.containsMouse(event.position.scaledAndTranslated(), false)
                        onUiAction(DesigningUiAction.UpdateCommonCounter)
                    }
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .pointerHoverIcon(uiState.canvasUiState.cursorPointerIcon)
            .onPointerEvent(PointerEventType.Press) { event ->
                onUiAction(DesigningUiAction.MouseClick(event.changes.last()))
            }
            .onPointerEvent(PointerEventType.Move) { event ->
                onUiAction(DesigningUiAction.MouseMove(event.changes.last()))
            }
            .onPointerEvent(PointerEventType.Release) {
                onUiAction(DesigningUiAction.MouseRelease)
            }
            .onPointerEvent(PointerEventType.Scroll) { event ->
                onUiAction(DesigningUiAction.MouseScroll(event.changes.last()))
            }
            .onSizeChanged { size ->
                onUiAction(DesigningUiAction.UpdateCanvasSize(Size(size.width.toFloat(), size.height.toFloat())))
            }
            .addTestTag("Diagram Canvas")
    ) {
        uiState.commonCounter.let {
            drawGrid(
                step = 24f * uiState.canvasUiState.zoom,
                canvasZoom = uiState.canvasUiState.zoom,
                canvasOffset = uiState.canvasUiState.offset
            )
            scale(uiState.canvasUiState.zoom) {
                translate(uiState.canvasUiState.offset.x, uiState.canvasUiState.offset.y) {
                    drawCenterHelper(
                        squareSize = 24f / uiState.canvasUiState.zoom
                    )

                    uiState.classConnections.forEach {
                        it.drawOn(
                            drawScope = this@Canvas,
                            textMeasurer = textMeasurer,
                            textStyle = umlConnectionTextStyle,
                            componentNameTextStyle = umlComponentNameTextStyle,
                            componentContentTextStyle = umlComponentContentTextStyle
                        )
                    }

                    uiState.classComponents.forEach {
                        it.drawOn(
                            drawScope = this@Canvas,
                            textMeasurer = textMeasurer,
                            nameTextStyle = umlComponentNameTextStyle,
                            contentTextStyle = umlComponentContentTextStyle
                        )
                    }

                    if (uiState.creatingConnection) uiState.classComponents.lastOrNull()?.let { component ->
                        val origin = component.position.plus(Offset(component.size.width / 2, component.size.height / 2))
                        drawArrowFromTo(
                            from = origin,
                            to = uiState.canvasUiState.mouseMoveEvent?.position?.scaledAndTranslated() ?: origin,
                            color = Color(UMLClassComponent.HIGHLIGHT_COLOR)
                        )
                    }
                }
            }
        }
    }
}