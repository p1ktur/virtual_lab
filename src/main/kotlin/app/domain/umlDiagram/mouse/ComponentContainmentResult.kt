package app.domain.umlDiagram.mouse

sealed interface ComponentContainmentResult {
    data object None : ComponentContainmentResult
    data object Whole : ComponentContainmentResult
    data class Side(val direction: SideDirection) : ComponentContainmentResult
    data class Vertex(val direction: VertexDirection) : ComponentContainmentResult

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