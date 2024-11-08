package app.domain.viewModels.courses.coursesList

sealed interface CoursesListUiAction {
    data object FetchData : CoursesListUiAction

    data class OpenCourse(val courseId: Int): CoursesListUiAction
    data object CreateCourse: CoursesListUiAction
}