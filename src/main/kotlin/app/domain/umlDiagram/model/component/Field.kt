package app.domain.umlDiagram.model.component;

import kotlinx.serialization.Serializable

@Serializable
data class Field(
    var name: String = "",
    var type: String = "",
    var visibility: Visibility = Visibility.PUBLIC,
    var isStatic: Boolean = false
)