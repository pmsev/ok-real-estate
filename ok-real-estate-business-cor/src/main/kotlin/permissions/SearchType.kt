package permissions

import ICorChainDsl
import ReContext
import chain
import models.ReSearchTypes
import models.ReState
import models.ReUserPermissions
import worker


fun ICorChainDsl<ReContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == ReState.RUNNING }
    worker("Определение типа поиска") {
        adFilterValidated.searchTypes = setOfNotNull(
            ReSearchTypes.OWN.takeIf { chainPermissions.contains(ReUserPermissions.SEARCH_OWN) },
            ReSearchTypes.PUBLIC.takeIf { chainPermissions.contains(ReUserPermissions.SEARCH_PUBLIC) },
            ReSearchTypes.REGISTERED.takeIf { chainPermissions.contains(ReUserPermissions.SEARCH_REGISTERED) },
        ).toMutableSet()
    }
}