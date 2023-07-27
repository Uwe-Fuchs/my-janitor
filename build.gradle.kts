plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.dokka") version "1.8.10"
    application
}

group = "com.uwefuchs.demo.kotlin.myjanitor"
version = "0.0.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":pocket-api"))
    testImplementation(kotlin("test"))
}

allprojects {
    apply(plugin = "org.jetbrains.dokka")
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