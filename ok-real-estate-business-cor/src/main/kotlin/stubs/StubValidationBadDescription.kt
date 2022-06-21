package stubs

import ICorChainDsl
import ReContext
import models.ReError
import models.ReErrorLevel
import models.ReState
import worker

fun ICorChainDsl<ReContext>.stubValidationBadDescription(title:  String) = worker {
    this.title = title
    on { stubCase == ReStubs.BAD_DESCRIPTION && state == ReState.RUNNING }
    handle {
        state = ReState.FAILING
        this.errors.add(
            ReError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field",
                level = ReErrorLevel.ERROR
            )
        )
    }

}
