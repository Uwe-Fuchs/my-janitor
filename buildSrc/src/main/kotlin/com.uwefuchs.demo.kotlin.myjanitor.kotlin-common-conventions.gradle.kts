import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm")
    //`jvm-test-suite`
}

group = "com.uwefuchs.demo.kotlin.myjanitor"
version = "0.0.7"

val inTestSuiteName = "integrationTest"
val assertJVersion = "3.24.2"
val jacksonVersion = "2.15.0"
val junitVersion = "5.9.1"
val mockitoVersion = "5.0.0"
val okHttpVersion = "4.11.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    constraints {
        // Define dependency versions as constraints
        implementation("org.apache.commons:commons-text:1.9")
    }

    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        register<JvmTestSuite>(inTestSuiteName) {
            dependencies {
                implementation(project)
                implementation("com.squareup.okhttp3:mockwebserver:4.10.0")
                implementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
                implementation("org.assertj:assertj-core:$assertJVersion")
                implementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
                implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
            }

            testType.set(TestSuiteType.INTEGRATION_TEST)

//            sources {
//                java {
//                    setSrcDirs(listOf("src/integrationTest/kotlin"))
//                }
//            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named(inTestSuiteName))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}


