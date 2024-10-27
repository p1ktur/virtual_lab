package app.domain.umlDiagram.classDiagram.connection

import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.UMLClassConnection.*
import kotlinx.serialization.*

@Serializable
sealed interface RefConnection {

    @Serializable
    data class SimpleConnection(var ref: UMLClassComponent, val offset: ConnectionOffset = ConnectionOffset()) : RefConnection
    @Serializable
    data class ReferencedConnection(var ref: UMLClassComponent, val refType: RefType) : RefConnection

    fun getRefClass(): UMLClassComponent {
        return when (this) {
            is ReferencedConnection -> this.ref
            is SimpleConnection -> this.ref
        }
    }

    fun setRefClass(ref: UMLClassComponent) {
        when (this) {
            is ReferencedConnection -> this.ref = ref
            is SimpleConnection -> this.ref = ref
        }
    }
}

@Serializable
sealed interface RefType {
    @Serializable
    data class Field(val index: Int) : RefType
    @Serializable
    data class Function(val index: Int) : RefType

    fun getTypeIndex(): Int {
        return when (this) {
            is Field -> this.index
            is Function -> this.index
        }
    }
}

fun RefConnection.getOffsets(): ConnectionOffset? {
    return when (this) {
        is RefConnection.ReferencedConnection -> null
        is RefConnection.SimpleConnection -> offset
    }
}