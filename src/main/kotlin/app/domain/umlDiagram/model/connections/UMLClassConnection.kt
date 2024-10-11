package app.domain.umlDiagram.model.connections

data class UMLClassConnection(
    val type: Type,
    val startRef: RefConnection,
    val endRef: RefConnection,
    val startOffset: ConnectionOffset = ConnectionOffset(),
    val endOffset: ConnectionOffset = ConnectionOffset()
) {
    enum class Type {
        HVH,           // horizontal-vertical-horizontal
        VHV            // vertical-horizontal-vertical
    }

    data class ConnectionOffset(
        val left: Float = 0.5f,
        val top: Float = 0.5f,
        val right: Float = 0.5f,
        val bottom: Float = 0.5f
    )
}
