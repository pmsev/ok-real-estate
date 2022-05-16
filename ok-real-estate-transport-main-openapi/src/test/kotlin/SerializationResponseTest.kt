import org.junit.Test
import ru.otus.otuskotlin.realestate.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.realestate.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.realestate.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationResponseTest {

    private val createResponse = AdCreateResponse(
        ad = AdResponseObject(
            title = "Title",
            description = "Description",
            re = ReObject(
                price = 6000000,
                location = Location(25.5555, 37.77777)
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
        assertContains(jsonString, "\"latitude\":25.5555")
    }

    @Test
    fun deserializeTest() {
        val jsonString = apiV1ResponseSerialize(createResponse)
        val decoded = apiV1ResponseDeserialize<AdCreateResponse>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(6000000, decoded.ad?.re?.price)
        assertEquals(25.5555, decoded.ad?.re?.location?.latitude)
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