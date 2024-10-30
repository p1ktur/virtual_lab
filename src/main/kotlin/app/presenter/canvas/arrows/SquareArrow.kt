package app.presenter.canvas.arrows

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.presenter.canvas.arrows.ArrowType.Companion.DASHED_LENGTH
import app.presenter.canvas.arrows.ArrowType.Companion.DASHED_STEP

fun DrawScope.drawSquareArrowFromTo(
    from: Offset,
    to: Offset,
    color: Color,
    highlightColor: Color,
    relativePosition: UMLClassConnection.RelativePosition,
    middleOffset: Float,
    highlightedSegments: List<ConnectionSegment>,
    arrowType: ArrowType,
    startArrowHead: ArrowHead,
    endArrowHead: ArrowHead
) {
    val pathEffect = if (arrowType == ArrowType.DASHED) {
        PathEffect.dashPathEffect(floatArrayOf(DASHED_LENGTH, DASHED_STEP))
    } else {
        null
    }

    when (relativePosition) {
        UMLClassConnection.RelativePosition.LEFT, UMLClassConnection.RelativePosition.RIGHT -> {
            val positiveCoef = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) 1 else -1
            val midX = (to.x - from.x) * middleOffset + from.x

            startArrowHead.drawOn(
                drawScope = this,
                at = from,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                angleRadians = if (to.x > from.x) Math.PI.toFloat() else 0f
            )

            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                start = from.copy(x = from.x - positiveCoef * startArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                end = from.copy(x = midX),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(x = midX),
                end = to.copy(x = midX),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(x = midX),
                end = to.copy(x = to.x + positiveCoef * endArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            endArrowHead.drawOn(
                drawScope = this,
                at = to,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                angleRadians = if (to.x > from.x) 0f else Math.PI.toFloat()
            )
        }
        UMLClassConnection.RelativePosition.TOP, UMLClassConnection.RelativePosition.BOTTOM -> {
            val positiveCoef = if (relativePosition == UMLClassConnection.RelativePosition.TOP) 1 else -1
            val midY = (to.y - from.y) * middleOffset + from.y

            startArrowHead.drawOn(
                drawScope = this,
                at = from,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                angleRadians = if (to.y > from.y) Math.PI.toFloat() * 3f / 2f else Math.PI.toFloat() / 2f
            )

            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                start = from.copy(y = from.y - positiveCoef * startArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                end = from.copy(y = midY),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(y = midY),
                end = to.copy(y = midY),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(y = midY),
                end = to.copy(y = to.y + positiveCoef * endArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            endArrowHead.drawOn(
                drawScope = this,
                at = to,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                angleRadians = if (to.y > from.y) Math.PI.toFloat() / 2f else Math.PI.toFloat() * 3f / 2f
            )
        }
    }
}

fun DrawScope.drawSquareArrowTexts(
    textMeasurer: TextMeasurer,
    from: Offset,
    to: Offset,
    color: Color,
    highlightColor: Color,
    relativePosition: UMLClassConnection.RelativePosition,
    middleOffset: Float,
    highlightedSegments: List<ConnectionSegment>,
    startText: String,
    name: String,
    endText: String,
    textStyle: TextStyle
) {
    val textPadding = 4f

    val startTextLayout = textMeasurer.measure(
        text = startText,
        style = textStyle,
        constraints = Constraints(maxWidth = 100)
    )
    val nameTextLayout = textMeasurer.measure(
        text = name,
        style = textStyle,
        constraints = Constraints(maxWidth = 100)
    )
    val endTextLayout = textMeasurer.measure(
        text = endText,
        style = textStyle,
        constraints = Constraints(maxWidth = 100)
    )

    val startOverEndY = from.y > to.y
    val startOverEndX = from.x > to.x

    when (relativePosition) {
        UMLClassConnection.RelativePosition.LEFT, UMLClassConnection.RelativePosition.RIGHT -> {
            drawText(
                textLayoutResult = startTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                topLeft = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) {
                    Offset(
                        x = from.x - textPadding - startTextLayout.size.width,
                        y = from.y + if (startOverEndY) textPadding else -startTextLayout.size.height - textPadding
                    )
                } else {
                    Offset(
                        x = from.x + textPadding,
                        y = from.y + if (startOverEndY) textPadding else -startTextLayout.size.height - textPadding
                    )
                }
            )

            drawText(
                textLayoutResult = nameTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                topLeft = Offset(
                    x = (to.x - from.x) * middleOffset + from.x + textPadding,
                    y = (to.y + from.y) / 2 - nameTextLayout.size.height / 2f
                )
            )

            drawText(
                textLayoutResult = endTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                topLeft = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) {
                    Offset(
                        x = to.x + textPadding,
                        y = to.y + if (startOverEndY) -endTextLayout.size.height - textPadding else textPadding
                    )
                } else {
                    Offset(
                        x = to.x - textPadding - endTextLayout.size.width,
                        y = to.y + if (startOverEndY) -endTextLayout.size.height - textPadding else textPadding
                    )
                }
            )
        }
        UMLClassConnection.RelativePosition.TOP, UMLClassConnection.RelativePosition.BOTTOM -> {
            drawText(
                textLayoutResult = startTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                topLeft = if (relativePosition == UMLClassConnection.RelativePosition.TOP) {
                    Offset(
                        x = from.x + if (startOverEndX) textPadding * 2 else -startTextLayout.size.width - textPadding * 2,
                        y = from.y - textPadding - startTextLayout.size.height
                    )
                } else {
                    Offset(
                        x = from.x + if (startOverEndX) textPadding * 2 else -startTextLayout.size.width - textPadding * 2,
                        y = from.y + textPadding
                    )
                }
            )

            drawText(
                textLayoutResult = nameTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                topLeft = Offset(
                    x = (to.x + from.x) / 2 - nameTextLayout.size.width / 2,
                    y = (to.y - from.y) * middleOffset + from.y - nameTextLayout.size.height - textPadding
                )
            )

            drawText(
                textLayoutResult = endTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                topLeft = if (relativePosition == UMLClassConnection.RelativePosition.TOP) {
                    Offset(
                        x = to.x + if (startOverEndX) -endTextLayout.size.width - textPadding * 2 else textPadding * 2,
                        y = to.y + textPadding
                    )
                } else {
                    Offset(
                        x = to.x + if (startOverEndX) -endTextLayout.size.width - textPadding * 2 else textPadding * 2,
                        y = to.y - textPadding - endTextLayout.size.height
                    )
                }
            )
        }
    }
}