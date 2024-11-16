package app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val educationalMaterials: List<EducationalMaterial> = emptyList()
)