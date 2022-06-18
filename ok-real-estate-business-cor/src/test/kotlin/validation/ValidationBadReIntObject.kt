package validation

import ReAdApartments.AD_TWO_BEDROOM_APART
import ReAdProcessor
import ReContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationEmptyObject(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        adRequest = ReAd(
            id = ReAdId("123"),
            title = "abc",
            description = "some description"
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ReState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("reIntObject", error?.field)
    assertContains(error?.message ?: "", "real estate object")

}


fun validationNonEmptyObject(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        adRequest = ReAd(
            id = ReAdId("123"),
            title = "abc",
            description = "some description",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ReState.FAILING, ctx.state)

}
