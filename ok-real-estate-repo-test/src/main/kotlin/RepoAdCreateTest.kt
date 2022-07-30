import kotlinx.coroutines.runBlocking
import models.ReAd
import models.ReAdId
import models.ReUserId
import kotlin.test.Test
import repo.DbReAdRequest
import repo.ReAdRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RepoAdCreateTest {
    abstract val repo: ReAdRepository

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.createReAd(DbReAdRequest(createObj)) }
        val expected = createObj.copy(id = result.result?.id ?: ReAdId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.result?.title)
        assertEquals(expected.description, result.result?.description)
        assertNotEquals(ReAdId.NONE, result.result?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitAds("search") {

        private val createObj = ReAd(
            title = "create object",
            description = "create object description",
            sellerId = ReUserId("some seller")
        )
        override val initObjects: List<ReAd> = emptyList()
    }
}
