package app.domain.di

import app.data.dataSources.*
import app.data.fileManager.*
import app.data.server.*
import app.domain.viewModels.courses.course.*
import app.domain.viewModels.courses.coursesList.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.domain.viewModels.task.*
import org.koin.dsl.*

val mainKoinModule = module {
    single { CoursesDataSource() }
    single { ServerRepository(get()) }

    single { FileManager() }

    factory { CoursesListViewModel() }
    factory { CourseViewModel() }
    factory { TaskViewModel() }
    factory { ClassDiagramViewModel() }
}