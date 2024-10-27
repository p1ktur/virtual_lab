package app.data.server

import app.data.dataSources.*
import app.domain.auth.*

class ServerRepository(
    private val coursesDataSource: CoursesDataSource
) {

    var authenticatedType = AuthType.TEACHER
        private set

    fun authenticateAs(authType: AuthType) {
        this.authenticatedType = authType
    }
}