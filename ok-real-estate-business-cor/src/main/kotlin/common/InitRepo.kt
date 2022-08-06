package common

import ICorChainDsl
import ReContext
import heplers.errorDevOps
import heplers.fail
import models.ReWorkMode
import repo.ReAdRepository
import worker

fun ICorChainDsl<ReContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        adRepo = when (workMode) {
            ReWorkMode.TEST -> settings.repoTest
            ReWorkMode.STUB -> ReAdRepository.NONE
            else -> settings.repoProd
        }
        if (workMode != ReWorkMode.STUB && adRepo == ReAdRepository.NONE) fail(
            errorDevOps(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
