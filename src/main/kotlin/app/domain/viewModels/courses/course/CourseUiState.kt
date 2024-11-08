package app.domain.viewModels.courses.course

import app.domain.auth.*
import app.domain.model.*

data class CourseUiState(
    val authType: AuthType,
    val course: Course = Course(),
    val tasks: List<Task> = emptyList(),
    val showSaveChangesButton: Boolean = false
)