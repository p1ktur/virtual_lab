package app.data.dataSources

import app.domain.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CoursesDataSource(
    private val httpClient: HttpClient
) {
    // AUTH
    suspend fun getFirstTeacherUser(): User? {
        return try {
            val response = httpClient.get {
                url {
                    path("Teacher")
                }
            }

            val teacherList = response.body<List<User>>()
            teacherList[0]
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getFirstStudentUser(): User? {
        return try {
            val response = httpClient.get {
                url {
                    path("Student")
                }
            }

            val studentList = response.body<List<User>>()
            studentList[0]
        } catch (_: Exception) {
            null
        }
    }

    // COMMON
    suspend fun getStudentsListByCourseIdInverse(courseId: Int): List<User>? {
        return try {
            httpClient.get {
                url {
                    path("Student")
                    parameters.append("courseId", courseId.toString())
                    parameters.append("inverse", "1")
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getStudentsListByCourseId(courseId: Int): List<User>? {
        return try {
            httpClient.get {
                url {
                    path("Student")
                    parameters.append("courseId", courseId.toString())
                    parameters.append("inverse", "0")
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getCourseById(courseId: Int): Course? {
        return try {
            httpClient.get {
                url {
                    path("Course/$courseId")
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getTasksByCourseId(courseId: Int): List<Task>? {
        return try {
            httpClient.get {
                url {
                    path("Task")
                    parameters.append("courseId", courseId.toString())
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getTaskById(taskId: Int): Task? {
        return try {
            httpClient.get {
                url {
                    path("Task/$taskId")
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getEducationalMaterialsByCourseId(courseId: Int): List<EducationalMaterial>? {
        return try {
            httpClient.get {
                url {
                    path("EducationalMaterial")
                    parameters.append("courseId", courseId.toString())
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getEducationalMaterialById(educationalMaterialId: Int): EducationalMaterial? {
        return try {
            httpClient.get {
                url {
                    path("EducationalMaterial/$educationalMaterialId")
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    // TEACHER
    suspend fun addStudentToCourse(courseId: Int, studentId: Int) {
        try {
            httpClient.post {
                url {
                    path("Course")
                    parameters.append("courseId", courseId.toString())
                    parameters.append("studentId", studentId.toString())
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun removeStudentFromCourse(courseId: Int, studentId: Int) {
        try {
            httpClient.delete {
                url {
                    path("Course")
                    parameters.append("courseId", courseId.toString())
                    parameters.append("studentId", studentId.toString())
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun getCoursesByTeacherId(teacherId: Int): List<Course>? {
        return try {
            httpClient.get {
                url {
                    path("Course")
                    parameters.append("teacherId", teacherId.toString())
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun postCourse(teacherId: Int, course: Course): Int? {
        return try {
            httpClient.post {
                url {
                    path("Course")
                    parameters.append("teacherId", teacherId.toString())

                    contentType(ContentType.Application.Json)
                    setBody(course)
                }
            }.body<Course>().id
        } catch (_: Exception) {
            null
        }
    }

    suspend fun updateCourseById(courseId: Int, course: Course) {
        try {
            httpClient.put {
                url {
                    path("Course/$courseId")

                    contentType(ContentType.Application.Json)
                    setBody(course)
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun deleteCourseById(courseId: Int) {
        try {
            httpClient.delete {
                url {
                    path("Course/$courseId")
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun postTaskToCourse(teacherId: Int, task: Task): Int? {
        return try {
            httpClient.post {
                url {
                    path("Task")
                    parameters.append("teacherId", teacherId.toString())

                    contentType(ContentType.Application.Json)
                    setBody(task)
                }
            }.body<Task>().id
        } catch (_: Exception) {
            null
        }
    }

    suspend fun updateTaskById(taskId: Int, task: Task) {
        try {
            httpClient.put {
                url {
                    path("Task/$taskId")

                    contentType(ContentType.Application.Json)
                    setBody(task)
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun deleteTaskById(taskId: Int) {
        try {
            httpClient.delete {
                url {
                    path("Task/$taskId")
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun postEducationalMaterialToCourse(educationalMaterial: EducationalMaterial): Int? {
        return try {
            httpClient.post {
                url {
                    path("EducationalMaterial")

                    contentType(ContentType.Application.Json)
                    setBody(educationalMaterial)
                }
            }.body<EducationalMaterial>().id
        } catch (_: Exception) {
            null
        }
    }

    suspend fun updateEducationalMaterialById(educationalMaterialId: Int, educationalMaterial: EducationalMaterial) {
        try {
            httpClient.put {
                url {
                    path("EducationalMaterial/$educationalMaterialId")

                    contentType(ContentType.Application.Json)
                    setBody(educationalMaterial)
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun deleteEducationalMaterialById(educationalMaterialId: Int) {
        try {
            httpClient.delete {
                url {
                    path("EducationalMaterial/$educationalMaterialId")
                }
            }
        } catch (_: Exception) {}
    }

    suspend fun getStudentAttemptsByCourseId(courseId: Int): List<StudentTaskAttempt>? {
        return try {
            httpClient.get {
                url {
                    path("StudentTaskAttempt")
                    parameters.append("courseId", courseId.toString())
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    // STUDENT
    suspend fun getCoursesByStudentId(studentId: Int): List<Course>? {
        return try {
            httpClient.get {
                url {
                    path("Course")
                    parameters.append("studentId", studentId.toString())
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun postStudentTaskAttempt(attempt: StudentTaskAttempt): StudentTaskAttempt? {
        return try {
            httpClient.post {
                url {
                    path("StudentTaskAttempt")

                    contentType(ContentType.Application.Json)
                    setBody(attempt)
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getStudentAttemptsByStudentId(courseId: Int, studentId: Int): List<StudentTaskAttempt>? {
        return try {
            httpClient.get {
                url {
                    path("StudentTaskAttempt")
                    parameters.append("courseId", courseId.toString())
                    parameters.append("studentId", studentId.toString())
                }
            }.body()
        } catch (_: Exception) {
            null
        }
    }
}