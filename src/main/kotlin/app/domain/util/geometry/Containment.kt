package app.domain.util.geometry

import androidx.compose.ui.geometry.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import app.domain.umlDiagram.model.connection.UMLClassConnection.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.numbers.*
import app.presenter.canvas.arrows.*
import kotlin.math.*

fun UMLClassComponent.checkSidesForContainment(mousePosition: Offset): ComponentContainmentResult {
    val containsLeft = mousePosition.x in (position.x - ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.x + ComponentContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y + ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.y + size.height - ComponentContainmentResult.SIDE_CHECK_RADIUS)

    if (containsLeft) return ComponentContainmentResult.Side(SideDirection.LEFT)

    val containsTop = mousePosition.x in (position.x + ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.x + size.width - ComponentContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y - ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.y + ComponentContainmentResult.SIDE_CHECK_RADIUS)

    if (containsTop) return ComponentContainmentResult.Side(SideDirection.TOP)

    val containsRight = mousePosition.x in (position.x + size.width - ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.x + size.width + ComponentContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y + ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.y + size.height - ComponentContainmentResult.SIDE_CHECK_RADIUS)

    if (containsRight) return ComponentContainmentResult.Side(SideDirection.RIGHT)

    val containsBottom = mousePosition.x in (position.x + ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.x + size.width - ComponentContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y + size.height - ComponentContainmentResult.SIDE_CHECK_RADIUS)..(position.y + size.height + ComponentContainmentResult.SIDE_CHECK_RADIUS)

    if (containsBottom) return ComponentContainmentResult.Side(SideDirection.BOTTOM)

    return ComponentContainmentResult.None
}

fun UMLClassComponent.checkVerticesForContainment(mousePosition: Offset): ComponentContainmentResult {
    val topLeft = position
    val containsTopLeft = mousePosition.isAround(topLeft, ComponentContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsTopLeft) return ComponentContainmentResult.Vertex(VertexDirection.TOP_LEFT)

    val topRight = Offset(position.x + size.width, position.y)
    val containsTopRight = mousePosition.isAround(topRight, ComponentContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsTopRight) return ComponentContainmentResult.Vertex(VertexDirection.TOP_RIGHT)

    val bottomLeft = Offset(position.x, position.y + size.height)
    val containsBottomLeft = mousePosition.isAround(bottomLeft, ComponentContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsBottomLeft) return ComponentContainmentResult.Vertex(VertexDirection.BOTTOM_LEFT)

    val bottomRight = Offset(position.x + size.width, position.y + size.height)
    val containsBottomRight = mousePosition.isAround(bottomRight, ComponentContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsBottomRight) return ComponentContainmentResult.Vertex(VertexDirection.BOTTOM_RIGHT)

    return ComponentContainmentResult.None
}

fun UMLClassConnection.checkSegmentsForContainment(mousePosition: Offset): ConnectionContainmentResult {
    return if (drawnAsArch) checkArchSegmentsForContainment(mousePosition) else checkSimpleSegmentsForContainment(mousePosition)
}

private fun UMLClassConnection.checkSimpleSegmentsForContainment(mousePosition: Offset): ConnectionContainmentResult {
    val (lowerX, higherX) = if (calculatedTo.x >= calculatedFrom.x) calculatedFrom.x to calculatedTo.x else calculatedTo.x to calculatedFrom.x
    val (lowerY, higherY) = if (calculatedTo.y >= calculatedFrom.y) calculatedFrom.y to calculatedTo.y else calculatedTo.y to calculatedFrom.y

    val yAxisIsCorrect = calculatedTo.y >= calculatedFrom.y
    val xAxisIsCorrect = calculatedTo.x >= calculatedFrom.x

    val middleOffset = this.middleOffset.limit(0f, 1f)

    when (cachedRelativePosition) {
        RelativePosition.LEFT -> {
            val midX = (higherX - lowerX) * (1f - middleOffset) + lowerX

            val containsFirst = mousePosition.x in midX..higherX && if (yAxisIsCorrect) {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsFirst) return ConnectionContainmentResult.FirstSegment(SegmentDirection.HORIZONTAL)

            val containsSecond = mousePosition.x in (midX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(midX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS) &&
                    mousePosition.y in lowerY..higherY
            if (containsSecond) return ConnectionContainmentResult.SecondSegment(SegmentDirection.VERTICAL)

            val containsThird = mousePosition.x in lowerX..midX && if (yAxisIsCorrect) {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsThird) return ConnectionContainmentResult.ThirdSegment(SegmentDirection.HORIZONTAL)

            return ConnectionContainmentResult.None
        }
        RelativePosition.TOP -> {
            val midY = (higherY - lowerY) * (1f - middleOffset) + lowerY

            val containsFirst = if (xAxisIsCorrect) {
                mousePosition.x in (lowerX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.x in (higherX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } && mousePosition.y in midY..higherY
            if (containsFirst) return ConnectionContainmentResult.FirstSegment(SegmentDirection.VERTICAL)

            val containsSecond = mousePosition.x in lowerX..higherX &&
                    mousePosition.y in (midY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(midY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            if (containsSecond) return ConnectionContainmentResult.SecondSegment(SegmentDirection.HORIZONTAL)

            val containsThird = if (xAxisIsCorrect) {
                mousePosition.x in (higherX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.x in (lowerX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } && mousePosition.y in lowerY..midY
            if (containsThird) return ConnectionContainmentResult.ThirdSegment(SegmentDirection.VERTICAL)

            return ConnectionContainmentResult.None
        }
        RelativePosition.RIGHT -> {
            val midX = (higherX - lowerX) * middleOffset + lowerX

            val containsFirst = mousePosition.x in lowerX..midX && if (yAxisIsCorrect) {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsFirst) return ConnectionContainmentResult.FirstSegment(SegmentDirection.HORIZONTAL)

            val containsSecond = mousePosition.x in (midX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(midX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS) &&
                    mousePosition.y in lowerY..higherY
            if (containsSecond) return ConnectionContainmentResult.SecondSegment(SegmentDirection.VERTICAL)

            val containsThird = mousePosition.x in midX..higherX && if (yAxisIsCorrect) {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsThird) return ConnectionContainmentResult.ThirdSegment(SegmentDirection.HORIZONTAL)

            return ConnectionContainmentResult.None
        }
        RelativePosition.BOTTOM -> {
            val midY = (higherY - lowerY) * middleOffset + lowerY

            val containsFirst = if (xAxisIsCorrect) {
                mousePosition.x in (lowerX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.x in (higherX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } && mousePosition.y in lowerY..midY
            if (containsFirst) return ConnectionContainmentResult.FirstSegment(SegmentDirection.VERTICAL)

            val containsSecond = mousePosition.x in lowerX..higherX &&
                    mousePosition.y in (midY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(midY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            if (containsSecond) return ConnectionContainmentResult.SecondSegment(SegmentDirection.HORIZONTAL)

            val containsThird = if (xAxisIsCorrect) {
                mousePosition.x in (higherX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.x in (lowerX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } && mousePosition.y in midY..higherY
            if (containsThird) return ConnectionContainmentResult.ThirdSegment(SegmentDirection.VERTICAL)

            return ConnectionContainmentResult.None
        }
    }
}

private fun UMLClassConnection.checkArchSegmentsForContainment(mousePosition: Offset): ConnectionContainmentResult {
    val (lowerY, higherY) = if (calculatedTo.y >= calculatedFrom.y) calculatedFrom.y to calculatedTo.y else calculatedTo.y to calculatedFrom.y

    val yAxisIsCorrect = calculatedTo.y >= calculatedFrom.y

    val relativePosition = when {
        calculatedTo.x + endRef.ref.size.width / 2 >= calculatedFrom.x + startRef.ref.size.width / 2 -> RelativePosition.RIGHT
        else -> RelativePosition.LEFT
    }

    return when (relativePosition) {
        RelativePosition.LEFT -> {
            val midX = -ARCH_SAMPLE * middleArchOffset + min(calculatedFrom.x, calculatedTo.x) - ARCH_MINIMUM

            val containsFirst = mousePosition.x in midX..calculatedFrom.x && if (yAxisIsCorrect) {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsFirst) return ConnectionContainmentResult.FirstSegment(SegmentDirection.HORIZONTAL)

            val containsSecond = mousePosition.x in (midX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(midX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS) &&
                    mousePosition.y in lowerY..higherY
            if (containsSecond) return ConnectionContainmentResult.SecondSegment(SegmentDirection.VERTICAL)

            val containsThird = mousePosition.x in midX..calculatedTo.x && if (yAxisIsCorrect) {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsThird) return ConnectionContainmentResult.ThirdSegment(SegmentDirection.HORIZONTAL)

            return ConnectionContainmentResult.None
        }
        RelativePosition.RIGHT -> {
            val midX = ARCH_SAMPLE * middleArchOffset + max(calculatedFrom.x, calculatedTo.x) + ARCH_MINIMUM

            val containsFirst = mousePosition.x in calculatedFrom.x..midX && if (yAxisIsCorrect) {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsFirst) return ConnectionContainmentResult.FirstSegment(SegmentDirection.HORIZONTAL)

            val containsSecond = mousePosition.x in (midX - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(midX + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS) &&
                    mousePosition.y in lowerY..higherY
            if (containsSecond) return ConnectionContainmentResult.SecondSegment(SegmentDirection.VERTICAL)

            val containsThird = mousePosition.x in calculatedTo.x..midX && if (yAxisIsCorrect) {
                mousePosition.y in (higherY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(higherY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            } else {
                mousePosition.y in (lowerY - ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)..(lowerY + ConnectionContainmentResult.SEGMENT_CHECK_RADIUS)
            }
            if (containsThird) return ConnectionContainmentResult.ThirdSegment(SegmentDirection.HORIZONTAL)

            return ConnectionContainmentResult.None
        }
        else -> ConnectionContainmentResult.None
    }
}