package app.domain.di

import app.data.dataSources.*
import app.data.fileManager.*
import app.data.server.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.dsl.*

val ioKoinModule = module {
    single {
        HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(ServerJson.get())
            }
            install(DefaultRequest) {
                url(ServerRepository.BASE_URL)
            }
        }
    }

    single { CoursesDataSource(get()) }
    single { ServerRepository(get()) }

    single { FileManager() }
}