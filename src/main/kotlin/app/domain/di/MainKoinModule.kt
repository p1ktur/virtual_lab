package app.domain.di

import app.data.dataSources.*
import app.data.fileManager.*
import app.data.server.*
import app.domain.viewModels.courses.course.*
import app.domain.viewModels.courses.coursesList.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.domain.viewModels.task.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import org.koin.dsl.*

val mainKoinModule = module {
    single {
        HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
            install(ContentNegotiation) {
                ServerJson.get()
            }
            install(DefaultRequest) {
                url(ServerRepository.BASE_URL)
            }
        }
    }

    single { CoursesDataSource(get()) }
    single { ServerRepository(get()) }

    single { FileManager() }

    factory { CoursesListViewModel(get(), get()) }
    factory { CourseViewModel(get(), get()) }
    factory { TaskViewModel(get(), get(), get()) }
    factory { ClassDiagramViewModel(get(), get()) }
}