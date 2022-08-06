package repo

import ICorChainDsl
import ReContext
import models.ReState
import worker


fun ICorChainDsl<ReContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == ReState.RUNNING }
    handle {
        val request = DbReAdIdRequest(adValidated)
        val result = adRepo.readReAd(request)
        val resultAd = result.result
        if (result.isSuccess && resultAd != null) {
            adRepoRead = resultAd
        } else {
            state = ReState.FAILING
            errors.addAll(result.errors)
        }
    }
}
