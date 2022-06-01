package stubs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.realestate.api.v1.models.*
import kotlin.test.assertEquals

class StubApiTest {
    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    @Test
    fun create() = testApplication {
        val client = myClient()

        val response = client.post("/ad/create") {
            val requestObj = AdCreateRequest(
                ad = AdCreateObject(
                    title = "Title",
                    description = "Description",
                    re = ReObject(
                        square = 70.0,
                        price = 15000000,
                        district = ReObject.District.CENTRAL,
                        rooms = 3,
                        address = "Address",
                        location = Location(25.5555, 37.7777)
                    )
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
    }

}