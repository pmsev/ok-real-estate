plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    val coroutinesVersion: String by project
    implementation(kotlin("stdlib-common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}
