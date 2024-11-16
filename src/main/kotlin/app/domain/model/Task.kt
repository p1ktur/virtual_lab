package app.domain.model

import kotlinx.serialization.*

@Serializable
data class Task(
    val id: Int = 0,
    val courseId: Int = 0,
    val name: String = "",
    val description: String = "",
    @SerialName("maxRate") val maxMark: Int = 100,
    @SerialName("minRate") val minMark: Int = 25,
    @SerialName("dataJSON") val diagramJson: String = "",
    val maxAttempts: Int = 0
)
