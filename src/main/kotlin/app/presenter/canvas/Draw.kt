package app.presenter.canvas

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*

const val BACKGROUND_COLOR = 0xFFDDDDEE
const val GRID_COLOR = 0xFFBBBBCC
const val CENTER_HELPER_COLOR = 0xFF9999AA

const val SMOOTHEN_VALUE = 4f

fun DrawScope.drawCenterHelper(
    squareSize: Float,
    gridColor: Color
) {
    drawLine(
        color = gridColor,
        start = Offset(size.width / 2 - squareSize / 2, size.height / 2),
        end = Offset(size.width / 2 + squareSize / 2, size.height / 2),
        strokeWidth = 1.5f
    )
    drawLine(
        color = gridColor,
        start = Offset(size.width / 2, size.height / 2 - squareSize / 2),
        end = Offset(size.width / 2, size.height / 2 + squareSize / 2),
        strokeWidth = 2f
    )
}

fun DrawScope.drawGrid(
    step: Float,
    canvasZoom: Float,
    canvasOffset: Offset,
    backgroundColor: Color,
    gridColor: Color
) {
    drawRect(
        color = backgroundColor,
        topLeft = Offset.Zero,
        size = size
    )

    val offsetX = canvasOffset.x * canvasZoom % step
    val offsetY = canvasOffset.y * canvasZoom % step

    var currentX = size.width / 2
    var currentY = size.height / 2

    while (currentX <= size.width + step) {
        drawLine(
            color = gridColor,
            start = Offset(currentX + offsetX, 0f),
            end = Offset(currentX + offsetX, size.height),
            strokeWidth = 1f
        )

        currentX += step
    }

    currentX = size.width / 2 - step

    while (currentX >= -step) {
        drawLine(
            color = gridColor,
            start = Offset(currentX + offsetX, 0f),
            end = Offset(currentX + offsetX, size.height),
            strokeWidth = 1f
        )

        currentX -= step
    }

    while (currentY <= size.height + step) {
        drawLine(
            color = gridColor,
            start = Offset(0f, currentY + offsetY),
            end = Offset(size.width, currentY + offsetY),
            strokeWidth = 1f
        )

        currentY += step
    }

    currentY = size.height / 2 - step

    while (currentY >= -step) {
        drawLine(
            color = gridColor,
            start = Offset(0f, currentY + offsetY),
            end = Offset(size.width, currentY + offsetY),
            strokeWidth = 1f
        )

        currentY -= step
    }
}