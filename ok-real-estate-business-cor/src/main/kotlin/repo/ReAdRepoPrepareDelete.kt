package repo

import ICorChainDsl
import ReContext
import models.ReState
import worker


fun ICorChainDsl<ReContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Подготовка данных к удалению из БД
    """.trimIndent()
    on { state == ReState.RUNNING }
    handle {
        adRepoPrepare = adValidated.deepCopy()
    }
}
