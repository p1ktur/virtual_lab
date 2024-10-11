package app.presenter.canvas

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.*
import kotlin.math.*

const val BACKGROUND_COLOR = 0xFFDDDDEE
const val GRID_COLOR = 0xFFBBBBCC
const val CENTER_HELPER_COLOR = 0xFF9999AA

fun DrawScope.drawCenterHelper(squareSize: Float) {
    drawLine(
        color = Color(CENTER_HELPER_COLOR),
        start = Offset(size.width / 2 - squareSize / 2, size.height / 2),
        end = Offset(size.width / 2 + squareSize / 2, size.height / 2),
        strokeWidth = 2f
    )
    drawLine(
        color = Color(CENTER_HELPER_COLOR),
        start = Offset(size.width / 2, size.height / 2 - squareSize / 2),
        end = Offset(size.width / 2, size.height / 2 + squareSize / 2),
        strokeWidth = 2f
    )
}

fun DrawScope.drawGrid(step: Float, canvasZoom: Float, canvasOffset: Offset) {
    drawRect(
        color = Color(BACKGROUND_COLOR),
        topLeft = Offset.Zero,
        size = size
    )

    val offsetX = canvasOffset.x * canvasZoom % step
    val offsetY = canvasOffset.y * canvasZoom % step

    var currentX = size.width / 2
    var currentY = size.height / 2

    while (currentX <= size.width + step) {
        drawLine(
            color = Color(GRID_COLOR),
            start = Offset(currentX + offsetX, 0f),
            end = Offset(currentX + offsetX, size.height),
            strokeWidth = 1f
        )

        currentX += step
    }

    currentX = size.width / 2

    while (currentX >= -step) {
        drawLine(
            color = Color(GRID_COLOR),
            start = Offset(currentX + offsetX, 0f),
            end = Offset(currentX + offsetX, size.height),
            strokeWidth = 1f
        )

        currentX -= step
    }

    while (currentY <= size.height + step) {
        drawLine(
            color = Color(GRID_COLOR),
            start = Offset(0f, currentY + offsetY),
            end = Offset(size.width, currentY + offsetY),
            strokeWidth = 1f
        )

        currentY += step
    }

    currentY = size.height / 2

    while (currentY >= -step) {
        drawLine(
            color = Color(GRID_COLOR),
            start = Offset(0f, currentY + offsetY),
            end = Offset(size.width, currentY + offsetY),
            strokeWidth = 1f
        )

        currentY -= step
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

fun DrawScope.drawSquaredArrowFromTo(
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