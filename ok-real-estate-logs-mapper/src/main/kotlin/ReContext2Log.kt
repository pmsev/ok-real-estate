import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock
import models.*
import ru.otus.otuskotlin.realestate.api.logs.models.*

fun ReContext.toLog(logId: String) = CommonLogModel(
    messageId = uuid4().toString(),
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-marketplace",
    realestate = toReLog(),
    errors = errors.map { it.toLog() },
)

fun ReContext.toReLog(): ReLogModel? {
    val adNone = ReAd()
    return ReLogModel(
        requestId = requestId.takeIf { it != ReRequestId.NONE }?.asString(),
        requestAd = adRequest.takeIf { it != adNone }?.toLog(),
        responseAd = adResponse.takeIf { it != adNone }?.toLog(),
        responseAds = adsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = adFilterRequest.takeIf { it != ReAdFilter() }?.toLog(),
    ).takeIf { it != ReLogModel() }
}

private fun ReAdFilter.toLog() = ReAdFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    sellerId = sellerId.takeIf { it != ReUserId.NONE }?.asString(),
    searchTypes = searchTypes.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)

fun ReError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.takeIf { it != null }?.name,
)

fun ReAd.toLog() = ReAdLog(
    id = id.takeIf { it != ReAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    sellerId = sellerId.takeIf { it != ReUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
