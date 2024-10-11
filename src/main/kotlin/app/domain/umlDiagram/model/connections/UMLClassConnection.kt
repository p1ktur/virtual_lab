package app.domain.umlDiagram.model.connections

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.geometry.*
import app.domain.util.numbers.*
import app.presenter.canvas.*

data class UMLClassConnection(
    val startRef: RefConnection,
    val endRef: RefConnection,
    var middleOffset: Float = 0.5f,
    val forcedType: Type? = null,
    val highlightedSegments: MutableList<ConnectionSegment> = mutableListOf()
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

    var calculatedFrom: Offset = Offset.Zero
    var calculatedTo: Offset = Offset.Zero

    private val relativePosition: RelativePosition get() = defineRelativePosition().apply {
        cachedRelativePosition = this
    }
    var cachedRelativePosition: RelativePosition = relativePosition

    private val type: Type get() = when (relativePosition) {
        RelativePosition.LEFT -> Type.HVH
        RelativePosition.TOP -> Type.VHV
        RelativePosition.RIGHT -> Type.HVH
        RelativePosition.BOTTOM -> Type.VHV
    }.apply {
        cachedType = this
    }
    var cachedType: Type = type

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
        drawScope: DrawScope
    ) {
        drawSimpleConnection(drawScope)
    }

    fun containsMouse(mousePosition: Offset): ConnectionContainmentResult {
        val segmentsContainment = checkSegmentsForContainment(mousePosition)
        when (segmentsContainment) {
            is ConnectionContainmentResult.FirstSegment -> {
                clearHighlight()
                highlightedSegments.add(ConnectionSegment.FIRST)
            }
            is ConnectionContainmentResult.SecondSegment -> {
                clearHighlight()
                highlightedSegments.add(ConnectionSegment.SECOND)
            }
            is ConnectionContainmentResult.ThirdSegment -> {
                clearHighlight()
                highlightedSegments.add(ConnectionSegment.THIRD)
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

    private fun drawSimpleConnection(drawScope: DrawScope) {
        val startOffsets = startRef.getOffsets()
        val endOffsets = endRef.getOffsets()

        cachedType = type

        calculatedFrom = when (relativePosition) {
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

        calculatedTo = when (relativePosition) {
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
            type = when (relativePosition) {
                RelativePosition.LEFT, RelativePosition.RIGHT -> Type.HVH
                RelativePosition.TOP, RelativePosition.BOTTOM -> Type.VHV
            },
            middleOffset = middleOffset,
            highlightedSegments = highlightedSegments
        )
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
