package app.domain.viewModels.educationalMaterial

import app.domain.auth.*
import app.domain.model.*

data class EducationalMaterialUiState(
    val authType: AuthType,
    val educationalMaterial: EducationalMaterial = EducationalMaterial(),
    val showSaveChangesButton: Boolean = false
)