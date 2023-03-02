plugins {
    kotlin("jvm") version "1.8.0"
}

group = "com.uwefuchs.demo.kotlin.myjanitor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":pocket-api"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}