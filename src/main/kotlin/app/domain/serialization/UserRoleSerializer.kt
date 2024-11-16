package app.domain.serialization

import app.domain.model.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object UserRoleSerializer : KSerializer<User.UserRole> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UserRole", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): User.UserRole {
        val intValue = decoder.decodeInt()
        val index = User.UserRole.entries.map { it.id }.indexOf(intValue)
        return User.UserRole.entries[index]
    }

    override fun serialize(encoder: Encoder, value: User.UserRole) {
        return encoder.encodeInt(value.id)
    }
}