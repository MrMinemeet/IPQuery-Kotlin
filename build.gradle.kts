plugins {
    kotlin("jvm") version "2.0.21"
}

description = "Kotlin library to query IP addresses using the https://ipQuery.io/ API"
group = "ipquery.kotlin"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0") {
        because("Used to parse the JSON response from the API")
    }
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:2.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}