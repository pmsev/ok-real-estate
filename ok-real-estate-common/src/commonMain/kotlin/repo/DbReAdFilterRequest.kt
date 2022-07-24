package repo

import models.ReStatus
import models.ReUserId

data class DbReAdFilterRequest(
    val titleFilter: String,
    val descriptionFilter: String,
    val sellerIdFilter: ReUserId = ReUserId.NONE,
    var statusFilter: ReStatus = ReStatus.NONE
)