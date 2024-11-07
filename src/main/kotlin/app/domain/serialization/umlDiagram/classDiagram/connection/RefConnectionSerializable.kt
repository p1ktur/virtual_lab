package app.domain.serialization.umlDiagram.classDiagram.connection

import app.domain.umlDiagram.classDiagram.connection.*
import app.domain.umlDiagram.classDiagram.connection.UMLClassConnection.*
import kotlinx.serialization.*

@Serializable
sealed interface RefConnectionSerializable {

    @Serializable
    data class Simple(var index: Int, val offset: ConnectionOffset = ConnectionOffset()) : RefConnectionSerializable
    @Serializable
    data class Referenced(var index: Int, val refType: RefType) : RefConnectionSerializable

    fun getRefIndex(): Int {
        return when (this) {
            is Simple -> this.index
            is Referenced -> this.index
        }
    }
}