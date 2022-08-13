plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":cor"))
    implementation(project(":ok-real-estate-common"))
    implementation(project(":ok-real-estate-stubs"))
    implementation(project(":ok-real-estate-repo-in-memory"))
    implementation(project(":ok-real-estate-repo-stub"))
    testImplementation(kotlin("test-junit"))
    testApi("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}