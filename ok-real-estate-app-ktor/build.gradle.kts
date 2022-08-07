import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val bmuschkoVersion: String by project

fun DependencyHandler.ktor(module: String, prefix: String = "server-", version: String? = ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {

    id("org.jetbrains.kotlin.jvm")
    application
    java
    id("com.bmuschko.docker-java-application") version "7.4.0"
    id("com.bmuschko.docker-remote-api") version "7.4.0"
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("Kate")
        ports.set(listOf(8080))
        val imageName = project.name
        images.set(
            listOf(
                "$imageName:${project.version}",
                "$imageName:latest"
            )
        )
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}


group = rootProject.group
version = rootProject.version



dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(ktor("core"))
    implementation(ktor("netty"))

    // jackson
    implementation(ktor("jackson", "serialization"))
    implementation(ktor("content-negotiation"))

    implementation(ktor("locations"))
    implementation(ktor("caching-headers"))
    implementation(ktor("call-logging"))
    implementation(ktor("auto-head-response"))
    implementation(ktor("cors"))
    implementation(ktor("default-headers"))
    implementation(ktor("websockets"))
    implementation(ktor("auth"))
    implementation(ktor("auth-jwt"))


    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(project(":ok-real-estate-common"))
    implementation(project(":ok-real-estate-mappers"))
    implementation(project(":ok-real-estate-transport-main-openapi"))
    implementation(project(":ok-real-estate-services"))
    implementation(project(":ok-real-estate-stubs"))
    implementation(project(":ok-real-estate-repo-in-memory"))

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("test-host"))
    testImplementation(ktor("content-negotiation", prefix = "client-"))


}