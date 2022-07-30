package repo

import ReAdProcessor
import ReAdRepoInMemory
import ReContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BlRepoReadTest {

    private val command = ReCommand.READ
    private val initAd = ReAd(
        id = ReAdId("123"),
        title = "abc",
        description = "abc",
    )
    private val repo by lazy { ReAdRepoInMemory(initObjects = listOf(initAd)) }
    private val settings by lazy {
        ReSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { ReAdProcessor(settings) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = ReContext(
            command = command,
            state = ReState.NONE,
            workMode = ReWorkMode.TEST,
            adRequest = ReAd(
                id = ReAdId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(ReState.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(processor, command)
}
