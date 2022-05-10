package models

data class ReAd (
    var id: ReAdId = ReAdId.NONE,
    var title: String = "",
    var description: String = "",
    var sellerId: ReUserId = ReUserId.NONE,
    var reIntObject: ReIntObject = ReIntObject.NONE,
    var status: ReStatus = ReStatus.NONE,
    var actions: MutableList<ReAction> = mutableListOf()
)