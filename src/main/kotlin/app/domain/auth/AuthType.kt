package app.domain.auth

import app.domain.model.*

sealed interface AuthType {
    data class Administrator(val user: User) : AuthType
    data class Teacher(val user: User) : AuthType
    data class Student(val user: User) : AuthType
}