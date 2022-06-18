package stubs

import ICorChainDsl
import ReContext
import models.ReIntObject
import models.ReState
import models.ReStatus
import worker

fun ICorChainDsl<ReContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ReStubs.SUCCESS && state == ReState.RUNNING }
    handle {
        state = ReState.FINISHING
        val stub = ReAdStub.prepareResult {
            adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            adRequest.status.takeIf{it!= ReStatus.NONE}?.also { this.status = it}
            adRequest.reIntObject.takeIf { it!= ReIntObject.NONE }?.also { this.reIntObject = it }

        }
        adResponse = stub
    }
}
