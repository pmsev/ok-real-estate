package repo

import models.ReAd
import models.ReError

data class DbReAdResponse(
    override val result: ReAd?,
    override val isSuccess: Boolean,
    override val errors: List<ReError> = emptyList()
) : ReDbResponse<ReAd> {

    companion object {
        fun success(result: ReAd) = DbReAdResponse(result, true)
        fun error(errors: List<ReError>) = DbReAdResponse(null, false, errors)
        fun error(error: ReError) = DbReAdResponse(null, false, listOf(error))
    }
}
