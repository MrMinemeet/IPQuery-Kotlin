plugins {
    kotlin("jvm") version "2.0.21"
}

group = "ipquery.kotlin"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:2.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}