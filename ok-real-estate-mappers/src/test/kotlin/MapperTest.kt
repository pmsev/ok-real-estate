import models.*
import org.junit.Test
import ru.otus.otuskotlin.realestate.api.v1.models.*
import stubs.ReStubs
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromTransport() {
        val req = AdCreateRequest(
            requestId = "1234",
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS,
            ),
            ad = AdCreateObject(
                title = "title",
                description = "desc",
                re = ReObject(
                    square = 35.2,
                    price = 6000000
                )
            ),
        )

        val context = ReContext()
        context.fromTransport(req)

        assertEquals(ReStubs.SUCCESS, context.stubCase)
        assertEquals(ReWorkMode.STUB, context.workMode)
        assertEquals("title", context.adRequest.title)
        assertEquals(35.2, context.adRequest.reIntObject.square)
        assertEquals(6000000, context.adRequest.reIntObject.price)
    }

    @Test
    fun toTransport() {
        val context = ReContext(
            requestId = ReRequestId("1234"),
            command = ReCommand.CREATE,
            adResponse = ReAd(
                title = "title",
                description = "desc",
                reIntObject = ReIntObject(
                    square = 35.2,
                    price = 6000000
                )
            ),
            errors = mutableListOf(
                ReError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = ReState.RUNNING,
        )

        val req = context.toTransportAd() as AdCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("title", req.ad?.title)
        assertEquals("desc", req.ad?.description)
        assertEquals(35.2, req.ad?.re?.square)
        assertEquals(6000000, req.ad?.re?.price)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}