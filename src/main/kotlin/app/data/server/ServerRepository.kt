package app.data.server

import app.data.dataSources.*
import app.domain.auth.*
import app.domain.model.*
import kotlinx.coroutines.flow.*

class ServerRepository(
    private val coursesDataSource: CoursesDataSource
) {

    companion object {
        const val BASE_URL = "https://localhost:7205/api/"
    }

    private var _authenticatedType = MutableStateFlow(AuthType.TEACHER)
    val authenticatedType = _authenticatedType.asStateFlow()

    fun authenticateAs(authType: AuthType) {
        _authenticatedType.value = authType
    }

    // COMMON
    suspend fun getCourse(courseId: Int): Course {
        return coursesDataSource.getCourseById(courseId)
    }

    suspend fun getCourseTasks(courseId: Int): List<Task> {
        return coursesDataSource.getTasksByCourseId(courseId)
    }

    suspend fun getTask(taskId: Int): Task {
        return coursesDataSource.getTaskById(taskId)
    }

    // TEACHER
    suspend fun getTeacherCourses(): List<Course> {
        return coursesDataSource.getCoursesByTeacherId()
    }

    suspend fun createCourse(course: Course) {
        coursesDataSource.postCourse(course)
    }

    suspend fun addTaskToCourse(courseId: Int, task: Task) {
        coursesDataSource.postTaskToCourse(courseId, task)
    }

    suspend fun updateCourse(courseId: Int, course: Course) {
        coursesDataSource.updateCourseById(courseId, course)
    }

    suspend fun updateTask(taskId: Int, task: Task) {
        coursesDataSource.updateTaskById(taskId, task)
    }

    // STUDENT
    suspend fun getStudentCourses(): List<Course> {
        return coursesDataSource.getCoursesByStudentId()
    }

    suspend fun submitStudentAttempt(attempt: StudentTaskAttempt) {
        coursesDataSource.postStudentTaskAttempt(attempt)
    }
}