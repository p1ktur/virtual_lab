package app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: Int = 0,
    val name: String = "Course",
    val description: String = "Course Description",
    val educationalMaterials: List<EducationalMaterial> = emptyList()
)