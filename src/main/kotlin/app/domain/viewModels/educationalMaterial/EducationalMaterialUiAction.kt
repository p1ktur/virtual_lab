package app.domain.viewModels.educationalMaterial

sealed interface EducationalMaterialUiAction {
    data object FetchData : EducationalMaterialUiAction

    data class UpdateName(val name: String): EducationalMaterialUiAction
    data class UpdateDescription(val description: String): EducationalMaterialUiAction

    data object AddURL: EducationalMaterialUiAction
    data class UpdateURL(val index: Int, val url: String): EducationalMaterialUiAction
    data class DeleteURL(val index: Int): EducationalMaterialUiAction

    data object SaveChanges : EducationalMaterialUiAction
    data object DeleteEducationalMaterial : EducationalMaterialUiAction
}