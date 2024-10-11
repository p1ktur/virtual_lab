package app.presenter.canvas

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*

fun DrawScope.drawSimpleArrowHead(
    at: Offset,
    length: Float,
    angleRadians: Float,
    color: Color
) {
    val thickness = 0.35f

    val path = Path().apply {
        moveTo(at.x - length, at.y)
        this.relativeLineTo(0f, thickness * length)
        this.relativeLineTo(length, -thickness * length)
        this.relativeLineTo(-length, -thickness * length)
        this.relativeLineTo(0f, thickness * length)
    }

    rotateRad(angleRadians, at) {
        drawPath(
            path = path,
            color = color
        )
    }
}