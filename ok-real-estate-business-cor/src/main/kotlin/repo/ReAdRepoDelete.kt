package common

import ICorChainDsl
import ReContext
import models.ReState
import repo.DbReAdIdRequest
import worker


fun ICorChainDsl<ReContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == ReState.RUNNING }
    handle {
        val request = DbReAdIdRequest(adRepoPrepare)
        val result = adRepo.deleteReAd(request)
        if (!result.isSuccess) {
            state = ReState.FAILING
            errors.addAll(result.errors)
        }
        adRepoDone = adRepoRead
    }
}
