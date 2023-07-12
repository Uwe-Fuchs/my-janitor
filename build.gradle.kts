plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.uwefuchs.demo.kotlin.myjanitor"
version = "0.0.1"

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
    mainClass.set("com.uwefuchs.demo.kotlin.myjanitor.JanitorKt")
}