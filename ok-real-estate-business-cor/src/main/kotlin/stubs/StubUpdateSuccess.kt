package stubs

import ICorChainDsl
import ReContext
import models.ReAdId
import models.ReIntObject
import models.ReState
import models.ReStatus
import worker

fun ICorChainDsl<ReContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ReStubs.SUCCESS && state == ReState.RUNNING }
    handle {
        state = ReState.FINISHING
        val stub = ReAdStub.prepareResult {
            adRequest.id.takeIf { it != ReAdId.NONE }?.also { this.id = it }
            adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            adRequest.status.takeIf{it!= ReStatus.NONE}?.also { this.status = it}
            adRequest.reIntObject.takeIf { it!= ReIntObject.NONE }?.also { this.reIntObject = it.deepCopy() }
        }
        adResponse = stub
    }
}
