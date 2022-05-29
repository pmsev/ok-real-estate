import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

fun DependencyHandler.ktor(module: String, prefix: String = "server-", version: String? = ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set(".ApplicationKt")
}



group = rootProject.group
version =rootProject.version



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

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(project(":ok-real-estate-common"))
    implementation(project(":ok-real-estate-mappers"))
    implementation(project(":ok-real-estate-transport-main-openapi"))
    implementation(project(":ok-real-estate-services"))
    implementation(project(":ok-real-estate-stubs"))

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("test-host"))
    testImplementation(ktor("content-negotiation", prefix = "client-"))


}