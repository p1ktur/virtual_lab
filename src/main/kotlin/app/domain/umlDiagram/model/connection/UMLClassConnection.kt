package app.domain.umlDiagram.model.connection

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.geometry.*
import app.domain.util.numbers.*
import app.presenter.canvas.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.w3c.dom.Text

@Serializable
data class UMLClassConnection(
    // Data
    var name: String = "",
    var startText: String = "",
    var endText: String = "",
    var startRef: RefConnection,
    var endRef: RefConnection,
    var startArrowHead: ArrowHead = ArrowHead.ASSOCIATION,
    var endArrowHead: ArrowHead = ArrowHead.ASSOCIATION,
    var arrowType: ArrowType = ArrowType.SOLID,
    // Graphics
    @Transient var middleOffset: Float = 0.5f,
    @Transient var forcedType: Type? = null,
    @Transient val highlightedSegments: MutableList<ConnectionSegment> = mutableListOf()
) {
    enum class Type {
        HVH,           // horizontal-vertical-horizontal
        VHV            // vertical-horizontal-vertical
    }

    enum class RelativePosition {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    data class ConnectionOffset(
        var left: Float = 0.5f,
        var top: Float = 0.5f,
        var right: Float = 0.5f,
        var bottom: Float = 0.5f
    )

    companion object {
        const val ARROW_COLOR = 0xFF000000
        const val HIGHLIGHT_COLOR = 0xFFBB4499
    }

    @Transient var calculatedFrom: Offset = Offset.Zero
    @Transient var calculatedTo: Offset = Offset.Zero

    private val relativePosition: RelativePosition get() = defineRelativePosition().apply {
        cachedRelativePosition = this
    }
    var cachedRelativePosition: RelativePosition = relativePosition

    private val type: Type get() = when (relativePosition) {
        RelativePosition.LEFT -> Type.HVH
        RelativePosition.TOP -> Type.VHV
        RelativePosition.RIGHT -> Type.HVH
        RelativePosition.BOTTOM -> Type.VHV
    }

    fun setSegmentOffset(
        connectionSegment: ConnectionSegment,
        coefficient: Float
    ) {
        when (connectionSegment) {
            ConnectionSegment.FIRST -> {
                (startRef as? RefConnection.SimpleConnection)?.offset?.apply {
                    when (cachedRelativePosition) {
                        RelativePosition.LEFT -> left = coefficient
                        RelativePosition.TOP -> top = coefficient
                        RelativePosition.RIGHT -> right = coefficient
                        RelativePosition.BOTTOM -> bottom = coefficient
                    }
                }
            }
            ConnectionSegment.SECOND -> {
                middleOffset = coefficient
            }
            ConnectionSegment.THIRD -> {
                (endRef as? RefConnection.SimpleConnection)?.offset?.apply {
                    when (cachedRelativePosition) {
                        RelativePosition.LEFT -> right = coefficient
                        RelativePosition.TOP -> bottom = coefficient
                        RelativePosition.RIGHT -> left = coefficient
                        RelativePosition.BOTTOM -> top = coefficient
                    }
                }
            }
        }
    }

    fun calculateSegmentOffset(connectionSegment: ConnectionSegment, mousePosition: Offset): Float {
        return when (connectionSegment) {
            ConnectionSegment.FIRST -> {
                if (type == Type.HVH) {
                    val total = startRef.ref.size.height
                    val relativeMousePositionY = mousePosition.y.relativeLimit(
                        bound1 = startRef.ref.position.y,
                        bound2 = startRef.ref.position.y + startRef.ref.size.height
                    )

                    relativeMousePositionY / total
                } else {
                    val total = startRef.ref.size.width
                    val relativeMousePositionX = mousePosition.x.relativeLimit(
                        bound1 = startRef.ref.position.x,
                        bound2 = startRef.ref.position.x + startRef.ref.size.width
                    )

                    relativeMousePositionX / total
                }
            }
            ConnectionSegment.SECOND -> {
                if (type == Type.HVH) {
                    val relativeMousePositionX = mousePosition.x.relativeLimit(
                        bound1 = calculatedFrom.x,
                        bound2 = calculatedTo.x
                    )

                    if (calculatedTo.x >= calculatedFrom.x) {
                        val total = calculatedTo.x - calculatedFrom.x
                        (relativeMousePositionX / total).limit(0.1f, 0.9f)
                    } else {
                        val total = calculatedFrom.x - calculatedTo.x
                        1f - (relativeMousePositionX / total).limit(0.1f, 0.9f)
                    }
                } else {
                    val relativeMousePositionY = mousePosition.y.relativeLimit(
                        bound1 = calculatedFrom.y,
                        bound2 = calculatedTo.y
                    )

                    if (calculatedTo.y >= calculatedFrom.y) {
                        val total = calculatedTo.y - calculatedFrom.y
                        (relativeMousePositionY / total).limit(0.1f, 0.9f)
                    } else {
                        val total = calculatedFrom.y - calculatedTo.y
                        1f - (relativeMousePositionY / total).limit(0.1f, 0.9f)
                    }
                }
            }
            ConnectionSegment.THIRD -> {
                if (type == Type.HVH) {
                    val total = endRef.ref.size.height
                    val relativeMousePositionY = mousePosition.y.relativeLimit(
                        bound1 = endRef.ref.position.y,
                        bound2 = endRef.ref.position.y + endRef.ref.size.height
                    )

                    relativeMousePositionY / total
                } else {
                    val total = endRef.ref.size.width
                    val relativeMousePositionX = mousePosition.x.relativeLimit(
                        bound1 = endRef.ref.position.x,
                        bound2 = endRef.ref.position.x + endRef.ref.size.width
                    )

                    relativeMousePositionX / total
                }
            }
        }
    }

    fun drawOn(
        drawScope: DrawScope,
        textMeasurer: TextMeasurer,
        textStyle: TextStyle,
        componentNameTextStyle: TextStyle,
        componentContentTextStyle: TextStyle
    ) {
        val startOffsets = startRef.getOffsets()
        val endOffsets = endRef.getOffsets()

        val forcedStartOn: RefType? = (startRef as? RefConnection.ReferencedConnection).let { it?.refType }
        val forcedEndOn: RefType? = (endRef as? RefConnection.ReferencedConnection).let { it?.refType }

        val startForcedOffset = forcedStartOn?.let {
            startRef.ref.getDrawOffsetOf(it, textMeasurer, componentNameTextStyle, componentContentTextStyle)
        }
        val endForcedOffset = forcedEndOn?.let {
            endRef.ref.getDrawOffsetOf(it, textMeasurer, componentNameTextStyle, componentContentTextStyle)
        }

        calculatedFrom = startForcedOffset?.let { offset ->
            when (relativePosition) {
                RelativePosition.LEFT -> startRef.ref.position.copy(
                    y = startRef.ref.position.y + offset.y
                )
                RelativePosition.RIGHT -> startRef.ref.position.copy(
                    x = startRef.ref.position.x + startRef.ref.size.width,
                    y = startRef.ref.position.y + offset.y
                )
                else -> startRef.ref.position
            }
        } ?: when (relativePosition) {
            RelativePosition.LEFT -> startRef.ref.position.copy(
                y = startRef.ref.position.y + startRef.ref.size.height * (startOffsets?.left ?: 0.5f)
            )
            RelativePosition.TOP -> startRef.ref.position.copy(
                x = startRef.ref.position.x + startRef.ref.size.width * (startOffsets?.top ?: 0.5f)
            )
            RelativePosition.RIGHT -> startRef.ref.position.copy(
                x = startRef.ref.position.x + startRef.ref.size.width,
                y = startRef.ref.position.y + startRef.ref.size.height * (startOffsets?.right ?: 0.5f)
            )
            RelativePosition.BOTTOM -> startRef.ref.position.copy(
                x = startRef.ref.position.x + startRef.ref.size.width * (startOffsets?.bottom ?: 0.5f),
                y = startRef.ref.position.y + startRef.ref.size.height
            )
        }

        calculatedTo = endForcedOffset?.let { offset ->
            when (relativePosition) {
                RelativePosition.LEFT -> endRef.ref.position.copy(
                    x = endRef.ref.position.x + endRef.ref.size.width,
                    y = endRef.ref.position.y + offset.y
                )
                RelativePosition.RIGHT -> endRef.ref.position.copy(
                    y = endRef.ref.position.y + offset.y
                )
                else -> endRef.ref.position
            }
        } ?: when (relativePosition) {
            RelativePosition.LEFT -> endRef.ref.position.copy(
                x = endRef.ref.position.x + endRef.ref.size.width,
                y = endRef.ref.position.y + endRef.ref.size.height * (endOffsets?.right ?: 0.5f)
            )
            RelativePosition.TOP -> endRef.ref.position.copy(
                x = endRef.ref.position.x + endRef.ref.size.width * (endOffsets?.bottom ?: 0.5f),
                y = endRef.ref.position.y + endRef.ref.size.height
            )
            RelativePosition.RIGHT -> endRef.ref.position.copy(
                y = endRef.ref.position.y + endRef.ref.size.height * (endOffsets?.left ?: 0.5f)
            )
            RelativePosition.BOTTOM -> endRef.ref.position.copy(
                x = endRef.ref.position.x + endRef.ref.size.width * (endOffsets?.top ?: 0.5f)
            )
        }

        drawScope.drawSquareArrowFromTo(
            from = calculatedFrom,
            to = calculatedTo,
            color = Color(ARROW_COLOR),
            relativePosition = relativePosition,
            middleOffset = middleOffset,
            highlightedSegments = highlightedSegments,
            arrowType = arrowType,
            startArrowHead = startArrowHead,
            endArrowHead = endArrowHead
        )

        drawScope.drawSquareArrowTexts(
            textMeasurer = textMeasurer,
            from = calculatedFrom,
            to = calculatedTo,
            color = Color(ARROW_COLOR),
            relativePosition = relativePosition,
            middleOffset = middleOffset,
            highlightedSegments = highlightedSegments,
            startText = startText,
            name = name,
            endText = endText,
            textStyle = textStyle
        )
    }

    fun containsMouse(mousePosition: Offset): ConnectionContainmentResult {
        val segmentsContainment = checkSegmentsForContainment(mousePosition)
        when (segmentsContainment) {
            is ConnectionContainmentResult.FirstSegment -> {
                if (startRef !is RefConnection.ReferencedConnection) {
                    clearHighlight()
                    highlightedSegments.add(ConnectionSegment.FIRST)
                } else return ConnectionContainmentResult.Whole
            }
            is ConnectionContainmentResult.SecondSegment -> {
                clearHighlight()
                highlightedSegments.add(ConnectionSegment.SECOND)
            }
            is ConnectionContainmentResult.ThirdSegment -> {
                if (endRef !is RefConnection.ReferencedConnection) {
                    clearHighlight()
                    highlightedSegments.add(ConnectionSegment.THIRD)
                } else return ConnectionContainmentResult.Whole
            }
            else -> {
                clearHighlight()
            }
        }

        return segmentsContainment
    }

    fun clearHighlight() {
        highlightedSegments.clear()
    }

    private fun defineRelativePosition(): RelativePosition {
        return when (forcedType) {
            Type.HVH -> {
                if (endRef.ref.position.x >= startRef.ref.position.x) {
                    RelativePosition.RIGHT
                } else {
                    RelativePosition.LEFT
                }
            }
            Type.VHV -> {
                if (endRef.ref.position.y >= startRef.ref.position.y) {
                    RelativePosition.BOTTOM
                } else {
                    RelativePosition.TOP
                }
            }
            null -> {
                val topLeft = startRef.ref.position
                val bottomRight = startRef.ref.position + Offset(startRef.ref.size.width, startRef.ref.size.height)
                val slope = (bottomRight.y - topLeft.y) / (bottomRight.x - topLeft.x)
                val b = topLeft.y - slope * topLeft.x

                val x0 = endRef.ref.position.x + endRef.ref.size.width / 2
                val y0 = endRef.ref.position.y + endRef.ref.size.height / 2
                val x1 = (y0 - b) / slope
                val x2 = ((topLeft.y * 2 + startRef.ref.size.height - y0) - b) / slope

                when {
                    x0 < x1 && x0 < x2 -> RelativePosition.LEFT
                    x0 > x1 && x0 < x2 -> RelativePosition.TOP
                    x0 > x1 && x0 > x2 -> RelativePosition.RIGHT
                    else -> RelativePosition.BOTTOM
                }
            }
        }
    }
}
