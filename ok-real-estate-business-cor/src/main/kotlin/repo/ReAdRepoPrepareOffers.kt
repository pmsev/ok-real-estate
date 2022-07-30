package repo

import ICorChainDsl
import ReContext
import models.ReState
import worker


fun ICorChainDsl<ReContext>.repoPrepareOffers(title: String) = worker {
    this.title = title
    description = "Подготовка данных к поиску предложений в БД"
    on { state == ReState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.deepCopy()
        adRepoDone = adRepoRead.deepCopy()
    }
}
