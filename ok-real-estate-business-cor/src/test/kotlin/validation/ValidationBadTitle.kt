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
fun validationTitleCorrect(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        principal = principalUser(),
        adRepo = ReAdRepoStub(),
        adRequest = ReAd(
            id = ReAdId("777"),
            title = "some title",
            description = "some description",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    println(ctx.errors.toString())
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ReState.FAILING, ctx.state)
    assertEquals("some title", ctx.adValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        adRepo = ReAdRepoStub(),
        principal = principalUser(),
        adRequest = ReAd(
            id = ReAdId("777"),
            title = " \n\t title \t\n ",
            description = "description",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ReState.FAILING, ctx.state)
    assertEquals("title", ctx.adValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        adRepo = ReAdRepoStub(),
        principal = principalUser(),
        adRequest = ReAd(
            id = ReAdId("777"),
            title = "",
            description = "description",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ReState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: ReCommand, processor: ReAdProcessor) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        adRepo = ReAdRepoStub(),
        adRequest = ReAd(
            id = ReAdId("777"),
            title = "!@#$%^&*(),.{}",
            description = "description",
            reIntObject = AD_TWO_BEDROOM_APART.reIntObject.deepCopy()
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ReState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
