plugins {
    id("com.uwefuchs.demo.kotlin.myjanitor.kotlin-library-conventions")
    `jvm-test-suite`
}

val assertJVersion = "3.24.2"
val jacksonVersion = "2.15.0"
val junitVersion = "5.9.1"
val mockitoVersion = "5.0.0"
val okHttpVersion = "4.11.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}


