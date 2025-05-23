plugins {
    kotlin("jvm") version "2.0.20"
    `maven-publish`
}

group = "cc.wordview.wordfind"
version = "2.5.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("org.slf4j:slf4j-api:2.0.16")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = rootProject.name
            version = version

            from(components["java"])
        }
    }
}