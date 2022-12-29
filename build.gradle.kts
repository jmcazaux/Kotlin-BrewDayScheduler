import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.adarshr.test-logger") version "3.1.0" // Logging test results in the console
    application
    jacoco
    id("org.sonarqube") version "3.5.0.2730"
}

group = "com.ironbird"
version = "1.0.alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.0")
    implementation("info.picocli:picocli:4.7.0")
    implementation(kotlin("script-runtime"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

sonar.properties {
    property("sonar.projectKey", "Kotlin-BrewDayScheduler")
    property("sonar.organization", "jmcazaux")
    property("sonar.host.url", "https://sonarcloud.io")
}

application {
    mainClass.set("MainKt")
    applicationName = "bds"
}
