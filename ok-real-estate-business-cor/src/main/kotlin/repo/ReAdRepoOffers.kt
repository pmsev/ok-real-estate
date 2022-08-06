package repo

import ICorChainDsl
import ReContext
import models.ReError
import models.ReState
import worker


fun ICorChainDsl<ReContext>.repoOffers(title: String) = worker {
    this.title = title
    description = "Поиск предложений для объявления по названию"
    on { state == ReState.RUNNING }
    handle {
        val adRequest = adRepoPrepare
        val filter = DbReAdFilterRequest(
            titleFilter = adRequest.title,
            descriptionFilter = adRequest.description,
            sellerIdFilter = adRequest.sellerId
        )
        val dbResponse = if (filter.titleFilter.isEmpty() && filter.descriptionFilter.isEmpty()) {
            DbReAdsResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    ReError(
                        field = "title|description",
                        message = "Title and description should not be both empty"
                    )
                )
            )
        } else {
            adRepo.searchReAd(filter)
        }


        val resultAds = dbResponse.result
        when {
            !resultAds.isNullOrEmpty() -> adsRepoDone = resultAds.toMutableList()
            dbResponse.isSuccess -> return@handle
            else -> {
                state = ReState.FAILING
                errors.addAll(dbResponse.errors)
            }
        }
    }
}
