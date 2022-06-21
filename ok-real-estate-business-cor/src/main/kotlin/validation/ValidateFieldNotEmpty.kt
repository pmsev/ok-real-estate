package validation

import ICorChainDsl
import ReContext
import heplers.errorValidation
import heplers.fail
import heplers.getField
import heplers.setField
import worker

fun ICorChainDsl<ReContext>.validateFieldNotEmpty(title: String, fieldName: String) = worker {
    this.title = title
    on { adValidating.getField<String>(fieldName)?.isEmpty() ?: error("Wrong field name") }
    handle {
        fail(errorValidation(
            field = fieldName,
            violationCode = "empty",
            description = "$fieldName must not be empty"
        ))
    }
}
