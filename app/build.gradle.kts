plugins {
    id("com.uwefuchs.demo.kotlin.myjanitor.kotlin-application-conventions")
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.dokka") version "1.8.10"
    kotlin("plugin.spring") version "1.8.0"
}

val assertJVersion = "3.24.2"
val jacksonVersion = "2.15.0"
val junitVersion = "5.9.1"
val mockitoVersion = "5.0.0"
val okHttpVersion = "4.11.0"

dependencies {
    implementation(project(":pocket-api"))
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation(project(":pocket-api"))
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

allprojects {
    apply(plugin = "org.jetbrains.dokka")
}