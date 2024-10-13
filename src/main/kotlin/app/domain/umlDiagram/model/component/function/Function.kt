package app.domain.umlDiagram.model.component.function

import app.domain.umlDiagram.model.component.*
import kotlinx.serialization.Serializable

@Serializable
data class Function(
    var name: String = "",
    var returnType: String = "",
    val params: MutableList<Param> = mutableListOf(),
    var visibility: Visibility = Visibility.PUBLIC,
    var isStatic: Boolean = false
) {
    override fun toString(): String {
        val paramsText = params.joinToString { p -> p.name + if (p.type.isNotBlank()) ": ${p.type}" else "" }
        return "${visibility.symbol}" + (if (name.isNotBlank()) "${name}($paramsText)" else "") + (if (returnType.isNotBlank()) ": $returnType" else "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Function

        if (name != other.name) return false
        if (returnType != other.returnType) return false
        if (!params.containsAll(other.params)) return false
        if (visibility != other.visibility) return false
        if (isStatic != other.isStatic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + returnType.hashCode()
        result = 31 * result + params.toTypedArray().contentHashCode()
        result = 31 * result + visibility.hashCode()
        result = 31 * result + isStatic.hashCode()
        return result
    }
}