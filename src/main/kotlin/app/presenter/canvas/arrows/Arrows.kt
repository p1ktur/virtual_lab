package app.presenter.canvas.arrows

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.presenter.canvas.arrows.ArrowType.Companion.DASHED_LENGTH
import app.presenter.canvas.arrows.ArrowType.Companion.DASHED_STEP
import app.presenter.theme.*
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
    mainColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    val color = if (isHighlighted) highlightColor else mainColor

    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(4f))
            .background(backgroundColor)
            .border(1.dp, color, RoundedCornerShape(4f))
            .clickable(onClick = onClick)
            .padding(6.dp),
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