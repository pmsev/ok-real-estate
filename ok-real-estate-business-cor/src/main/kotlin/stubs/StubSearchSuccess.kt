package stubs

import ICorChainDsl
import ReContext
import models.ReState
import worker

fun ICorChainDsl<ReContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ReStubs.SUCCESS && state == ReState.RUNNING }
    handle {
        state = ReState.FINISHING
        adsResponse.addAll(ReAdStub.prepareSearchList(adFilterRequest.searchString))
    }
}
