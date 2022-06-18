package stubs

import ICorChainDsl
import ReContext
import models.ReError
import models.ReState
import worker

fun ICorChainDsl<ReContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == ReStubs.BAD_ID && state == ReState.RUNNING }
    handle {
        state = ReState.FAILING
        this.errors.add(
            ReError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
