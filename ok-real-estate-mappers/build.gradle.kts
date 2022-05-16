plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-real-estate-common"))
    implementation(project(":ok-real-estate-transport-main-openapi"))
    testImplementation(kotlin("test-junit"))
}