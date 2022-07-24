package repo

import ICorChainDsl
import ReContext
import models.ReState
import worker

fun ICorChainDsl<ReContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == ReState.RUNNING }
    handle {
        val request = DbReAdFilterRequest(
            titleFilter = adFilterValidated.searchString,
            descriptionFilter = adFilterValidated.searchString,
            sellerIdFilter = adFilterValidated.sellerId,
            statusFilter = adFilterValidated.status
        )
        val result = adRepo.searchReAd(request)
        val resultAds = result.result
        if (result.isSuccess && resultAds != null) {
            adsRepoDone = resultAds.toMutableList()
        } else {
            state = ReState.FAILING
            errors.addAll(result.errors)
        }
    }
}
