plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val coroutinesVersion: String by project
    val kmpUUIDVersion: String by project

    implementation(kotlin("stdlib-common"))

    implementation(project(":ok-real-estate-api-logs"))
    implementation(project(":ok-real-estate-common"))
    implementation("com.benasher44:uuid:$kmpUUIDVersion")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))

    testApi("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

}

