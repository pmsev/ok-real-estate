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

class BlRepoSearchTest {

    private val command = ReCommand.SEARCH
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
    fun repoSearchSuccessTest() = runTest {
        val ctx = ReContext(
            command = command,
            state = ReState.NONE,
            workMode = ReWorkMode.TEST,
            principal = principalUser(),
            adFilterRequest = ReAdFilter(
                searchString = "ab",
                description = "zxc"

            ),
        )
        processor.exec(ctx)
        assertEquals(ReState.FINISHING, ctx.state)
        assertEquals(1, ctx.adsResponse.size)
    }
}
