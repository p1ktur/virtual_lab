package app.domain.umlDiagram.classDiagram.component

import kotlinx.serialization.Serializable

@Serializable
data class Field(
    var name: String = "",
    var type: String = "",
    var visibility: Visibility = Visibility.PUBLIC,
    var isStatic: Boolean = false
) {
    override fun toString(): String {
        return "${visibility.symbol}${name}" + if (type.isNotBlank()) ": $type" else ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Field

        if (name != other.name) return false
        if (type != other.type) return false
        if (visibility != other.visibility) return false
        if (isStatic != other.isStatic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + visibility.hashCode()
        result = 31 * result + isStatic.hashCode()
        return result
    }
}