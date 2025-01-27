import com.diffplug.spotless.LineEnding

plugins {
    kotlin("jvm") version "2.0.21"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.18.0"
}

group = "io.github.petretiandrea"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

var ktorVersion = "3.0.3"

dependencies {
    implementation("io.arrow-kt:arrow-core:2.0.0")

    // ktor client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-gson:$ktorVersion")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0") // Mockito Kotlin extension
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // For coroutine testing
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    lineEndings = LineEnding.UNIX
    kotlin {
        toggleOffOn()
        targetExclude("build/**/*")
        ktfmt().kotlinlangStyle()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        toggleOffOn()
        targetExclude("build/**/*.kts")
        ktfmt().googleStyle()
    }
}