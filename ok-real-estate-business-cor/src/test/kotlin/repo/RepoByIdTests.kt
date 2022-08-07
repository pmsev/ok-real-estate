package repo

import ReAdProcessor
import ReAdRepoInMemory
import ReContext
import helpers.principalUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals

private val initAd = ReAd(
    id = ReAdId("123"),
    title = "abc",
    description = "abc",
)
private val uuid = "10000000-0000-0000-0000-000000000001"
private val repo: ReAdRepository
    get() = ReAdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuid })


@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(processor: ReAdProcessor, command: ReCommand) = runTest {
    val ctx = ReContext(
        command = command,
        state = ReState.NONE,
        workMode = ReWorkMode.TEST,
        adRepo = repo,
        principal = principalUser(),
        adRequest = ReAd(
            id = ReAdId("12345"),
            title = "xyz",
            description = "xyz",
            lock = ReAdLock(uuid),
            reIntObject =  ReIntObject(
                square = 70.0,
                rooms = 2,
                district = ReDistrict.SOUTH
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(ReState.FAILING, ctx.state)
    assertEquals(ReAd(), ctx.adResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
