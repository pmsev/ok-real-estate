plugins {
    kotlin("jvm") apply false
}

group = "ru.otus.otuskotlin.realestate"
version = "0.0.1"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
