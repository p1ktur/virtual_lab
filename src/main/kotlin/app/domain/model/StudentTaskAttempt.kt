package app.domain.model

import kotlinx.serialization.*

@Serializable
data class StudentTaskAttempt(
    val id: Int,
    val studentId: Int,
    val taskId: Int,
    val attemptDate: String,
    val isSuccessful: Boolean,
    val number: Int,
    @SerialName("rate") val mark: Int,
    @SerialName("studentDataJSON") val studentDiagramJson: String
)