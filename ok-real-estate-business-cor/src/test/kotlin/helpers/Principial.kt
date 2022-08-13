package helpers

import models.ReUserId
import models.security.RePrincipalModel
import models.security.ReUserGroups
import stubs.RealEstate

fun principalUser(id: ReUserId = RealEstate.getModel().sellerId, banned: Boolean = false) = RePrincipalModel(
    id = id,
    groups = setOf(
        ReUserGroups.USER,
        ReUserGroups.TEST,
        if (banned) ReUserGroups.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)
