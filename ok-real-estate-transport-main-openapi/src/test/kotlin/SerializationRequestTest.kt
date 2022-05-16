import org.junit.Test
import ru.otus.otuskotlin.realestate.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.realestate.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.realestate.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationRequestTest {

    private val createRequest = AdCreateRequest(
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
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = apiV1RequestSerialize(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"requestType":"create"""")
    }

    @Test
    fun deserializeTest() {
        val jsonString = apiV1RequestSerialize(createRequest)
        val decoded = apiV1RequestDeserialize<AdCreateRequest>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(ReObject.District.CENTRAL, decoded.ad?.re?.district)
        assertEquals(25.5555, decoded.ad?.re?.location?.latitude)
    }
}
