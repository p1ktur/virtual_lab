package app.domain.umlDiagram.model.connection

import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.UMLClassConnection.*
import kotlinx.serialization.Serializable

@Serializable
sealed class RefConnection(open val ref: UMLClassComponent) {
    data class SimpleConnection(override val ref: UMLClassComponent, val offset: ConnectionOffset = ConnectionOffset()) : RefConnection(ref)
    data class ReferencedConnection(override val ref: UMLClassComponent, val refType: RefType) : RefConnection(ref)
}

sealed class RefType(open val index: Int) {
    data class Field(override val index: Int) : RefType(index)
    data class Function(override val index: Int) : RefType(index)
}

fun RefConnection.getOffsets(): ConnectionOffset? {
    return when (this) {
        is RefConnection.ReferencedConnection -> null
        is RefConnection.SimpleConnection -> offset
    }
}