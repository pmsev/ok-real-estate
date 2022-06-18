package validation

import ICorChainDsl
import ReContext
import heplers.errorValidation
import heplers.fail
import models.ReAdId
import worker

fun ICorChainDsl<ReContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { adValidating.id != ReAdId.NONE && ! adValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = adValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only"
        ))
    }
}
