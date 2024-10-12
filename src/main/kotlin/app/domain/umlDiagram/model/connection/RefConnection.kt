package app.domain.umlDiagram.model.connection

import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.UMLClassConnection.*
import kotlinx.serialization.Serializable

@Serializable
sealed class RefConnection(open val ref: UMLClassComponent) {
    data class SimpleConnection(override val ref: UMLClassComponent, val offset: ConnectionOffset = ConnectionOffset()) : RefConnection(ref)
    data class ReferencedConnection(override val ref: UMLClassComponent, val refType: RefType) : RefConnection(ref)
}

sealed interface RefType {
    data class Field(val index: Int) : RefType
    data class Function(val index: Int) : RefType
}

fun RefConnection.getOffsets(): ConnectionOffset? {
    return when (this) {
        is RefConnection.ReferencedConnection -> null
        is RefConnection.SimpleConnection -> offset
    }
}