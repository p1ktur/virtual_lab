package app.domain.model

import app.domain.serialization.*
import kotlinx.serialization.*

@Serializable
data class User(
    val id: Int = 0,
    @SerialName("userRoleId")
    @Serializable(with = UserRoleSerializer::class)
    val userRole: UserRole = UserRole.STUDENT,
    val name: String = "",
    val middleName: String = "",
    val surname: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = ""
) {
    enum class UserRole(val id: Int) {
        ADMINISTRATOR(1),
        TEACHER(2),
        STUDENT(3)
    }
}
