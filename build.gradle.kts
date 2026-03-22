plugins {
    kotlin("jvm") version "2.3.10"
    application
}

application {
    mainClass.set("MainKt")
}

group = "fr.anthonus"
version = file("src/main/resources/version.txt").readText().trim()

repositories {
    mavenCentral()

    maven {
        name = "Lavalink"
        url = uri("https://maven.lavalink.dev/releases")
    }
}

dependencies {
    // Source: https://mvnrepository.com/artifact/net.dv8tion/JDA
    implementation("net.dv8tion:JDA:6.3.2")
    // Source: https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.32")

    // Source: https://mvnrepository.com/artifact/io.github.cdimascio/dotenv-java
    implementation("io.github.cdimascio:dotenv-java:3.2.0")

    // Source: https://mvnrepository.com/artifact/dev.arbjerg/lavaplayer
    implementation("dev.arbjerg:lavaplayer:2.2.6")
    // Source: https://mvnrepository.com/artifact/dev.lavalink.youtube/v2
    implementation("dev.lavalink.youtube:v2:1.18.0")

    // Source: https://mvnrepository.com/artifact/club.minnced/jdave-api
    implementation("club.minnced:jdave-api:0.1.7")
    // Source: https://mvnrepository.com/artifact/club.minnced/jdave-native-linux-x86-64
    implementation("club.minnced:jdave-native-linux-x86-64:0.1.7")
    // Source: https://mvnrepository.com/artifact/club.minnced/jdave-native-win-x86-64
    implementation("club.minnced:jdave-native-win-x86-64:0.1.7")

    // Source: https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    // Source: https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.2")
}

kotlin {
    jvmToolchain(25)
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}
