import kotlinx.coroutines.runBlocking
import models.ReAd
import models.ReAdId
import models.ReError
import repo.DbReAdIdRequest
import repo.ReAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals


abstract class RepoAdReadTest {
    abstract val repo: ReAdRepository
    protected open val successId = Companion.successId

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.readReAd(DbReAdIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub.copy(successId), result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.readReAd(DbReAdIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(ReError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object: BaseInitAds("read") {
        override val initObjects: List<ReAd> = listOf(
            createInitTestModel("read")
        )
        private val readSuccessStub = initObjects.first()

        private val successId = ReAdId(readSuccessStub.id.asString())
        val notFoundId = ReAdId("ad-repo-read-notFound")

    }
}
