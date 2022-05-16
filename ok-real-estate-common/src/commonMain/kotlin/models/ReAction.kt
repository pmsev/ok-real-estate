package models

import NONE
import kotlinx.datetime.Instant


data class ReAction (
    var id: ReActionId = ReActionId.NONE,
    var actionType: ReActionType = ReActionType.NONE,
    var details: String = "",
    var actionDt: Instant = Instant.NONE,
    var buyerId: ReUserId = ReUserId.NONE
)