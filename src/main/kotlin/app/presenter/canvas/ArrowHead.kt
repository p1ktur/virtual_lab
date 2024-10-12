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
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.connection.*
import app.domain.umlDiagram.model.connection.UMLClassConnection.Companion.ARROW_COLOR

const val THICKNESS = 0.4f

enum class ArrowHead {
    ASSOCIATION {
        override fun drawOn(drawScope: DrawScope, at: Offset, length: Float, angleRadians: Float, color: Color) = Unit
    },
    NAVIGABLE_ASSOCIATION_OR_DEPENDENCY {
        override fun drawOn(drawScope: DrawScope, at: Offset, length: Float, angleRadians: Float, color: Color) {
            val path = Path().apply {
                moveTo(at.x - length, at.y + THICKNESS * length)
                this.relativeLineTo(length, -THICKNESS * length)
                this.relativeLineTo(-length, -THICKNESS * length)
            }

            drawScope.rotateRad(angleRadians, at) {
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(1f)
                )
            }
        }
    },
    INHERITANCE_OR_REALIZATION_OR_IMPLEMENTATION {
        override fun drawOn(drawScope: DrawScope, at: Offset, length: Float, angleRadians: Float, color: Color) {
            val path = Path().apply {
                moveTo(at.x - length, at.y)
                this.relativeLineTo(0f, THICKNESS * length)
                this.relativeLineTo(length, -THICKNESS * length)
                this.relativeLineTo(-length, -THICKNESS * length)
                this.relativeLineTo(0f, THICKNESS * length)
            }

            drawScope.rotateRad(angleRadians, at) {
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(1f)
                )
            }
        }
    },
    AGGREGATION {
        override fun drawOn(drawScope: DrawScope, at: Offset, length: Float, angleRadians: Float, color: Color) {
            val path = Path().apply {
                moveTo(at.x - 2 * length, at.y)
                this.relativeLineTo(length, THICKNESS * length)
                this.relativeLineTo(length, -THICKNESS * length)
                this.relativeLineTo(-length, -THICKNESS * length)
                this.relativeLineTo(-length, THICKNESS * length)
            }

            drawScope.rotateRad(angleRadians, at) {
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(1f)
                )
            }
        }
    },
    COMPOSITION {
        override fun drawOn(drawScope: DrawScope, at: Offset, length: Float, angleRadians: Float, color: Color) {
            val path = Path().apply {
                moveTo(at.x - 2 * length, at.y)
                this.relativeLineTo(length, THICKNESS * length)
                this.relativeLineTo(length, -THICKNESS * length)
                this.relativeLineTo(-length, -THICKNESS * length)
                this.relativeLineTo(-length, THICKNESS * length)
            }

            drawScope.rotateRad(angleRadians, at) {
                drawPath(
                    path = path,
                    color = color
                )
            }
        }
    };

    companion object {
        const val ARROW_HEAD_LENGTH = 12f
    }

    abstract fun drawOn(
        drawScope: DrawScope,
        at: Offset,
        length: Float,
        angleRadians: Float,
        color: Color
    )

    fun lengthsToDraw(): Int {
        return when (this) {
            ASSOCIATION -> 0
            NAVIGABLE_ASSOCIATION_OR_DEPENDENCY -> 0
            INHERITANCE_OR_REALIZATION_OR_IMPLEMENTATION -> 1
            AGGREGATION -> 2
            COMPOSITION -> 2
        }
    }

    fun takesTwoLengthToDraw(): Boolean {
        return when (this) {
            ASSOCIATION -> false
            NAVIGABLE_ASSOCIATION_OR_DEPENDENCY -> false
            INHERITANCE_OR_REALIZATION_OR_IMPLEMENTATION -> false
            AGGREGATION -> true
            COMPOSITION -> true
        }
    }
}

@Composable
fun DrawnArrowHead(
    modifier: Modifier = Modifier,
    modifierSize: DpSize,
    arrowHead: ArrowHead = ArrowHead.ASSOCIATION,
    lookingUp: Boolean = true,
    isHighlighted: Boolean = false,
    onClick: () -> Unit
) {
    val color = if (isHighlighted) Color(UMLClassConnection.HIGHLIGHT_COLOR) else Color.Black
    val offsetLength = if (arrowHead.lengthsToDraw() == 2) {
        modifierSize.height.value
    } else {
        modifierSize.height.div(2).value
    }

    Canvas(
        modifier = modifier
            .size(modifierSize)
            .clip(RoundedCornerShape(4f))
            .background(Color.White)
            .border(1.dp, color, RoundedCornerShape(4f))
            .clickable(onClick = onClick)
            .padding(4.dp),
    ) {
        if (arrowHead == ArrowHead.ASSOCIATION) {
            drawRoundRect(
                color = color,
                topLeft = Offset(size.width * 0.1f, size.height * 0.1f),
                size = Size(size.width * 0.8f, size.height * 0.8f),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(1f)
            )
        } else {
            translate(
                top = if (lookingUp) -offsetLength / 2f else offsetLength / 2f
            ) {
                arrowHead.drawOn(
                    drawScope = this,
                    at = center,
                    length = modifierSize.height.div(2).value,
                    angleRadians = if (lookingUp) -Math.PI.toFloat() / 2f else Math.PI.toFloat() / 2f,
                    color = color
                )
            }
        }
    }
}

fun DrawScope.drawSimpleArrowHead(
    at: Offset,
    length: Float,
    angleRadians: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(at.x - length, at.y)
        this.relativeLineTo(0f, THICKNESS * length)
        this.relativeLineTo(length, -THICKNESS * length)
        this.relativeLineTo(-length, -THICKNESS * length)
        this.relativeLineTo(0f, THICKNESS * length)
    }

    rotateRad(angleRadians, at) {
        drawPath(
            path = path,
            color = color
        )
    }
}