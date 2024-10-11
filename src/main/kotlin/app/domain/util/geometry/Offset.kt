package app.domain.util.geometry

import androidx.compose.ui.geometry.*

fun Offset.zoom(center: Offset, zoom: Float): Offset {
    val x = center.x - (center.x - this.x) * zoom
    val y = center.y - (center.y - this.y) * zoom

    return Offset(x, y)
}

fun Offset.unZoom(center: Offset, zoom: Float): Offset {
    val x = (this.x - center.x) / zoom + center.x
    val y = (this.y - center.y) / zoom + center.y

    return Offset(x, y)
}

fun Offset.isAround(center: Offset, radius: Float): Boolean {
    return x in (center.x - radius)..(center.x + radius) && y in (center.y - radius)..(center.y + radius)
}