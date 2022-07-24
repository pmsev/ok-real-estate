package repo

import ICorChainDsl
import ReContext
import models.ReState
import worker


fun ICorChainDsl<ReContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == ReState.RUNNING }
    handle {
        val request = DbReAdRequest(
            adRepoPrepare.deepCopy().apply {
                this.title = adValidated.title
                description = adValidated.description
            }
        )
        val result = adRepo.updateReAd(request)
        val resultAd = result.result
        if (result.isSuccess && resultAd != null) {
            adRepoDone = resultAd
        } else {
            state = ReState.FAILING
            errors.addAll(result.errors)
            adRepoDone
        }
    }
}
