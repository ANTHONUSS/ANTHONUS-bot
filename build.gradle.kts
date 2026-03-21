plugins {
    kotlin("jvm") version "2.3.10"
}

group = "fr.anthonus"
version = file("src/main/resources/version.txt").readText().trim()

repositories {
    mavenCentral()
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

    // Source: https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    // Source: https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.2")

}

kotlin {
    jvmToolchain(25)
}