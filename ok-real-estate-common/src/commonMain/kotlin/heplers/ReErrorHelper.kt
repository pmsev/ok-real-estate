package heplers

import ReContext
import models.ReError
import models.ReErrorLevel
import models.ReState

fun Throwable.asReError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = ReError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this
)

fun ReContext.addError(error: ReError) = errors.add(error)
fun ReContext.fail(error: ReError) {
    addError(error)
    state = ReState.FAILING
}

fun errorConcurrency(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: ReErrorLevel = ReErrorLevel.ERROR,
) = ReError(
    field = "lock",
    code = "concurrent-$violationCode",
    group = "concurrency",
    message = "Concurrent object access error: $description",
    level = level,
)

fun errorDevOps(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: ReErrorLevel = ReErrorLevel.ERROR,
) = ReError(
    field = field,
    code = "devops-$violationCode",
    group = "devops",
    message = "Microservice management error: $description",
    level = level,
)

