package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

object ArcadeDbContainer {
    val container by lazy {
        GenericContainer(DockerImageName.parse("arcadedata/arcadedb:latest")).apply {
            withExposedPorts(2480, 2424, 8182)
            withEnv("arcadedb.server.rootPassword", "1r2d3g4h")
            withEnv("arcadedb.server.plugins", "GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin")
                .withEnv("TESTCONTAINERS_RYUK_DISABLED", "true")
            waitingFor(Wait.forLogMessage(".*ArcadeDB Server started.*\\n", 1))
            start()
            println("ARCADE: http://${host}:${getMappedPort(2480)}")
            println("ARCADE: http://${host}:${getMappedPort(2424)}")
            println(this.logs)
            println("RUNNING?: ${this.isRunning}")
        }
    }
}
