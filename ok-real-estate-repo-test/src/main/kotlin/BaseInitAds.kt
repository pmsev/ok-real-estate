import models.ReAd
import models.ReAdId
import models.ReAdLock
import models.ReUserId

abstract class BaseInitAds(val op: String): IInitObjects<ReAd> {

    fun createInitTestModel(
        suf: String,
        sellerId: ReUserId = ReUserId("some seller")

    ) = ReAd(
        id = ReAdId("re-ad-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        sellerId = sellerId,
        lock = ReAdLock("20000000-0000-0000-0000-000000000000")
    )
}
