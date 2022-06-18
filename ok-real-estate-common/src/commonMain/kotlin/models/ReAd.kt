package models

/**
 * Real Estate Advertisement - объявление о продаже недвижимости
 */
data class ReAd (
    var id: ReAdId = ReAdId.NONE,
    var title: String = "",
    var description: String = "",
    var sellerId: ReUserId = ReUserId.NONE,
    var reIntObject: ReIntObject = ReIntObject.NONE,
    var status: ReStatus = ReStatus.NONE,
    var actions: MutableList<ReAction> = mutableListOf()
){
    fun deepCopy(
    ) = ReAd(
        id = this@ReAd.id,
        title = this@ReAd.title,
        description = this@ReAd.description,
        sellerId = this@ReAd.sellerId,
        status = this@ReAd.status,
        reIntObject = this@ReAd.reIntObject.deepCopy(),
        actions = this@ReAd.actions.toMutableList()
    )
}