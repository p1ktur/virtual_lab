package app.domain.viewModels.courses.course

import app.domain.model.*

sealed interface CourseUiAction {
    data object FetchData : CourseUiAction

    data class OpenTask(val taskId: Int): CourseUiAction
    data object CreateTask: CourseUiAction

    data class UpdateName(val name: String): CourseUiAction
    data class UpdateDescription(val description: String): CourseUiAction
    data class AddEducationMaterial(val material: EducationalMaterial): CourseUiAction
    data class RemoveEducationMaterial(val index: Int): CourseUiAction
    data object SaveChanges : CourseUiAction
}