rootProject.name = "ok-real-estate"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val kotestVersion: String by settings
        val openapiVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatform") version kotlinVersion apply false
        id("io.kotest.multiplatform") version kotestVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}


include ("ok-real-estate-transport-main-openapi")
include ("ok-real-estate-common")
include("ok-real-estate-mappers")
include("ok-real-estate-services")
include("ok-real-estate-stubs")
include("ok-real-estate-app-ktor")
include("cor")
include("ok-real-estate-business-cor")
include("ok-real-estate-kafka")
