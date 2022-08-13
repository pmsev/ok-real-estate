package permissions

import ICorChainDsl
import ReContext
import chain
import heplers.fail
import models.ReAd
import models.ReError
import models.ReState
import models.security.RePrincipalModel
import models.security.RePrincipalRelations
import ru.otus.otuskotlin.marketplace.biz.permissions.AccessTableConditions
import ru.otus.otuskotlin.marketplace.biz.permissions.accessTable
import worker


fun ICorChainDsl<ReContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == ReState.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        adRepoRead.principalRelations = adRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = adRepoRead.principalRelations.asSequence().flatMap { relation ->
            chainPermissions.map { permission ->
                AccessTableConditions(
                    command = command,
                    permission = permission,
                    relation = relation,
                )
            }
        }.any {
            accessTable[it] ?: false
        }
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(ReError(message = "User is not allowed to this operation"))
        }
    }
}

private fun ReAd.resolveRelationsTo(principal: RePrincipalModel): Set<RePrincipalRelations> = setOfNotNull(
    RePrincipalRelations.NONE,
    RePrincipalRelations.OWN.takeIf { principal.id == sellerId }
)