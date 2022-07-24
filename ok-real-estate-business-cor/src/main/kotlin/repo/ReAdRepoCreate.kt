package repo

import ICorChainDsl
import ReContext
import models.ReState
import worker


fun ICorChainDsl<ReContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == ReState.RUNNING }
    handle {
        val request = DbReAdRequest(adRepoPrepare)
        val result = adRepo.createReAd(request)
        val resultAd = result.result
        if (result.isSuccess && resultAd != null) {
            adRepoDone = resultAd
        } else {
            state = ReState.FAILING
            errors.addAll(result.errors)
        }
    }
}
