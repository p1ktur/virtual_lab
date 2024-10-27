package app.presenter.canvas.arrows

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.presenter.canvas.arrows.ArrowType.Companion.DASHED_LENGTH
import app.presenter.canvas.arrows.ArrowType.Companion.DASHED_STEP
import app.presenter.theme.*
import kotlin.math.*

const val ARCH_SAMPLE = 64f
const val ARCH_MINIMUM = 20f

fun DrawScope.drawSquareArchArrowFromTo(
    from: Offset,
    to: Offset,
    color: Color,
    relativePosition: UMLClassConnection.RelativePosition,
    middleArchOffset: Float,
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
        UMLClassConnection.RelativePosition.LEFT -> {
            val middleX = -ARCH_SAMPLE * middleArchOffset + min(from.x, to.x) - ARCH_MINIMUM

            startArrowHead.drawOn(
                drawScope = this,
                at = from,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                angleRadians = 0f
            )

            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                start = from.copy(x = from.x - startArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                end = from.copy(x = middleX),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(x = middleX),
                end = to.copy(x = middleX),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(x = middleX),
                end = to.copy(x = to.x - endArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            endArrowHead.drawOn(
                drawScope = this,
                at = to,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                angleRadians = 0f
            )
        }
        UMLClassConnection.RelativePosition.RIGHT -> {
            val middleX = ARCH_SAMPLE * middleArchOffset + max(from.x, to.x) + ARCH_MINIMUM

            startArrowHead.drawOn(
                drawScope = this,
                at = from,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                angleRadians = Math.PI.toFloat()
            )

            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                start = from.copy(x = from.x + startArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                end = from.copy(x = middleX),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(x = middleX),
                end = to.copy(x = middleX),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(x = middleX),
                end = to.copy(x = to.x + endArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            endArrowHead.drawOn(
                drawScope = this,
                at = to,
                length = ArrowHead.ARROW_HEAD_LENGTH,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                angleRadians = Math.PI.toFloat()
            )
        }
        else -> Unit
    }
}

fun DrawScope.drawSquareArchArrowTexts(
    textMeasurer: TextMeasurer,
    from: Offset,
    to: Offset,
    color: Color,
    relativePosition: UMLClassConnection.RelativePosition,
    middleArchOffset: Float,
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

    val middleX = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) {
        -ARCH_SAMPLE * middleArchOffset + min(from.x, to.x) - ARCH_MINIMUM
    } else {
        ARCH_SAMPLE * middleArchOffset + max(from.x, to.x) + ARCH_MINIMUM
    }

    val startOverEnd = from.y > to.y

    drawText(
        textLayoutResult = startTextLayout,
        color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
        topLeft = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) {
            Offset(
                x = from.x - textPadding - startTextLayout.size.width,
                y = from.y + if (startOverEnd) textPadding else -startTextLayout.size.height - textPadding
            )
        } else {
            Offset(
                x = from.x + textPadding,
                y = from.y + if (startOverEnd) textPadding else -startTextLayout.size.height - textPadding
            )
        }
    )

    drawText(
        textLayoutResult = nameTextLayout,
        color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
        topLeft = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) {
            Offset(
                x = middleX - textPadding - nameTextLayout.size.width,
                y = (to.y + from.y) / 2 - nameTextLayout.size.height / 2f
            )
        } else {
            Offset(
                x = middleX + textPadding,
                y = (to.y + from.y) / 2 - nameTextLayout.size.height / 2f
            )
        }
    )

    drawText(
        textLayoutResult = endTextLayout,
        color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
        topLeft = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) {
            Offset(
                x = to.x - textPadding - endTextLayout.size.width,
                y = to.y + if (startOverEnd) -endTextLayout.size.height - textPadding else textPadding
            )
        } else {
            Offset(
                x = to.x + textPadding,
                y = to.y + if (startOverEnd) -endTextLayout.size.height - textPadding else textPadding
            )
        }
    )
}