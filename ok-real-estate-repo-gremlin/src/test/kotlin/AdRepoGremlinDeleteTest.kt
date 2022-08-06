package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ReAdRepoGremlin
import RepoAdDeleteTest
import models.ReAdId


class AdRepoGremlinDeleteTest: RepoAdDeleteTest() {
    override val repo: ReAdRepoGremlin by lazy {

        ReAdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g ->
                g.V().drop().iterate()
            },
        )
    }
    override val successId: ReAdId = repo.initializedObjects.first().id
}
