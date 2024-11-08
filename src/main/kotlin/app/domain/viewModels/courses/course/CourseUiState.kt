package app.domain.viewModels.courses.course

import app.domain.model.*

data class CourseUiState(
    val course: Course = Course(),
    val tasks: List<Task> = emptyList(),
    val showSaveChangesButton: Boolean = false
)