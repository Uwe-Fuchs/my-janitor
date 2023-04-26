plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.uwefuchs.demo.kotlin.myjanitor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":pocket-api"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("JanitorKt")
}