package app.domain.umlDiagram.model.component;

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
}