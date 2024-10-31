plugins {
    kotlin("jvm") version "2.0.20"
}

group = "cc.wordview.wordfind"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}