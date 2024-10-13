package app.domain.umlDiagram.model.component

import kotlinx.serialization.Serializable

@Serializable
data class Function(
    var name: String = "",
    var returnType: String = "",
    val params: MutableList<Param> = mutableListOf(),
    var visibility: Visibility = Visibility.PUBLIC,
    var isStatic: Boolean = false
) {
    @Serializable
    data class Param(
        var name: String = "",
        var type: String = ""
    )

    override fun toString(): String {
        val paramsText = params.joinToString { p -> p.name + if (p.type.isNotBlank()) ": ${p.type}" else "" }
        return "${visibility.symbol}" + (if (name.isNotBlank()) "${name}($paramsText)" else "") + (if (returnType.isNotBlank()) ": $returnType" else "")
    }
}