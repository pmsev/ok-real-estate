package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ReAdRepoGremlin
import RepoAdReadTest
import models.ReAdId


class AdRepoGremlinReadTest : RepoAdReadTest() {
    override val repo: ReAdRepoGremlin by lazy {
        ReAdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
        )
    }
    override val successId: ReAdId by lazy {
        repo.initializedObjects.firstOrNull()?.id ?: ReAdId.NONE
    }
}
