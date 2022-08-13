package permissions

import ICorChainDsl
import ReContext
import models.ReState
import models.ReUserPermissions
import models.security.ReUserGroups
import worker


fun ICorChainDsl<ReContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on {
        state == ReState.RUNNING
    }

    handle {
        val permAdd: Set<ReUserPermissions> = principal.groups.map {
            when(it) {
                ReUserGroups.USER -> setOf(
                    ReUserPermissions.READ_OWN,
                    ReUserPermissions.READ_PUBLIC,
                    ReUserPermissions.CREATE_OWN,
                    ReUserPermissions.UPDATE_OWN,
                    ReUserPermissions.DELETE_OWN,
                    ReUserPermissions.OFFER_FOR_OWN,
                )
                ReUserGroups.MODERATOR_RE -> setOf()
                ReUserGroups.ADMIN_AD -> setOf()
                ReUserGroups.TEST -> setOf()
                ReUserGroups.BAN_AD -> setOf()
            }
        }.flatten().toSet()
        val permDel: Set<ReUserPermissions> = principal.groups.map {
            when(it) {
                ReUserGroups.USER -> setOf()
                ReUserGroups.MODERATOR_RE -> setOf()
                ReUserGroups.ADMIN_AD -> setOf()
                ReUserGroups.TEST -> setOf()
                ReUserGroups.BAN_AD -> setOf(
                    ReUserPermissions.UPDATE_OWN,
                    ReUserPermissions.CREATE_OWN,
                )
            }
        }.flatten().toSet()
        chainPermissions.addAll(permAdd)
        chainPermissions.removeAll(permDel)
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $chainPermissions")
    }
}
