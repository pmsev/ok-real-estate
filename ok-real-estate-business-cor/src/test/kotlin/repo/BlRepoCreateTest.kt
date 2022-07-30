package repo

import ReAdProcessor
import ReAdRepoInMemory
import ReContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlRepoCreateTest {

    private val command = ReCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = ReAdRepoInMemory(
        randomUuid = { uuid }
    )
    private val settings = ReSettings(
        repoTest = repo
    )
    private val processor = ReAdProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = ReContext(
            command = command,
            state = ReState.NONE,
            workMode = ReWorkMode.TEST,
            adRequest = ReAd(
                id = ReAdId("123"),
                title = "abc",
                description = "abc",
                reIntObject = ReIntObject(square = 35.2, rooms = 1)
            ),
        )
        processor.exec(ctx)
        println(ctx.errors)
        assertEquals(ReState.FINISHING, ctx.state)
        assertNotEquals(ReAdId.NONE, ctx.adResponse.id)
        assertEquals("abc", ctx.adResponse.title)
        assertEquals("abc", ctx.adResponse.description)
        assertEquals(uuid, ctx.adResponse.lock.asString())
    }
}
