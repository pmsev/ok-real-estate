package mappers

import io.ktor.server.auth.jwt.*
import models.ReUserId
import models.security.RePrincipalModel
import models.security.ReUserGroups


fun JWTPrincipal?.toModel() = this?.run {
    RePrincipalModel(
        id = payload.getClaim("id").asString()?.let { ReUserId(it) } ?: ReUserId.NONE,
        fname = payload.getClaim("fname").asString() ?: "",
        mname = payload.getClaim("mname").asString() ?: "",
        lname = payload.getClaim("lname").asString() ?: "",
        groups = payload
            .getClaim("groups")
            ?.asList(String::class.java)
            ?.mapNotNull {
                try {
                    ReUserGroups.valueOf(it)
                } catch (e: Throwable) {
                    null
                }
            }?.toSet() ?: emptySet()
    )
} ?: RePrincipalModel.NONE
