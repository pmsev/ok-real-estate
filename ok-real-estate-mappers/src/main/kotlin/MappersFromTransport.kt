import exceptions.UnknownRequestClass
import models.*
import ru.otus.otuskotlin.realestate.api.v1.models.*
import stubs.ReStubs

fun ReContext.fromTransport(request: Request) = when (request) {
    is AdCreateRequest -> fromTransport(request)
    is AdUpdateRequest -> fromTransport(request)
    is AdReadRequest -> fromTransport(request)
    is AdDeleteRequest -> fromTransport(request)
    is AdSearchRequest -> fromTransport(request)
    is AdOffersRequest -> fromTransport(request)
    is AdActionRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

private fun String?.toAdId() = this?.let { ReAdId(it) } ?: ReAdId.NONE
private fun String?.toUserId() = this?.let { ReUserId(it) } ?: ReUserId.NONE
private fun String?.toReObjectOd() = this?.let { ReObjectId(it) } ?: ReObjectId.NONE
private fun BaseAdIdRequestAd?.toAdWithId() = ReAd(id = this?.id.toAdId())

private fun Request?.requestId() = this?.requestId?.let { ReRequestId(it) } ?: ReRequestId.NONE
private fun AdCreateObject.toInternal(): ReAd = ReAd(
    title = this.title ?: "",
    description = this.description ?: "",
    sellerId = this.sellerId.toUserId(),
    reIntObject = this.re?.fromTransport() ?: ReIntObject()
)

private fun AdUpdateObject.toAdWithId(): ReAd = ReAd(
    id = this.id.toAdId(),
    title = this.title ?: "",
    description = this.description ?: "",
    sellerId = this.sellerId.toUserId(),
    reIntObject = this.re?.fromTransport() ?: ReIntObject()
)

private fun ReObject.District?.fromTransport() = when (this) {
    ReObject.District.CENTRAL -> ReDistrict.CENTRAL
    ReObject.District.EAST -> ReDistrict.EAST
    ReObject.District.NORTH -> ReDistrict.NORTH
    ReObject.District.SOUTH -> ReDistrict.SOUTH
    ReObject.District.WEST -> ReDistrict.WEST
    null -> ReDistrict.NONE
}

private fun AdDebug?.transportToWorkMode(): ReWorkMode = when (this?.mode) {
    AdRequestDebugMode.PROD -> ReWorkMode.PROD
    AdRequestDebugMode.TEST -> ReWorkMode.TEST
    AdRequestDebugMode.STUB -> ReWorkMode.STUB
    null -> ReWorkMode.PROD
}

private fun AdDebug?.transportToStubCase(): ReStubs = when (this?.stub) {
    AdRequestDebugStubs.SUCCESS -> ReStubs.SUCCESS
    AdRequestDebugStubs.NOT_FOUND -> ReStubs.NOT_FOUND
    AdRequestDebugStubs.BAD_ID -> ReStubs.BAD_ID
    AdRequestDebugStubs.BAD_TITLE -> ReStubs.BAD_TITLE
    AdRequestDebugStubs.BAD_DESCRIPTION -> ReStubs.BAD_DESCRIPTION
    AdRequestDebugStubs.CANNOT_DELETE -> ReStubs.CANNOT_DELETE
    AdRequestDebugStubs.BAD_SEARCH_STRING -> ReStubs.BAD_SEARCH_STRING
    null -> ReStubs.NONE
}

private fun AdAction?.toInternal(): ReActionType = when (this) {
    AdAction.MEET -> ReActionType.MEET
    AdAction.CONFERENCE -> ReActionType.CONFERENCE
    AdAction.MESSAGE -> ReActionType.MESSAGE
    AdAction.SIGN -> ReActionType.SIGN
    null -> ReActionType.NONE
}

private fun ReObject.fromTransport() = ReIntObject(
    id = this.id?.toReObjectOd() ?: ReObjectId.NONE,
    square = this.square ?: 0.0,
    price = this.price ?: 0,
    district = this.district.fromTransport(),
    rooms = this.rooms ?: 0
)

private fun AdSearchFilter?.toInternal(): ReAdFilter = ReAdFilter(
    searchString = this?.searchString ?: ""
)


fun ReContext.fromTransport(request: AdCreateRequest) {
    command = ReCommand.CREATE
    requestId = request.requestId()
    adRequest = request.ad?.toInternal() ?: ReAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ReContext.fromTransport(request: AdUpdateRequest) {
    command = ReCommand.UPDATE
    requestId = request.requestId()
    adRequest = request.ad?.toAdWithId() ?: ReAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ReContext.fromTransport(request: AdReadRequest) {
    command = ReCommand.READ
    requestId = request.requestId()
    adRequest = request.ad?.toAdWithId() ?: ReAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ReContext.fromTransport(request: AdDeleteRequest) {
    command = ReCommand.DELETE
    requestId = request.requestId()
    adRequest = request.ad?.toAdWithId() ?: ReAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ReContext.fromTransport(request: AdOffersRequest) {
    command = ReCommand.OFFERS
    requestId = request.requestId()
    adRequest = request.ad?.toAdWithId() ?: ReAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ReContext.fromTransport(request: AdActionRequest) {
    command = ReCommand.ACTION
    requestId = request.requestId()
    actionRequest = ReAction(
        actionType = request.actionType.toInternal(),
        details = request.details ?: "",
        buyerId = request.buyerId.toUserId()
    )
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ReContext.fromTransport(request: AdSearchRequest) {
    command = ReCommand.SEARCH
    requestId = request.requestId()
    adFilterRequest = request.adFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}
