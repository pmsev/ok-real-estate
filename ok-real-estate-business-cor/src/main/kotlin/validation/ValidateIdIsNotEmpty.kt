package validation

import ICorChainDsl
import ReContext
import heplers.errorValidation
import heplers.fail
import models.ReAdId
import worker

fun  ICorChainDsl<ReContext>.validateIdIsNotEmpty(title: String)  = worker {
    this.title = title
    on {this.adValidating.id == ReAdId.NONE}
    handle {
        fail(
            errorValidation(
                field ="id",
                violationCode = "empty field",
                description = "real estate id must not be empty"
            )
        )
    }
}
