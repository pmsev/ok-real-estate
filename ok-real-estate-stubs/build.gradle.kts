plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    val jacksonVersion: String by project
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":ok-real-estate-common"))
}

