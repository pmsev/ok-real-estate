package heplers

import ReContext
import models.ReError
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
