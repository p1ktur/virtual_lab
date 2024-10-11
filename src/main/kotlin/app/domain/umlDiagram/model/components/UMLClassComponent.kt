package app.domain.umlDiagram.model.components

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.geometry.*
import kotlin.math.*

data class UMLClassComponent(
    var position: Offset = Offset(100f, 100f),
    var size: Size = Size(MIN_WIDTH, MIN_HEIGHT),
    var name: String = "Class",
    var isHighlighted: Boolean = false,
    val highlightedSides: MutableList<SideDirection> = mutableListOf(),
    val highlightedVertices: MutableList<VertexDirection> = mutableListOf()
) : UMLDiagramComponent {

    private var containedClickPosition: Offset? = null

    private var cachedSize: Size? = null
    private var cachedPosition: Offset? = null

    companion object {
        const val MIN_WIDTH = 144f
        const val MIN_HEIGHT = 72f

        const val DRAW_LINES_WIDTH = 2f
        const val DRAW_POINTS_RADIUS = 8f
        const val DRAW_PADDING = 5f

        const val HIGHLIGHT_COLOR = 0xFFBB4499
    }

    fun moveTo(position: Offset) {
        this.position = position - (containedClickPosition ?: Offset.Zero)
    }

    fun resizeLeftBy(delta: Float) {
        if (cachedSize == null) cachedSize = size
        if (cachedPosition == null) cachedPosition = position

        if (delta >= 0f) {
            size = cachedSize?.let { it.copy(width = max(MIN_WIDTH, it.width - delta)) } ?: size

            position = cachedPosition?.let { it.copy(x = min(it.x + (cachedSize?.width ?: MIN_WIDTH) - MIN_WIDTH, it.x + delta)) } ?: position
        } else {
            size = cachedSize?.let { it.copy(width = max(MIN_WIDTH, it.width - delta)) } ?: size
            position = cachedPosition?.let { it.copy(x = min(it.x, it.x + delta)) } ?: position
        }
    }

    fun resizeRightBy(delta: Float) {
        if (cachedSize == null) cachedSize = size

        size = cachedSize?.let { it.copy(width = max(MIN_WIDTH, it.width + delta)) } ?: size
    }

    fun applyResizing() {
        cachedSize = null
        cachedPosition = null
    }

    fun drawOn(
        drawScope: DrawScope,
        textMeasurer: TextMeasurer,
        textStyle: TextStyle
    ) {
        val nameText = textMeasurer.measure(
            text = name,
            style = textStyle.copy(textAlign = TextAlign.Center),
            constraints = Constraints(maxWidth = (size.width - DRAW_PADDING * 2).toInt())
        )

        if (size.height != nameText.size.height + DRAW_PADDING * 2) {
            size = size.copy(height = max(size.height, nameText.size.height + DRAW_PADDING * 2))
        }

        drawScope.drawRect(
            color = if (isHighlighted) Color(HIGHLIGHT_COLOR) else Color.Black,
            topLeft = position,
            size = size,
            style = Stroke(2f)
        )

        if (!isHighlighted) {
            highlightedSides.forEach { sideDirection ->
                val side = sideDirection.getSide()
                drawScope.drawLine(
                    color = Color(HIGHLIGHT_COLOR),
                    start = side.start,
                    end = side.end,
                    strokeWidth = DRAW_LINES_WIDTH
                )
            }

            highlightedVertices.forEach { vertexDirection ->
                val vertex = vertexDirection.getVertex()
                drawScope.drawCircle(
                    color = Color(HIGHLIGHT_COLOR),
                    center = vertex.position,
                    radius = DRAW_POINTS_RADIUS
                )
            }
        }

        drawScope.drawText(
            textLayoutResult = nameText,
            color = Color.Black,
            topLeft = position + Offset((size.width - nameText.size.width) / 2f, DRAW_PADDING)
        )
    }

    fun containsMouse(mousePosition: Offset, isClick: Boolean): ContainmentResult {
        val sidesContainment = checkSidesForContainment(position, mousePosition)
        if (sidesContainment != ContainmentResult.None) {
            (sidesContainment as? ContainmentResult.Side)?.let {
                clearHighlight()
                highlightedSides.add(it.direction)
            }
            return sidesContainment
        }

        val verticesContainment = checkVerticesForContainment(position, mousePosition)
        if (verticesContainment != ContainmentResult.None) {
            (verticesContainment as? ContainmentResult.Vertex)?.let {
                clearHighlight()
                highlightedVertices.add(it.direction)
            }
            return verticesContainment
        }

        val containsWhole = mousePosition.x in (position.x..position.x + size.width) &&
                mousePosition.y in (position.y..position.y + size.height)

        if (containsWhole) {
            if (isClick) containedClickPosition = mousePosition - position
            isHighlighted = true

            return ContainmentResult.Whole
        } else {
            clearHighlight()

            return ContainmentResult.None
        }
    }

    fun clearHighlight() {
        isHighlighted = false
        highlightedSides.clear()
        highlightedVertices.clear()
    }

    private fun SideDirection.getSide(): Side {
        return when (this) {
            SideDirection.LEFT -> Side(
                Offset(position.x, position.y),
                Offset(position.x, position.y + size.height)
            )
            SideDirection.TOP -> Side(
                Offset(position.x, position.y),
                Offset(position.x + size.width, position.y)
            )
            SideDirection.RIGHT -> Side(
                Offset(position.x + size.width, position.y),
                Offset(position.x + size.width, position.y + size.height)
            )
            SideDirection.BOTTOM -> Side(
                Offset(position.x, position.y + size.height),
                Offset(position.x + size.width, position.y + size.height)
            )
        }
    }

    private fun VertexDirection.getVertex(): Vertex {
        return when (this) {
            VertexDirection.TOP_LEFT -> Vertex(Offset(position.x, position.y))
            VertexDirection.TOP_RIGHT -> Vertex(Offset(position.x + size.width, position.y))
            VertexDirection.BOTTOM_LEFT -> Vertex(Offset(position.x, position.y + size.height))
            VertexDirection.BOTTOM_RIGHT -> Vertex(Offset(position.x + size.width, position.y + size.height))
        }
    }
}