package app.domain.viewModels.courses.coursesList

import app.domain.model.*

data class CoursesListUiState(
    val courses: List<Course> = emptyList()
)