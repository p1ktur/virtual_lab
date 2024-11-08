package app.domain.viewModels.courses.coursesList

import app.domain.auth.*
import app.domain.model.*

data class CoursesListUiState(
    val authType: AuthType,
    val courses: List<Course> = emptyList()
)