plugins {
    id("com.uwefuchs.demo.kotlin.myjanitor.kotlin-application-conventions")
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.dokka") version "1.8.10"
    kotlin("plugin.spring") version "1.8.0"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":pocket-api"))
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation(project(":shared"))
    testImplementation(project(":pocket-api"))
    integrationTestImplementation(project(":shared"))
    integrationTestImplementation(project(":pocket-api"))
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-web")
}

allprojects {
    apply(plugin = "org.jetbrains.dokka")
}