import kotlinx.coroutines.runBlocking
import models.ReAd
import models.ReAdId
import models.ReError
import repo.DbReAdIdRequest
import repo.ReAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals



abstract class RepoAdDeleteTest {
    abstract val repo: ReAdRepository
    protected open val successId = ReAdId(deleteSuccessStub.id.asString())


    @Test
    fun deleteSuccess() {
        val result = runBlocking { repo.deleteReAd(DbReAdIdRequest(id = successId, lock = deleteSuccessStub.lock)) }

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(deleteSuccessStub.copy(id = successId), result.result)
    }

    @Test
    fun deleteNotFound() {
        val result = runBlocking { repo.readReAd(DbReAdIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(ReError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object: BaseInitAds("delete") {
        override val initObjects: List<ReAd> = listOf(
            createInitTestModel("delete")
        )
        private val deleteSuccessStub = initObjects.first()
        val notFoundId = ReAdId("ad-repo-delete-notFound")
    }
}
