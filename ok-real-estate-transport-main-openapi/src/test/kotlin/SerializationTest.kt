import org.junit.Test
import ru.otus.otuskotlin.realestate.api.v1.jacksonMapper
import ru.otus.otuskotlin.realestate.api.v1.models.AdCreateObject
import ru.otus.otuskotlin.realestate.api.v1.models.AdCreateRequest
import ru.otus.otuskotlin.realestate.api.v1.models.ReObject
import kotlin.test.assertContains

class SerializationTest {
    @Test
    fun serTest() {
        val createRequest = AdCreateRequest(
            ad = AdCreateObject(
                title = "Title",
                description = "Description",
                re = ReObject(
                    square = 70.0,
                    price = 15000000,
                    district = ReObject.District.CENTRAL,
                    rooms = 3
                )
            )
        )
        val jsonString = jacksonMapper.writeValueAsString(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"square":70.0""")
    }

}