package app.domain.model

import kotlinx.serialization.*

@Serializable
data class EducationalMaterial(
    val id: Int = 0,
    val courseId: Int = 0,
    val name: String = "",
    val description: String = "",
    @SerialName("cloudDriveAttachedFileURLs") val urls: List<String> = emptyList()
)