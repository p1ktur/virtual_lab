package app.domain.umlDiagram.comparing

data class Mark(
    val componentPresence: Float,
    val connectionPresence: Float,
    val componentAccordance: Float,
    val connectionAccordance: Float
) {
    fun value(maxMark: Float): Float {
        return maxMark * 0.25f * (componentPresence + connectionPresence) * (componentAccordance + connectionAccordance)
    }
}
