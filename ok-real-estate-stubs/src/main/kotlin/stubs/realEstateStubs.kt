package stubs

import models.ReAd
import models.ReAdId
import models.ReUserId
import models.security.ReAdPermissionClient

object RealEstate {
    private fun stubReady() = ReAd(
        id = ReAdId(id = "777"),
        title = "Квартира в южном районе",
        description = "Лучший вид в городе",
        sellerId = ReUserId(id = "1945"),
        permissionsClient = mutableSetOf(ReAdPermissionClient.READ)
    )

//    private fun stubInProgress() = MkplAd(
//        id = MkplAdId(id = "12345678"),
//        title = "Пока не знаю какой болт",
//        description = "Еще не придумал описание",
//        ownerId = MkplUserId(id = "1990"),
//        visibility = MkplVisibility.VISIBLE_TO_OWNER,
//        adType = MkplDealSide.SUPPLY,
//        permissionsClient = mutableSetOf(MkplAdPermissionClient.MAKE_VISIBLE_OWNER)
//    )

    fun getModel(model: (ReAd.() -> Unit)? = null) = model?.let {
        stubReady().apply(it)
    } ?: stubReady()
}
