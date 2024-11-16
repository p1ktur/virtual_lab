package app.domain.viewModels.courses.course

sealed interface CourseUiAction {
    data object FetchData : CourseUiAction

    data class OpenTask(val taskId: Int) : CourseUiAction
    data object CreateTask: CourseUiAction
    data class DeleteTask(val taskId: Int) : CourseUiAction

    data object CreateEducationalMaterial : CourseUiAction
    data class OpenEducationalMaterial(val educationalMaterialId: Int) : CourseUiAction
    data class DeleteEducationalMaterial(val educationalMaterialId: Int) : CourseUiAction

    data class UpdateName(val name: String) : CourseUiAction
    data class UpdateDescription(val description: String) : CourseUiAction

    data class OpenStudentsList(val inverse: Boolean) : CourseUiAction
    data class AddStudentToCourse(val studentId: Int) : CourseUiAction
    data class RemoveStudentFromCourse(val studentId: Int) : CourseUiAction

    data object SaveChanges : CourseUiAction
    data object DeleteCourse : CourseUiAction
}