import kotlinx.coroutines.runBlocking
import models.ReAd
import models.ReUserId
import kotlin.test.Test
import repo.DbReAdFilterRequest
import repo.ReAdRepository
import kotlin.test.assertEquals

abstract class RepoAdSearchTest {
    abstract val repo: ReAdRepository
    protected open val initAds: List<ReAd> = initObjects

    @Test
    fun searchSeller() {
        val result = runBlocking { repo.searchReAd(DbReAdFilterRequest(sellerIdFilter = searchSellerId, titleFilter = "title", descriptionFilter = "")) }
        assertEquals(true, result.isSuccess)
        val expected = initAds.filter { it.sellerId == searchSellerId }.sortedBy { it.id.asString() }
        assertEquals(expected, result.result?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }


    companion object : BaseInitAds("search") {

        val searchSellerId = ReUserId("another seller")
        override val initObjects: List<ReAd> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", sellerId = searchSellerId),
            createInitTestModel("ad4", sellerId = searchSellerId),
        )
    }
}
