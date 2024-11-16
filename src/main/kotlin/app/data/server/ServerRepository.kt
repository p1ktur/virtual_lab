package app.data.server

import app.data.dataSources.*
import app.domain.auth.*
import app.domain.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@Suppress("OPT_IN_USAGE")
class ServerRepository(
    private val coursesDataSource: CoursesDataSource
) {

    companion object {
        const val BASE_URL = "http://localhost:5046/api/"
    }

    private var _authenticatedType = MutableStateFlow<AuthType>(AuthType.Teacher(User(-1)))
    val authenticatedType = _authenticatedType.asStateFlow()

    private var _authenticating = MutableStateFlow(true)
    val authenticating = _authenticating.asStateFlow()

    init {
        authenticateAs(User.UserRole.TEACHER, GlobalScope)
    }

    fun authenticateAs(userRole: User.UserRole, scope: CoroutineScope) {
        _authenticating.value = true
        scope.launch(Dispatchers.IO) {
            val user = when (userRole) {
                User.UserRole.ADMINISTRATOR -> null
                User.UserRole.TEACHER -> getTeacherUser()
                User.UserRole.STUDENT -> getStudentUser()
            }

            if (user == null) return@launch

            _authenticatedType.value = when (userRole) {
                User.UserRole.TEACHER -> AuthType.Teacher(user)
                User.UserRole.STUDENT -> AuthType.Student(user)
                else -> AuthType.Administrator(User())
            }
            _authenticating.value = false
        }
    }

    fun getAuthId(): Int? {
        return when (authenticatedType.value) {
            is AuthType.Administrator -> (authenticatedType.value as? AuthType.Administrator)?.user?.id
            is AuthType.Student -> (authenticatedType.value as? AuthType.Student)?.user?.id
            is AuthType.Teacher -> (authenticatedType.value as? AuthType.Teacher)?.user?.id
        }
    }

    // AUTH
    private suspend fun getTeacherUser(): User? {
        return coursesDataSource.getFirstTeacherUser()
    }

    private suspend fun getStudentUser(): User? {
        return coursesDataSource.getFirstStudentUser()
    }

    // COMMON
    suspend fun getStudentsList(courseId: Int): List<User>? {
        return coursesDataSource.getStudentsListByCourseIdInverse(courseId)
    }

    suspend fun getStudentsListFromCourse(courseId: Int): List<User>? {
        return coursesDataSource.getStudentsListByCourseId(courseId)
    }

    suspend fun getCourse(courseId: Int): Course? {
        return coursesDataSource.getCourseById(courseId)
    }

    suspend fun getCourseTasks(courseId: Int): List<Task>? {
        return coursesDataSource.getTasksByCourseId(courseId)
    }

    suspend fun getTask(taskId: Int): Task? {
        return coursesDataSource.getTaskById(taskId)
    }

    suspend fun getCourseEducationalMaterials(courseId: Int): List<EducationalMaterial>? {
        return coursesDataSource.getEducationalMaterialsByCourseId(courseId)
    }

    suspend fun getEducationalMaterial(educationalMaterialId: Int): EducationalMaterial? {
        return coursesDataSource.getEducationalMaterialById(educationalMaterialId)
    }

    // TEACHER
    suspend fun addStudentToCourse(courseId: Int, studentId: Int) {
        return coursesDataSource.addStudentToCourse(courseId, studentId)
    }

    suspend fun removeStudentFromCourse(courseId: Int, studentId: Int) {
        return coursesDataSource.removeStudentFromCourse(courseId, studentId)
    }

    suspend fun getTeacherCourses(teacherId: Int): List<Course>? {
        return coursesDataSource.getCoursesByTeacherId(teacherId)
    }

    suspend fun createCourse(teacherId: Int, course: Course): Int? {
        return coursesDataSource.postCourse(teacherId, course)
    }

    suspend fun updateCourse(courseId: Int, course: Course) {
        coursesDataSource.updateCourseById(courseId, course)
    }

    suspend fun deleteCourse(courseId: Int) {
        coursesDataSource.deleteCourseById(courseId)
    }

    suspend fun addTaskToCourse(courseId: Int, task: Task): Int? {
        return coursesDataSource.postTaskToCourse(courseId, task)
    }

    suspend fun updateTask(taskId: Int, task: Task) {
        coursesDataSource.updateTaskById(taskId, task)
    }

    suspend fun deleteTask(taskId: Int) {
        coursesDataSource.deleteTaskById(taskId)
    }

    suspend fun addEducationalMaterialToCourse(educationalMaterial: EducationalMaterial): Int? {
        return coursesDataSource.postEducationalMaterialToCourse(educationalMaterial)
    }

    suspend fun updateEducationalMaterial(taskId: Int, educationalMaterial: EducationalMaterial) {
        coursesDataSource.updateEducationalMaterialById(taskId, educationalMaterial)
    }

    suspend fun deleteEducationalMaterial(educationalMaterialId: Int) {
        coursesDataSource.deleteEducationalMaterialById(educationalMaterialId)
    }

    suspend fun getStudentAttemptsByTask(taskId: Int): List<StudentTaskAttempt>? {
        return coursesDataSource.getStudentAttemptsByTaskId(taskId)
    }

    // STUDENT
    suspend fun getStudentCourses(studentId: Int): List<Course>? {
        return coursesDataSource.getCoursesByStudentId(studentId)
    }

    suspend fun getStudentAttemptsByTaskAndStudent(taskId: Int, studentId: Int): List<StudentTaskAttempt>? {
        return coursesDataSource.getStudentAttemptsByTaskAndStudentId(taskId, studentId)
    }

    suspend fun submitStudentAttempt(attempt: StudentTaskAttempt): StudentTaskAttempt? {
        return coursesDataSource.postStudentTaskAttempt(attempt)
    }
}