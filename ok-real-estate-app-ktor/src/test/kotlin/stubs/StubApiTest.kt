package stubs

import auth.addAuth
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import config.KtorAuthConfig
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import module
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
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }

        application { module(authConfig = KtorAuthConfig.TEST) }
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
            addAuth()
            setBody(requestObj)
        }
        val responseObj = response.body<AdCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
    }

    @Test
    fun read() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }

        application { module(authConfig = KtorAuthConfig.TEST) }
        val client = myClient()

        val response = client.post("/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "12345",
                ad = BaseAdIdRequestAd("777"),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(requestObj)
        }
        val responseObj = response.body<AdReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
    }

    @Test
    fun update() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }

        application { module(authConfig = KtorAuthConfig.TEST) }
        val client = myClient()

        val response = client.post("/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "12345",
                ad = AdUpdateObject(
                    id="777",
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
            addAuth()
            setBody(requestObj)
        }
        val responseObj = response.body<AdUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
    }

    @Test
    fun delete() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }

        application { module(authConfig = KtorAuthConfig.TEST) }
        val client = myClient()

        val response = client.post("/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "222",
                ad = BaseAdIdRequestAd(
                    id = "777",
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(requestObj)
        }
        val responseObj = response.body<AdDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("777", responseObj.ad?.id)
    }


}