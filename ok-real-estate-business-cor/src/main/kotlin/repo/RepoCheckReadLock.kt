package repo

import ICorChainDsl
import ReContext
import heplers.errorConcurrency
import heplers.fail
import models.ReState
import worker

fun ICorChainDsl<ReContext>.repoCheckReadLock(title: String) = worker {
    this.title = title
    description = """
        Проверяем, что блокировка из запроса совпадает с блокировкой в БД
    """.trimIndent()
    on { state == ReState.RUNNING && adValidated.lock != adRepoRead.lock }
    handle {
        fail(errorConcurrency(violationCode = "changed", "Object has been inconsistently changed"))
        adRepoDone = adRepoRead
    }
}
