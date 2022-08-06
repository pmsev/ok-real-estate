package repo

import ICorChainDsl
import ReContext
import models.ReState
import models.ReUserId
import worker


fun ICorChainDsl<ReContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == ReState.RUNNING }
    handle {
        adRepoPrepare = adValidated.deepCopy().apply {
            sellerId = ReUserId("some-seller-id")
        }

    }
}
