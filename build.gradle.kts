plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-xml:1.6.0")
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}