package app.domain.umlDiagram.mouse

sealed interface ConnectionContainmentResult {
    data object None : ConnectionContainmentResult
    data object Whole : ConnectionContainmentResult
    data class FirstSegment(val direction: SegmentDirection) : ConnectionContainmentResult
    data class SecondSegment(val direction: SegmentDirection) : ConnectionContainmentResult
    data class ThirdSegment(val direction: SegmentDirection) : ConnectionContainmentResult

    companion object {
        const val SEGMENT_CHECK_RADIUS = 16f
    }
}

enum class SegmentDirection {
    HORIZONTAL,
    VERTICAL
}