package app.presenter.canvas

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.WindowPosition.PlatformDefault.x
import androidx.compose.ui.window.WindowPosition.PlatformDefault.y
import app.domain.umlDiagram.model.connection.*
import app.presenter.canvas.ArrowType.Companion.DASHED_LENGTH
import app.presenter.canvas.ArrowType.Companion.DASHED_STEP
import kotlin.math.*

enum class ArrowType {
    SOLID,
    DASHED;

    companion object {
        const val DASHED_LENGTH = 4f
        const val DASHED_STEP = 2f
    }
}

@Composable
fun DrawnArrow(
    modifier: Modifier = Modifier,
    arrowType: ArrowType = ArrowType.SOLID,
    startArrowHead: ArrowHead = ArrowHead.ASSOCIATION,
    endArrowHead: ArrowHead = ArrowHead.ASSOCIATION,
    lookingUp: Boolean = false,
    isHighlighted: Boolean = false,
    onClick: () -> Unit
) {
    val color = if (isHighlighted) Color(UMLClassConnection.HIGHLIGHT_COLOR) else Color.Black

    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(4f))
            .background(Color.White)
            .border(1.dp, color, RoundedCornerShape(4f))
            .clickable(onClick = onClick)
            .padding(4.dp),
    ) {
        drawLine(
            color = color,
            start = center.copy(y = 0f + startArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
            end = center.copy(y = size.height - endArrowHead.lengthsToDraw() * ArrowHead.ARROW_HEAD_LENGTH),
            strokeWidth = 2f,
            pathEffect = if (arrowType == ArrowType.DASHED) {
                PathEffect.dashPathEffect(floatArrayOf(DASHED_LENGTH, DASHED_STEP))
            } else null
        )
        startArrowHead.drawOn(
            drawScope = this,
            at = center.copy(y = 0f),
            length = ArrowHead.ARROW_HEAD_LENGTH,
            angleRadians = if (!lookingUp) -Math.PI.toFloat() / 2f else Math.PI.toFloat() / 2f,
            color = color
        )
        endArrowHead.drawOn(
            drawScope = this,
            at = center.copy(y = size.height),
            length = ArrowHead.ARROW_HEAD_LENGTH,
            angleRadians = if (lookingUp) -Math.PI.toFloat() / 2f else Math.PI.toFloat() / 2f,
            color = color
        )
    }
}

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

    val highlightColor = Color(UMLClassConnection.HIGHLIGHT_COLOR)

    when (relativePosition) {
        UMLClassConnection.RelativePosition.LEFT, UMLClassConnection.RelativePosition.RIGHT -> {
            val positiveCoef = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) 1 else -1

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
                end = from.copy(x = (to.x - from.x) * middleOffset + from.x),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(x = (to.x - from.x) * middleOffset + from.x),
                end = to.copy(x = (to.x - from.x) * middleOffset + from.x),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(x = (to.x - from.x) * middleOffset + from.x),
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
                end = from.copy(y = (to.y - from.y) * middleOffset + from.y),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                start = from.copy(y = (to.y - from.y) * middleOffset + from.y),
                end = to.copy(y = (to.y - from.y) * middleOffset + from.y),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )
            drawLine(
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                start = to.copy(y = (to.y - from.y) * middleOffset + from.y),
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
    relativePosition: UMLClassConnection.RelativePosition,
    middleOffset: Float,
    highlightedSegments: List<ConnectionSegment>,
    startText: String,
    name: String,
    endText: String,
    textStyle: TextStyle
) {
    val highlightColor = Color(UMLClassConnection.HIGHLIGHT_COLOR)
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

    when (relativePosition) {
        UMLClassConnection.RelativePosition.LEFT, UMLClassConnection.RelativePosition.RIGHT -> {
            drawText(
                textLayoutResult = startTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.FIRST)) highlightColor else color,
                topLeft = if (relativePosition == UMLClassConnection.RelativePosition.LEFT) {
                    Offset(
                        x = from.x - textPadding - startTextLayout.size.width,
                        y = from.y - textPadding - startTextLayout.size.height
                    )
                } else {
                    Offset(
                        x = from.x + textPadding,
                        y = from.y - textPadding - startTextLayout.size.height
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
                        y = to.y - textPadding - endTextLayout.size.height
                    )
                } else {
                    Offset(
                        x = to.x - textPadding - endTextLayout.size.width,
                        y = to.y - textPadding - endTextLayout.size.height
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
                        x = from.x + textPadding * 2,
                        y = from.y - textPadding - startTextLayout.size.height
                    )
                } else {
                    Offset(
                        x = from.x + textPadding * 2,
                        y = from.y + textPadding
                    )
                }
            )

            drawText(
                textLayoutResult = nameTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.SECOND)) highlightColor else color,
                topLeft = Offset(
                    x = (to.x + from.x) / 2 + textPadding,
                    y = (to.y - from.y) * middleOffset + from.y - nameTextLayout.size.height - textPadding
                )
            )

            drawText(
                textLayoutResult = endTextLayout,
                color = if (highlightedSegments.contains(ConnectionSegment.THIRD)) highlightColor else color,
                topLeft = if (relativePosition == UMLClassConnection.RelativePosition.TOP) {
                    Offset(
                        x = to.x + textPadding,
                        y = to.y + textPadding
                    )
                } else {
                    Offset(
                        x = to.x + textPadding,
                        y = to.y - textPadding - endTextLayout.size.height
                    )
                }
            )
        }
    }
}