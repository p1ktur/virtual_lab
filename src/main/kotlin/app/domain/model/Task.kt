package app.domain.model

import kotlinx.serialization.*

@Serializable
data class Task(
    val id: Int = 0,
    val courseId: Int = 0,
    val name: String = "Task",
    val description: String = "Task Description",
    @SerialName("dataJSON") val diagramJson: String = "",
    val maxAttempts: Int = 0
)
