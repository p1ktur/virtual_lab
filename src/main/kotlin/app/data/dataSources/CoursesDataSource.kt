package app.data.dataSources

import app.domain.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CoursesDataSource(
    private val httpClient: HttpClient
) {

    // COMMON
    suspend fun getCourseById(courseId: Int): Course {
        return httpClient.get {
            url {
                path("Course")
                parameters.append("courseId", courseId.toString())
            }
        }.body()
    }

    suspend fun getTasksByCourseId(courseId: Int): List<Task> {
        return httpClient.get {
            url {
                path("Course")
                parameters.append("courseId", courseId.toString())
            }
        }.body()
    }

    suspend fun getTaskById(taskId: Int): Task {
        return httpClient.get {
            url {
                path("Task")
                parameters.append("taskId", taskId.toString())
            }
        }.body()
    }

    // TEACHER
    suspend fun getCoursesByTeacherId(): List<Course> {
        return httpClient.get {
            url {
                path("Course")
                parameters.append("teacherId", "0")
            }
        }.body()
    }

    suspend fun postCourse(course: Course): List<Course> {
        return httpClient.get {
            url {
                path("Course")
                parameters.append("teacherId", "0")
                setBody(course)
            }
        }.body()
    }

    suspend fun postTaskToCourse(courseId: Int, task: Task) {
        httpClient.post {
            url {
                path("Task")
                parameters.append("courseId", courseId.toString())
                parameters.append("teacherId", "0")
                setBody(task)
            }
        }
    }

    suspend fun updateCourseById(courseId: Int, course: Course) {
        httpClient.put {
            url {
                path("Course")
                parameters.append("courseId", courseId.toString())
                setBody(course)
            }
        }
    }

    suspend fun updateTaskById(taskId: Int, task: Task) {
        httpClient.put {
            url {
                path("Task")
                parameters.append("taskId", taskId.toString())
                setBody(task)
            }
        }
    }

    // STUDENT
    suspend fun getCoursesByStudentId(): List<Course> {
        return httpClient.get {
            url {
                path("Course")
                parameters.append("studentId", "0")
            }
        }.body()
    }

    suspend fun postStudentTaskAttempt(attempt: StudentTaskAttempt) {
        httpClient.post {
            url {
                path("StudentTaskAttempt")
                parameters.append("studentId", "0")
                setBody(attempt)
            }
        }
    }
}