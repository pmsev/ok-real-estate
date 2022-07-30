package repo

import ICorChainDsl
import ReContext
import models.ReState
import worker


fun ICorChainDsl<ReContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Подготовка данных к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == ReState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.deepCopy().apply {
            this.title = adValidated.title
            description = adValidated.description
            reIntObject = adValidated.reIntObject.deepCopy()
        }
    }
}
