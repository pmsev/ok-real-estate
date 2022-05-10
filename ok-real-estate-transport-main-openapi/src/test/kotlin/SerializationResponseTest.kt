import org.junit.Test
import ru.otus.otuskotlin.realestate.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.realestate.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.realestate.api.v1.models.AdCreateResponse
import ru.otus.otuskotlin.realestate.api.v1.models.AdResponseObject
import ru.otus.otuskotlin.realestate.api.v1.models.ReObject
import ru.otus.otuskotlin.realestate.api.v1.models.Response
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationResponseTest {

    private val createResponse = AdCreateResponse(
        ad = AdResponseObject(
            title = "Title",
            description = "Description",
            re = ReObject(
                price = 6000000
            )
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = apiV1ResponseSerialize(createResponse)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"responseType":"create"""")
        assertContains(jsonString, "\"price\":6000000")
    }

    @Test
    fun deserializeTest() {
        val jsonString = apiV1ResponseSerialize(createResponse)
        val decoded = apiV1ResponseDeserialize<AdCreateResponse>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(6000000, decoded.ad?.re?.price)
    }

    @Test
    fun deserializeResponseTest() {
        val jsonString = apiV1ResponseSerialize(createResponse)
        val decoded = apiV1ResponseDeserialize<Response>(jsonString) as AdCreateResponse
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(6000000, decoded.ad?.re?.price)
    }

}