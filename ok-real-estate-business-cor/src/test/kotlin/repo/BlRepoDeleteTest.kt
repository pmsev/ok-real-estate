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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BlRepoDeleteTest {

    private val command = ReCommand.DELETE
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidBad = "10000000-0000-0000-0000-000000000003"
    private val initAd = ReAd(
        id = ReAdId("123"),
        title = "abc",
        description = "abc",
        lock = ReAdLock(uuidOld),
    )
    private val repo by lazy {
        ReAdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew })
    }
    private val settings by lazy {
        ReSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { ReAdProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = ReAd(
            id = ReAdId("123"),
            lock = ReAdLock(uuidOld)
        )
        val ctx = ReContext(
            command = command,
            state = ReState.NONE,
            workMode = ReWorkMode.TEST,
            principal = principalUser(),
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        print(ctx.errors)
        assertEquals(ReState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(uuidOld, ctx.adResponse.lock.asString())
    }

    @Test
    fun repoDeleteConcurrentTest() = runTest {
        val adToUpdate = ReAd(
            id = ReAdId("123"),
            lock = ReAdLock(uuidBad),
        )
        val ctx = ReContext(
            command = command,
            state = ReState.NONE,
            workMode = ReWorkMode.TEST,
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(ReState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("lock", ctx.errors.first().field)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(uuidOld, ctx.adResponse.lock.asString())
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(processor, command)
}
