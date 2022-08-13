import models.*
import org.junit.Test
import kotlin.test.assertEquals

class LogMappingTest {
    @Test
    fun fromContext() {
        val context = ReContext(
            requestId = ReRequestId("777"),
            command = ReCommand.CREATE,
            adResponse = ReAd(
                title = "title",
                description = "desc",
            ),
            errors = mutableListOf(
                ReError(
                    code = "some error",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = ReState.RUNNING,
        )

        val log = context.toLog("test-id")

        assertEquals("test-id", log.logId)
        assertEquals("ok-marketplace", log.source)
        assertEquals("777", log.realestate?.requestId)
        assertEquals("wrong title", log.errors?.firstOrNull()?.message)
    }
}