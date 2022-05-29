package stubs

import kotlinx.datetime.Instant
import models.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

object RealEstateObject {
    private fun stubOpened() = ReAd(
        id = ReAdId(id = "777"),
        title = "Евродвушка в лучшем районе. Срочно. Дешево.",
        description = "Евродвушка с евроремонтом в Южном районе Нска.",
        sellerId = ReUserId(id = "888"),
        status = ReStatus.OPENED
    )

    private fun stubReserved() = ReAd(
        id = ReAdId(id = "333"),
        title = "Студия 25 кв.м. Зарезервирована",
        description = "Студия в Северном районе. Один собственник",
        sellerId = ReUserId(id = "222"),
        status = ReStatus.RESERVED,
        actions = mutableListOf(
            ReAction(
                id = ReActionId("1"), buyerId = ReUserId("111"), actionType = ReActionType.MEET,
                actionDt = Instant.fromEpochMilliseconds(LocalDate.now().minusDays(7).toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC))
            )
        )
    )

    fun getModel(model: (ReAd.() -> Unit)? = null) = model?.let {
        stubOpened().apply(it)
    } ?: stubOpened()

    fun getModels() = listOf(
        stubOpened(),
        stubReserved()
    )

    fun ReAd.update(updateableAd: ReAd) = apply {
        title = updateableAd.title
        description = updateableAd.description
        sellerId = updateableAd.sellerId
        status = updateableAd.status
        actions = updateableAd.actions
    }

}