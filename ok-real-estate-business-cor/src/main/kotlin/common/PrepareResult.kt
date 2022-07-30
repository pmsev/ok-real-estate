package common

import ICorChainDsl
import ReContext
import models.ReState
import models.ReWorkMode
import worker

fun ICorChainDsl<ReContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != ReWorkMode.STUB }
    handle {
        adResponse = adRepoDone
        adsResponse = adsRepoDone
        state = when (val st = state) {
            ReState.RUNNING -> ReState.FINISHING
            else -> st
        }
    }
}
