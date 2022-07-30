import kotlinx.coroutines.runBlocking
import models.*
import kotlin.test.Test
import repo.DbReAdRequest
import repo.ReAdRepository
import kotlin.test.assertEquals

abstract class RepoAdUpdateTest {
    abstract val repo: ReAdRepository
    protected open val updateId = initObjects.first().id
    protected open val newLock = ReAdLock("20000000-0000-0000-0000-000000000001")
    protected open val updateObj = ReAd(
        id = updateId,
        title = "update object",
        description = "update object description",
        sellerId = ReUserId("some seller"),
        lock = initObjects.first().lock,
    )


    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.updateReAd(DbReAdRequest(updateObj)) }
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.id, result.result?.id)
        assertEquals(updateObj.title, result.result?.title)
        assertEquals(updateObj.description, result.result?.description)
        assertEquals(newLock, result.result?.lock)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.updateReAd(DbReAdRequest(updateObjNotFound)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(listOf(ReError(field = "id", message = "Not Found")), result.errors)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<ReAd> = listOf(
            createInitTestModel("update")
        )
        private val updateIdNotFound = ReAdId("ad-repo-update-not-found")

        private val updateObjNotFound = ReAd(
            id = updateIdNotFound,
            title = "update object not found",
            description = "update object not found description",
            sellerId = ReUserId("some seller"),
        )
    }
}
