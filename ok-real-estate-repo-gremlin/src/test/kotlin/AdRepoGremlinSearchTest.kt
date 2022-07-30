package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ReAdRepoGremlin
import RepoAdSearchTest
import models.ReAd


class AdRepoGremlinSearchTest: RepoAdSearchTest() {
    override val repo: ReAdRepoGremlin by lazy {
        ReAdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
        )
    }
    override val initAds: List<ReAd> = repo.initializedObjects
}
