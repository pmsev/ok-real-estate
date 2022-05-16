import models.*
import models.location.Degree
import models.location.ReoLocation
import ru.otus.otuskotlin.realestate.api.v1.models.*

fun ReContext.toTransportAd(): Response = when (val cmd = command) {
    ReCommand.CREATE -> toTransportCreate()
    ReCommand.READ -> toTransportRead()
    ReCommand.UPDATE -> toTransportUpdate()
    ReCommand.DELETE -> toTransportDelete()
    ReCommand.SEARCH -> toTransportSearch()
    ReCommand.OFFERS -> toTransportOffers()
    ReCommand.ACTION -> toTransportAction()
    ReCommand.NONE -> throw UnknownReCommand(cmd)
}


private fun ReAd.toTransportAd(): AdResponseObject = AdResponseObject(
    id = id.takeIf { it != ReAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    sellerId = sellerId.takeIf { it != ReUserId.NONE }?.asString(),
    re = reIntObject.toTransport(),
    actions = actions.toTransport()
)

private fun MutableList<ReAction>.toTransport(): Set<AdAction>? = this
    .filter { it.actionType != ReActionType.NONE }
    .map { it.toTransportAd()!! }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun ReAction.toTransportAd(): AdAction? = when (this.actionType) {
    ReActionType.SIGN -> AdAction.SIGN
    ReActionType.MESSAGE -> AdAction.MESSAGE
    ReActionType.MEET -> AdAction.MEET
    ReActionType.CONFERENCE -> AdAction.CONFERENCE
    else -> null
}

private fun ReIntObject.toTransport(): ReObject = ReObject(
    id = id.takeIf { it != ReObjectId.NONE }?.asString(),
    square = square,
    price = price,
    district = district.toTransport(),
    rooms = rooms,
    address = address,
    location = location.toTransport()
    )

private fun ReoLocation.toTransport(): Location = Location(
    latitude = latitude.takeIf { it != Degree.NONE }?.asDouble(),
    longitude = longitude.takeIf { it != Degree.NONE }?.asDouble()
)

private fun ReDistrict.toTransport(): ReObject.District? = when (this) {
    ReDistrict.NONE -> null
    ReDistrict.WEST -> ReObject.District.WEST
    ReDistrict.SOUTH -> ReObject.District.SOUTH
    ReDistrict.NORTH -> ReObject.District.NORTH
    ReDistrict.EAST -> ReObject.District.EAST
    ReDistrict.CENTRAL -> ReObject.District.CENTRAL
}


fun ReContext.toTransportCreate(): Response = AdCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ReState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

private fun ReContext.toTransportRead(): Response = AdReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ReState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

private fun ReContext.toTransportUpdate(): Response = AdUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ReState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

private fun ReContext.toTransportDelete(): Response = AdDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ReState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun ReContext.toTransportSearch() = AdSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ReState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ads = adsResponse.toTransportAd()
)

fun ReContext.toTransportOffers() = AdOffersResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ReState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    offers = adsResponse.toTransportAd()
)

fun ReContext.toTransportAction() = AdActionResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ReState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors()
)

fun List<ReAd>.toTransportAd(): List<AdResponseObject>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun List<ReError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ReError.toTransportAd() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

