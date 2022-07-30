buildscript {
    val atomicfuVersion: String by project
    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:$atomicfuVersion")
    }
}
apply(plugin = "kotlinx-atomicfu")

plugins {
    application
    kotlin("jvm")
    id("com.bmuschko.docker-java-application") version "7.4.0"
}

application {
    mainClass.set("kafka.main")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("Kate")
//        ports.set(listOf(8080))
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


group =rootProject.group
version =rootProject.version


dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")

    // log
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":ok-real-estate-common"))
    implementation(project(":ok-real-estate-stubs"))
    implementation(project(":ok-real-estate-business-cor"))
    implementation(project(":ok-real-estate-services"))
    implementation(project(":ok-real-estate-transport-main-openapi"))
    implementation(project(":ok-real-estate-mappers"))
    testImplementation(kotlin("test-junit"))
}