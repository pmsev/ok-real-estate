package permissions

import ICorChainDsl
import ReContext
import chain
import models.ReState
import models.security.ReAdPermissionClient
import models.security.ReUserGroups
import worker


fun ICorChainDsl<ReContext>.frontPermissions(title: String) = chain {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == ReState.RUNNING }

    worker {
        this.title = "Разрешения для собственного объявления"
        description = this.title
        on { adRepoDone.sellerId == principal.id }
        handle {
            val permAdd: Set<ReAdPermissionClient> = principal.groups.map {
                when (it) {
                    ReUserGroups.USER -> setOf(
                        ReAdPermissionClient.READ,
                        ReAdPermissionClient.UPDATE,
                        ReAdPermissionClient.DELETE,
                    )
                    ReUserGroups.MODERATOR_RE -> setOf()
                    ReUserGroups.ADMIN_AD -> setOf()
                    ReUserGroups.TEST -> setOf()
                    ReUserGroups.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<ReAdPermissionClient> = principal.groups.map {
                when (it) {
                    ReUserGroups.USER -> setOf()
                    ReUserGroups.MODERATOR_RE -> setOf()
                    ReUserGroups.ADMIN_AD -> setOf()
                    ReUserGroups.TEST -> setOf()
                    ReUserGroups.BAN_AD -> setOf(
                        ReAdPermissionClient.UPDATE,
                        ReAdPermissionClient.DELETE,
                    )
                }
            }.flatten().toSet()
            adRepoDone.permissionsClient.addAll(permAdd)
            adRepoDone.permissionsClient.removeAll(permDel)
        }
    }

    worker {
        this.title = "Разрешения для модератора"
        description = this.title
        on { adRepoDone.sellerId != principal.id /* && tag, group, ... */ }
        handle {
            val permAdd: Set<ReAdPermissionClient> = principal.groups.map {
                when (it) {
                    ReUserGroups.USER -> setOf()
                    ReUserGroups.MODERATOR_RE -> setOf(
                        ReAdPermissionClient.READ,
                        ReAdPermissionClient.UPDATE,
                        ReAdPermissionClient.DELETE,
                    )
                    ReUserGroups.ADMIN_AD -> setOf()
                    ReUserGroups.TEST -> setOf()
                    ReUserGroups.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<ReAdPermissionClient> = principal.groups.map {
                when (it) {
                    ReUserGroups.USER -> setOf(
                        ReAdPermissionClient.UPDATE,
                        ReAdPermissionClient.DELETE,
                    )
                    ReUserGroups.MODERATOR_RE -> setOf()
                    ReUserGroups.ADMIN_AD -> setOf()
                    ReUserGroups.TEST -> setOf()
                    ReUserGroups.BAN_AD -> setOf(
                        ReAdPermissionClient.UPDATE,
                        ReAdPermissionClient.DELETE,
                    )
                }
            }.flatten().toSet()
            adRepoDone.permissionsClient.addAll(permAdd)
            adRepoDone.permissionsClient.removeAll(permDel)
        }
    }
    worker {
        this.title = "Разрешения для администратора"
        description = this.title
    }
}
