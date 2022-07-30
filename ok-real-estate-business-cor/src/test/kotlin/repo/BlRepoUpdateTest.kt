package repo

import ReAdProcessor
import ReAdRepoInMemory
import ReContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BlRepoUpdateTest {

    private val command = ReCommand.UPDATE
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidBad = "10000000-0000-0000-0000-000000000003"
    private val initAd = ReAd(
        id = ReAdId("123"),
        title = "abc",
        description = "abc",
        lock = ReAdLock(uuidOld),
    )
    private val repo by lazy { ReAdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew }) }
    private val settings by lazy {
        ReSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { ReAdProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = ReAd(
            id = ReAdId("123"),
            title = "xyz",
            description = "xyz",
            lock = ReAdLock(uuidOld),
            reIntObject = ReIntObject(
                square = 70.0,
                rooms = 2,
                district = ReDistrict.SOUTH
            )
        )
        val ctx = ReContext(
            command = command,
            state = ReState.NONE,
            workMode = ReWorkMode.TEST,
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        print(ctx.errors)
        assertEquals(ReState.FINISHING, ctx.state)
        assertEquals(adToUpdate.id, ctx.adResponse.id)
        assertEquals(adToUpdate.title, ctx.adResponse.title)
        assertEquals(adToUpdate.description, ctx.adResponse.description)
        assertEquals(uuidNew, ctx.adResponse.lock.asString())
    }

    @Test
    fun repoUpdateConcurrentTest() = runTest {
        val adToUpdate = ReAd(
            id = ReAdId("123"),
            title = "xyz",
            description = "xyz",
            lock = ReAdLock(uuidBad),
            reIntObject = ReIntObject(
                square = 70.0,
                rooms = 2,
                district = ReDistrict.SOUTH
            )
        )
        val ctx = ReContext(
            command = command,
            state = ReState.NONE,
            workMode = ReWorkMode.TEST,
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(ReState.FAILING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(uuidOld, ctx.adResponse.lock.asString())
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(processor, command)
}
