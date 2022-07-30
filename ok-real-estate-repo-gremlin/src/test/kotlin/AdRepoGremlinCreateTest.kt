package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ReAdRepoGremlin
import RepoAdCreateTest
import repo.ReAdRepository

class AdRepoGremlinCreateTest : RepoAdCreateTest() {
    override val repo: ReAdRepository by lazy {
        ReAdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            initObjects = RepoAdSearchTest.initObjects,
            initRepo = { g -> g.V().drop().iterate() },
        )
    }
}
