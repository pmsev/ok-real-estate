import ReAdApartments.AD_TWO_BEDROOM_APART
import models.ReAd
import models.ReAdId
import stubs.RealEstateObject.getModel

object ReAdStub {
    fun prepareResult(block: ReAd.() -> Unit): ReAd = getModel().apply(block)
    fun prepareSearchList(searchString: String) = listOf(
        reSomeAd("111","Южн"),
        reSomeAd("777","дешев"),
        reSomeAd("7","ремонт"),
        reSomeAd("555","двушка")
    )

    private fun reSomeAd(id: String, filter: String) =
        reAd(AD_TWO_BEDROOM_APART, id = id, filter = filter)

    private fun reAd(base: ReAd, id: String, filter: String) = base.copy(
        id = ReAdId(id),
        title = "$filter $id",
        description = "desc $filter $id"
    )

}