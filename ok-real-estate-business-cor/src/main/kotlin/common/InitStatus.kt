package common

import ICorChainDsl
import ReContext
import models.ReState
import worker

fun ICorChainDsl<ReContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == ReState.NONE }
    handle { state = ReState.RUNNING }
}
