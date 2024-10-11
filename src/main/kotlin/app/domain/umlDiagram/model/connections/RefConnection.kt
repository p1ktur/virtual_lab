package app.domain.umlDiagram.model.connections

import app.domain.umlDiagram.model.components.*

sealed interface RefConnection {
    data class SimpleConnection(val ref: UMLClassComponent) : RefConnection
    data class ReferencedConnection(val ref: UMLClassComponent, val refType: RefType) : RefConnection
}

sealed interface RefType {
    data class Field(val index: Int) : RefType
    data class Function(val index: Int) : RefType
}