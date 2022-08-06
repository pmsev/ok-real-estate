package repo

import models.ReAd
import models.ReError

data class DbReAdsResponse(
    override val result: List<ReAd>?,
    override val isSuccess: Boolean,
    override val errors: List<ReError> = emptyList()
) : ReDbResponse<List<ReAd>> {

    companion object {
        fun success(result: List<ReAd>) = DbReAdsResponse(result, true)
        fun error(errors: List<ReError>) = DbReAdsResponse(null, false, errors)
        fun error(error: ReError) = DbReAdsResponse(null, false, listOf(error))
    }
}

