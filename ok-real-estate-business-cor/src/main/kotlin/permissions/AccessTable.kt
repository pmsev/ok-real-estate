package ru.otus.otuskotlin.marketplace.biz.permissions

import models.ReCommand
import models.ReUserPermissions
import models.security.RePrincipalRelations


data class AccessTableConditions(
    val command: ReCommand,
    val permission: ReUserPermissions,
    val relation: RePrincipalRelations
)

val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = ReCommand.CREATE,
        permission = ReUserPermissions.CREATE_OWN,
        relation = RePrincipalRelations.NONE
    ) to true,

    // Read
    AccessTableConditions(
        command = ReCommand.READ,
        permission = ReUserPermissions.READ_OWN,
        relation = RePrincipalRelations.OWN
    ) to true,
    AccessTableConditions(
        command = ReCommand.READ,
        permission = ReUserPermissions.READ_PUBLIC,
        relation = RePrincipalRelations.PUBLIC
    ) to true,

    // Update
    AccessTableConditions(
        command = ReCommand.UPDATE,
        permission = ReUserPermissions.UPDATE_OWN,
        relation = RePrincipalRelations.OWN
    ) to true,

    // Delete
    AccessTableConditions(
        command = ReCommand.DELETE,
        permission = ReUserPermissions.DELETE_OWN,
        relation = RePrincipalRelations.OWN
    ) to true,

    // Offers
    AccessTableConditions(
        command = ReCommand.OFFERS,
        permission = ReUserPermissions.OFFER_FOR_OWN,
        relation = RePrincipalRelations.NONE
    ) to true,
)
