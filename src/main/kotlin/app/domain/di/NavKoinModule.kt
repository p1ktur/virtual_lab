package app.domain.di

import app.domain.umlDiagram.comparing.*
import app.domain.viewModels.courses.course.*
import app.domain.viewModels.courses.coursesList.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.domain.viewModels.educationalMaterial.*
import app.domain.viewModels.studentsList.*
import app.domain.viewModels.task.*
import org.koin.dsl.*

val navKoinModule = module {
    single { UMLDiagramComparer() }

    factory { CoursesListViewModel(get()) }
    factory { (courseId: Int?) ->
        CourseViewModel(courseId, get())
    }
    factory { (courseId: Int, taskId: Int?) ->
        TaskViewModel(courseId, taskId, get(), get())
    }
    factory { (taskId: Int?) ->
        ClassDiagramViewModel(taskId, get())
    }
    factory { (courseId: Int, educationalMaterialId: Int?) ->
        EducationalMaterialViewModel(courseId, educationalMaterialId, get())
    }
    factory { (courseId: Int?) ->
        StudentsListViewModel(courseId, get())
    }
}