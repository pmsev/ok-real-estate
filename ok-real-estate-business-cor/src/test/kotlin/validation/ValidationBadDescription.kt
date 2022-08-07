package validation

import ReAdApartments.AD_TWO_BEDROOM_APART
import ReAdProcessor
import ReAdRepoStub
import ReContext
import helpers.principalUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*

import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionCorrect(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        principal = principalUser(),
        adRequest = ReAd(
            id = ReAdId("777"),
            title = "some title",
            description = "some description",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ReState.FAILING, ctx.state)
    assertEquals("some description", ctx.adValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionTrim(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        principal = principalUser(),
        adRequest = ReAd(
            id = ReAdId("123"),
            title = "abc",
            description = " \n\tabc \n\t",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ReState.FAILING, ctx.state)
    assertEquals("abc", ctx.adValidated.description)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionEmpty(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        principal = principalUser(),
        adRequest = ReAd(
            id = ReAdId("123"),
            title = "abc",
            description = "",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ReState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionSymbols(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        principal = principalUser(),
        adRepo = ReAdRepoStub(),
        adRequest = ReAd(
            id = ReAdId("123"),
            title = "abc",
            description = "!@#$%^&*(),.{}",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ReState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
