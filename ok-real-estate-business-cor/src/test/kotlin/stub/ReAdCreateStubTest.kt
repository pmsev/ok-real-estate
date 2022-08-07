package ru.otus.otuskotlin.marketplace.biz.stub.stub

import ReAdApartments.AD_TWO_BEDROOM_APART
import ReAdProcessor
import ReContext
import helpers.principalUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import stubs.ReStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ReAdCreateStubTest {

    private val processor = ReAdProcessor()
    private val id = ReAdId("777")
    private val title = "real estate title"
    private val description = "some description"


    @Test
    fun create() = runTest {

        val ctx = ReContext(
            command = ReCommand.CREATE,
            state = ReState.NONE,
            workMode = ReWorkMode.STUB,
            stubCase = ReStubs.SUCCESS,
            principal = principalUser(),
            adRequest = ReAd(
                id = id,
                title = title,
                description = description
            ),
        )
        processor.exec(ctx)
        assertEquals(AD_TWO_BEDROOM_APART.id, ctx.adResponse.id)
        assertEquals(title, ctx.adResponse.title)
        assertEquals(description, ctx.adResponse.description)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = ReContext(
            command = ReCommand.CREATE,
            state = ReState.NONE,
            workMode = ReWorkMode.STUB,
            stubCase = ReStubs.BAD_TITLE,
            principal = principalUser(),
            adRequest = ReAd(
                id = id,
                title = "",
                description = description
            ),
        )
        processor.exec(ctx)
        assertEquals(ReAd(), ctx.adResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = ReContext(
            command = ReCommand.CREATE,
            state = ReState.NONE,
            workMode = ReWorkMode.STUB,
            stubCase = ReStubs.BAD_DESCRIPTION,
            principal = principalUser(),
            adRequest = ReAd(
                id = id,
                title = title,
                description = ""
            ),
        )
        processor.exec(ctx)
        assertEquals(ReAd(), ctx.adResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = ReContext(
            command = ReCommand.CREATE,
            state = ReState.NONE,
            workMode = ReWorkMode.STUB,
            stubCase = ReStubs.DB_ERROR,
            principal = principalUser(),
            adRequest = ReAd(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(ReAd(), ctx.adResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = ReContext(
            command = ReCommand.CREATE,
            state = ReState.NONE,
            workMode = ReWorkMode.STUB,
            stubCase = ReStubs.BAD_ID,
            principal = principalUser(),
            adRequest = ReAd(
                id = id,
                title = title,
                description = description
            ),
        )
        processor.exec(ctx)
        assertEquals(ReAd(), ctx.adResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
