import app.data.server.*
import app.domain.model.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import org.junit.Test

class UrlTest {
    @Test
    fun `Test how url builds`(): Unit = runBlocking {
        val url = URLBuilder(
            host = "localhost",
            port = 7205,
            pathSegments = listOf("EducationalMaterial"),
//            parameters = Parameters.build {
//                append("courseId", "0")
//                append("teacherId", "0")
//            }
        ).build()

        println(url)

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"ip":"127.0.0.1"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
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
//            .preparePost("https://localhost:8080/") {
//            contentType(ContentType.Application.Json)
//            setBody(Task(name = "1", description = "2", diagramJson = "3"))
//            println(build())
//        }

        val statement: HttpStatement = client.preparePost("http://localhost:8080/customer") {
            contentType(ContentType.Application.Json)
            setBody(Task(3, 0, "Jet", "Brains"))
        }
        println(statement)
    }
}