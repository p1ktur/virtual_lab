package app.presenter.canvas

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import app.domain.umlDiagram.model.connections.*
import kotlin.math.*

fun DrawScope.drawArrowFromTo(
    from: Offset,
    to: Offset,
    color: Color
) {
    drawLine(
        color = color,
        start = from,
        end = to,
        strokeWidth = 1f
    )

    val tan = (from.y - to.y) / (from.x - to.x)
    val angleRadians = atan(tan) + if (from.x - to.x < 0) 0f else Math.PI.toFloat()

    drawSimpleArrowHead(
        at = to,
        length = 12f,
        color = color,
        angleRadians = angleRadians
    )
}

fun DrawScope.drawSquareArrowFromTo(
    from: Offset,
    to: Offset,
    color: Color,
    type: UMLClassConnection.Type,
    middleOffset: Float,
    highlightedSegments: List<ConnectionSegment>
) {
    val highlightColor = Color(UMLClassConnection.HIGHLIGHT_COLOR)
    when (type) {
        UMLClassConnection.Type.HVH -> {
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                start = from,
                end = from.copy(x = (to.x - from.x) * middleOffset + from.x),
                strokeWidth = 1f
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(x = (to.x - from.x) * middleOffset + from.x),
                end = to.copy(x = (to.x - from.x) * middleOffset + from.x),
                strokeWidth = 1f
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(x = (to.x - from.x) * middleOffset + from.x),
                end = to,
                strokeWidth = 1f
            )

            drawSimpleArrowHead(
                at = to,
                length = 12f,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                angleRadians = if (to.x > from.x) 0f else Math.PI.toFloat()
            )
        }
        UMLClassConnection.Type.VHV -> {
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                start = from,
                end = from.copy(y = (to.y - from.y) * middleOffset + from.y),
                strokeWidth = 1f
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(y = (to.y - from.y) * middleOffset + from.y),
                end = to.copy(y = (to.y - from.y) * middleOffset + from.y),
                strokeWidth = 1f
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(y = (to.y - from.y) * middleOffset + from.y),
                end = to,
                strokeWidth = 1f
            )

            drawSimpleArrowHead(
                at = to,
                length = 12f,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                angleRadians = if (to.y > from.y) Math.PI.toFloat() / 2f else Math.PI.toFloat() * 3f / 2f
            )
        }
    }
}