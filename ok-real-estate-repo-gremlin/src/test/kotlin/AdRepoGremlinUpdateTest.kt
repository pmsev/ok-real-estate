package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ReAdRepoGremlin
import RepoAdUpdateTest
import models.ReAd
import models.ReAdId

class AdRepoGremlinUpdateTest: RepoAdUpdateTest() {
    override val repo: ReAdRepoGremlin by lazy {
        ReAdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
            randomUuid = { newLock.asString() },
        )
    }
    override val updateId: ReAdId = repo.initializedObjects.first().id
    override val updateObj: ReAd = super.updateObj.copy(id = updateId)
}
