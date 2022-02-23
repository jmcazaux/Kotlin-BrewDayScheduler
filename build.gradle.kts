import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "com.ironbird"
version = "1.0.alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.+")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
