package app.domain.viewModels.studentsList

import app.domain.model.*

data class StudentsListUiState(
    val studentsList: List<User> = emptyList()
)