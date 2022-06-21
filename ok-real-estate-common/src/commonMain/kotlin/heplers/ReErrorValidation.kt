package heplers

import models.ReError
import models.ReErrorLevel

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: ReErrorLevel = ReErrorLevel.ERROR,
) = ReError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)
