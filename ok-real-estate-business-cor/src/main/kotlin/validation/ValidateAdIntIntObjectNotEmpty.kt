package validation

import ICorChainDsl
import ReContext
import heplers.errorValidation
import heplers.fail
import models.ReIntObject
import worker

fun  ICorChainDsl<ReContext>.validateAdIntObjectNotEmpty(title: String)  = worker {
    this.title = title
    on {this.adValidating.reIntObject == ReIntObject.NONE }
    handle {
        fail(
            errorValidation(
                field ="reIntObject",
                violationCode = "empty field",
                description = "ad must contain info about real estate object"
            )
        )
    }
}
