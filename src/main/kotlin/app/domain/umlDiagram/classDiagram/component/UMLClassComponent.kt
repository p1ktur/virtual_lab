package app.domain.umlDiagram.classDiagram.component

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.serialization.*
import app.domain.umlDiagram.classDiagram.component.function.Function
import app.domain.umlDiagram.classDiagram.connection.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.geometry.*
import kotlinx.serialization.*
import kotlin.math.*

@Serializable
data class UMLClassComponent(
    // Data
    var name: String = "Class Component",
    var fields: MutableList<Field> = mutableListOf(),
    var functions: MutableList<Function> = mutableListOf(),
    var isInterface: Boolean = false,
    // Graphics
    @Serializable(with = OffsetSerializer::class) var position: Offset = Offset(100f, 100f),
    @Serializable(with = SizeSerializer::class) var size: Size = Size(MIN_WIDTH, MIN_HEIGHT),
    @Transient var isHighlighted: Boolean = false,
    @Transient val highlightedSides: MutableList<SideDirection> = mutableListOf(),
    @Transient val highlightedVertices: MutableList<VertexDirection> = mutableListOf()
) {

    companion object {
        const val MIN_WIDTH = 144f
        const val MIN_HEIGHT = 72f

        const val DRAW_LINES_WIDTH = 2f
        const val DRAW_POINTS_RADIUS = 8f
        const val DRAW_PADDING = 5f
    }

    @Transient private var containedClickPosition: Offset? = null

    @Transient private var cachedSize: Size? = null
    @Transient private var cachedPosition: Offset? = null

    fun moveTo(position: Offset) {
        this.position = (position - (containedClickPosition ?: Offset.Zero)).smoothen()
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
        nameTextStyle: TextStyle,
        contentTextStyle: TextStyle,
        frameColor: Color,
        containerColor: Color,
        textColor: Color,
        highlightColor: Color
    ) {
        val color = if (isHighlighted) highlightColor else frameColor

        val maxTextWidth = size.width - DRAW_PADDING * 2
        var contentHeightSum = 0f

        val nameTextLayout = textMeasurer.measure(
            text = if (isInterface) "«interface» $name" else name,
            style = nameTextStyle.copy(textAlign = TextAlign.Center),
            constraints = Constraints(maxWidth = maxTextWidth.toInt())
        )
        contentHeightSum += nameTextLayout.size.height

        val fieldTextLayouts = fields.map {
            textMeasurer.measure(
                text = it.toString(),
                style = contentTextStyle,
                constraints = Constraints(maxWidth = maxTextWidth.toInt())
            ).apply {
                contentHeightSum += size.height
            }
        }
        if (fieldTextLayouts.isNotEmpty()) contentHeightSum += DRAW_PADDING * 2

        val functionTextLayouts = functions.map {
            textMeasurer.measure(
                text = it.toString(),
                style = contentTextStyle,
                constraints = Constraints(maxWidth = maxTextWidth.toInt())
            ).apply {
                contentHeightSum += size.height
            }
        }
        if (functionTextLayouts.isNotEmpty()) contentHeightSum += DRAW_PADDING * 2

        if (size.height != contentHeightSum + DRAW_PADDING * 2) {
            size = size.copy(height = max(MIN_HEIGHT, contentHeightSum + DRAW_PADDING * 2))
        }

        drawScope.drawRect(
            color = containerColor,
            topLeft = position,
            size = size,
            style = Fill
        )
        drawScope.drawRect(
            color = color,
            topLeft = position,
            size = size,
            style = Stroke(2f)
        )

        if (!isHighlighted) {
            highlightedSides.forEach { sideDirection ->
                val side = sideDirection.getSide()
                drawScope.drawLine(
                    color = highlightColor,
                    start = side.start,
                    end = side.end,
                    strokeWidth = DRAW_LINES_WIDTH
                )
            }

            highlightedVertices.forEach { vertexDirection ->
                val vertex = vertexDirection.getVertex()
                drawScope.drawCircle(
                    color = highlightColor,
                    center = vertex.position,
                    radius = DRAW_POINTS_RADIUS
                )
            }
        }

        var currentPosition = position.copy(x = position.x + DRAW_PADDING)

        currentPosition += Offset(0f, DRAW_PADDING)
        drawScope.drawText(
            textLayoutResult = nameTextLayout,
            color = textColor,
            topLeft = Offset(
                x = position.x + size.width / 2f - nameTextLayout.size.width / 2f,
                y = currentPosition.y
            )
        )
        currentPosition += Offset(0f, nameTextLayout.size.height.toFloat())

        if (fieldTextLayouts.isNotEmpty()) {
            currentPosition += Offset(0f, DRAW_PADDING)
            drawScope.drawLine(
                color = color,
                start = currentPosition.copy(x = position.x),
                end = currentPosition.copy(x = position.x + size.width),
                strokeWidth = 1f
            )
            currentPosition += Offset(0f, DRAW_PADDING)

            fieldTextLayouts.forEachIndexed { index, layout ->
                drawScope.drawText(
                    textLayoutResult = layout,
                    color = textColor,
                    topLeft = currentPosition,
                    textDecoration = if (fields[index].isStatic) TextDecoration.Underline else null
                )
                currentPosition += Offset(0f, layout.size.height.toFloat())
            }
        }

        if (functionTextLayouts.isNotEmpty()) {
            currentPosition += Offset(0f, DRAW_PADDING)
            drawScope.drawLine(
                color = color,
                start = currentPosition.copy(x = position.x),
                end = currentPosition.copy(x = position.x + size.width),
                strokeWidth = 1f
            )
            currentPosition += Offset(0f, DRAW_PADDING)

            functionTextLayouts.forEachIndexed { index, layout ->
                drawScope.drawText(
                    textLayoutResult = layout,
                    color = textColor,
                    topLeft = currentPosition,
                    textDecoration = if (functions[index].isStatic) TextDecoration.Underline else null
                )
                currentPosition += Offset(0f, layout.size.height.toFloat())
            }
        }
    }

    fun getDrawOffsetOf(
        refType: RefType,
        textMeasurer: TextMeasurer,
        nameTextStyle: TextStyle,
        contentTextStyle: TextStyle
    ): Offset {
        val maxTextWidth = size.width - DRAW_PADDING * 2

        return when (refType) {
            is RefType.Field -> {
                val indexTarget = refType.index

                val nameTextLayout = textMeasurer.measure(
                    text = if (isInterface) "«interface» $name" else name,
                    style = nameTextStyle.copy(textAlign = TextAlign.Center),
                    constraints = Constraints(maxWidth = maxTextWidth.toInt())
                )

                val fieldTextLayouts = fields.map {
                    textMeasurer.measure(
                        text = it.toString(),
                        style = contentTextStyle,
                        constraints = Constraints(maxWidth = maxTextWidth.toInt())
                    )
                }

                var currentHeight = 0f
                currentHeight += nameTextLayout.size.height.toFloat() + DRAW_PADDING * 3

                for (index in fieldTextLayouts.indices) {
                    val layout = fieldTextLayouts[index]
                    if (index != indexTarget) {
                        currentHeight += layout.size.height.toFloat()
                    } else {
                        currentHeight += layout.size.height / 2f
                        break
                    }
                }

                Offset(0f, currentHeight)
            }
            is RefType.Function -> {
                val indexTarget = refType.index

                val nameTextLayout = textMeasurer.measure(
                    text = if (isInterface) "«interface» $name" else name,
                    style = nameTextStyle.copy(textAlign = TextAlign.Center),
                    constraints = Constraints(maxWidth = maxTextWidth.toInt())
                )

                val fieldTextLayouts = fields.map {
                    textMeasurer.measure(
                        text = it.toString(),
                        style = contentTextStyle,
                        constraints = Constraints(maxWidth = maxTextWidth.toInt())
                    )
                }

                val functionTextLayouts = functions.map {
                    textMeasurer.measure(
                        text = it.toString(),
                        style = contentTextStyle,
                        constraints = Constraints(maxWidth = maxTextWidth.toInt())
                    )
                }

                var currentHeight = 0f
                currentHeight += nameTextLayout.size.height.toFloat() + DRAW_PADDING * 3

                fieldTextLayouts.forEachIndexed { index, layout ->
                    currentHeight += layout.size.height.toFloat()
                }

                currentHeight += DRAW_PADDING * 2

                if (functionTextLayouts.isNotEmpty()) {
                    for (index in functionTextLayouts.indices) {
                        val layout = functionTextLayouts[index]
                        if (index != indexTarget) {
                            currentHeight += layout.size.height.toFloat()
                        } else {
                            currentHeight += layout.size.height / 2f
                            break
                        }
                    }
                }

                Offset(0f, currentHeight)
            }
        }
    }

    fun containsMouse(mousePosition: Offset, isClick: Boolean): ComponentContainmentResult {
        val sidesContainment = checkSidesForContainment(mousePosition)
        if (sidesContainment != ComponentContainmentResult.None) {
            (sidesContainment as? ComponentContainmentResult.Side)?.let {
                if (it.direction == SideDirection.RIGHT || it.direction == SideDirection.LEFT) {
                    clearHighlight()
                    highlightedSides.add(it.direction)
                    return it
                }
            }
        }

//        val verticesContainment = checkVerticesForContainment(mousePosition)
//        if (verticesContainment != ComponentContainmentResult.None) {
//            (verticesContainment as? ComponentContainmentResult.Vertex)?.let {
//                clearHighlight()
//                highlightedVertices.add(it.direction)
//            }
//            return verticesContainment
//        }

        val containsWhole = mousePosition.x in (position.x..position.x + size.width) &&
                mousePosition.y in (position.y..position.y + size.height)

        if (containsWhole) {
            if (isClick) containedClickPosition = mousePosition - position
            isHighlighted = true

            return ComponentContainmentResult.Whole
        } else {
            clearHighlight()

            return ComponentContainmentResult.None
        }
    }

    fun clearHighlight() {
        isHighlighted = false
        highlightedSides.clear()
        highlightedVertices.clear()
    }

    fun equalsTo(other: UMLClassComponent): Boolean {
        return try {
            this.name == other.name &&
                    this.fields.mapIndexed { index, field ->
                        field.toString() == other.fields[index].toString()
                    }.all { it } &&
                    this.functions.mapIndexed { index, function ->
                        function.toString() == other.functions[index].toString()
                    }.all { it } &&
                    this.isInterface == other.isInterface &&
                    this.position == other.position &&
                    this.size == other.size
        } catch (_: Exception) {
            false
        }
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

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + fields.toTypedArray().contentHashCode()
        result = 31 * result + functions.toTypedArray().contentHashCode()
        result = 31 * result + isInterface.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UMLClassComponent

        if (name != other.name) return false
        if (!fields.containsAll(other.fields)) return false
        if (!functions.containsAll(other.functions)) return false
        if (isInterface != other.isInterface) return false

        return true
    }
}