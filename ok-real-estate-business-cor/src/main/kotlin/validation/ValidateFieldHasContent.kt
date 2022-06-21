package validation

import ICorChainDsl
import ReContext
import heplers.errorValidation
import heplers.fail
import heplers.getField
import worker

fun ICorChainDsl<ReContext>.validateFieldHasContent(title: String, fieldName: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")

    on {
        val fieldValue = adValidating.getField<String>(fieldName)   ?: error("$fieldName - Wrong field name")
        fieldValue.isNotEmpty() && ! fieldValue.contains(regExp) }
    handle {
        fail(errorValidation(
            field =fieldName,
            violationCode = "noContent",
            description = "$fieldName must contain letters"
        ))
    }
}