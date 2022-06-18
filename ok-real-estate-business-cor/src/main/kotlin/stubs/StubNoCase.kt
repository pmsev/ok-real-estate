package stubs

import ICorChainDsl
import ReContext
import models.ReError
import models.ReState
import worker

fun ICorChainDsl<ReContext>.stubNoCase(title: String)  = worker {
    this.title = title
    on { state == ReState.RUNNING }
    handle {
        state = ReState.FAILING
        this.errors.add(
            ReError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}