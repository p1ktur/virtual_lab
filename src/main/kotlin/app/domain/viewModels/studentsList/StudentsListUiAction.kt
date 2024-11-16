package app.domain.viewModels.studentsList

sealed interface StudentsListUiAction {
    data class FetchData(val inverse: Boolean = true) : StudentsListUiAction
    data class SelectStudent(val studentId: Int) : StudentsListUiAction
}