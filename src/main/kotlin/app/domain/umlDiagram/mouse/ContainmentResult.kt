package app.domain.umlDiagram.mouse

sealed interface ContainmentResult {
    data object None : ContainmentResult
    data object Whole : ContainmentResult
    data class Side(val direction: SideDirection) : ContainmentResult
    data class Vertex(val direction: VertexDirection) : ContainmentResult

    companion object {
        const val SIDE_CHECK_RADIUS = 16f
        const val VERTEX_CHECK_RADIUS = 16f
    }
}

enum class SideDirection {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM
}

enum class VertexDirection {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
}