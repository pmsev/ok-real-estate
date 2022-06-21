package stubs

import ICorChainDsl
import ReContext
import models.ReState
import worker

fun ICorChainDsl<ReContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ReStubs.SUCCESS && state == ReState.RUNNING }
    handle {
        state = ReState.FINISHING
        val stub = ReAdStub.prepareResult {
            adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        adResponse = stub
    }
}
