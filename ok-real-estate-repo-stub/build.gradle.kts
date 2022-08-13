plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    val coroutinesVersion: String by project
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":ok-real-estate-common"))
    implementation(project(":ok-real-estate-stubs"))
}