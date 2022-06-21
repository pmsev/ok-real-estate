plugins {
    kotlin("jvm")
    id ("org.openapi.generator")
}

group =rootProject.group
version =rootProject.version


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":ok-real-estate-common"))
    implementation(project(":ok-real-estate-stubs"))
    implementation(project(":ok-real-estate-business-cor"))
    testImplementation(kotlin("test-junit"))
}