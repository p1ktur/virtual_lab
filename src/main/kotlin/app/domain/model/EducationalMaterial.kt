package app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EducationalMaterial(
    val id: Int,
    val courseId: Int,
    val name: String,
    val description: String,
    val cloudDriveAttachedFileURLs: List<String>
)