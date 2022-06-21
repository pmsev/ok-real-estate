package services

import ReContext
import models.ReError
import models.ReState

fun ReContext.errorResponse(buildError: () -> ReError, error: (ReError) -> ReError) = apply {
    state = ReState.FINISHING
    errors.add(error(buildError()))
}

fun ReContext.successResponse(context: ReContext.() -> Unit) = apply(context)
    .apply { state = ReState.RUNNING }

val notFoundError: (String) -> String = { "Not found ad by id $it" }
