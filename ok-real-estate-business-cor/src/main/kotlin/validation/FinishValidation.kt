package validation

import ICorChainDsl
import ReContext
import models.ReState
import worker

fun ICorChainDsl<ReContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == ReState.RUNNING }
    handle {
        adValidated = adValidating
    }
}

fun ICorChainDsl<ReContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == ReState.RUNNING }
    handle {
        adFilterValidated = adFilterValidating
    }
}

