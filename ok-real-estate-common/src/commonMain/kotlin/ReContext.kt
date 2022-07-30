import kotlinx.datetime.Instant
import models.*
import repo.ReAdRepository
import stubs.ReStubs

data class ReContext(
    var settings: ReSettings = ReSettings(),
    var command: ReCommand = ReCommand.NONE,
    var state: ReState = ReState.NONE,
    val errors: MutableList<ReError> = mutableListOf(),

    var workMode: ReWorkMode = ReWorkMode.PROD,
    var stubCase: ReStubs = ReStubs.NONE,

    var requestId: ReRequestId = ReRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: ReAd = ReAd(),
    var adFilterRequest: ReAdFilter = ReAdFilter(),
    var actionRequest: ReAction = ReAction(),

    var adRepo: ReAdRepository = ReAdRepository.NONE,

    var adRepoRead: ReAd = ReAd(),
    var adRepoPrepare: ReAd = ReAd(),
    var adRepoDone: ReAd = ReAd(),
    var adsRepoDone: MutableList<ReAd> = mutableListOf(),

    var adResponse: ReAd = ReAd(),
    var adsResponse: MutableList<ReAd> = mutableListOf(),

    var adValidating: ReAd = ReAd(),
    var adValidated: ReAd = ReAd(),

    var adFilterValidating: ReAdFilter = ReAdFilter(),
    var adFilterValidated: ReAdFilter = ReAdFilter()



)
