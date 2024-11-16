package app.domain.umlDiagram.comparing

import kotlin.math.*

data class Mark(
    val componentPresence: Float,
    val connectionPresence: Float,
    val componentAccordance: Float,
    val connectionAccordance: Float
) {
    fun value(maxMark: Float): Float {
        return maxMark * 0.25f * (componentPresence + connectionPresence + componentAccordance + connectionAccordance)
    }

    fun value(maxMark: Int): Int {
        return value(maxMark.toFloat()).roundToInt()
    }
}
