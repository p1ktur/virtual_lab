package app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentTaskAttempt(
    val id: Int,
    val studentId: Int,
    val taskId: Int,
    val attemptDate: String,
    val isSuccessful: Boolean,
    val number: Int,
    val rate: Int,
    val studentDataJSON: String
)