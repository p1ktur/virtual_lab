package app.domain.util.geometry

import androidx.compose.ui.geometry.*
import app.presenter.canvas.*

fun Offset.smoothen(about: Float = SMOOTHEN_VALUE): Offset {
    return copy(
        x = x - x % about,
        y = y - y % about
    )
}

//fun Size.smoothen(about: Float = SMOOTHEN_VALUE): Size {
//    return copy(
//        width = width - width % about,
//        height = height - height % about
//    )
//}

fun Float.smoothen(about: Float = SMOOTHEN_VALUE): Float {
    return this - this % about
}