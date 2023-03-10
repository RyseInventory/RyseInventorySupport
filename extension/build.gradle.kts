import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

group = "io.github.rysefoxx.inventory.bot.extension"
archivesName.set("Supportbot-Extension")

repositories {
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly(project(":logger"))
    compileOnly(project(":command-bridge"))
    compileOnly(project(":spring:core"))
    compileOnly(project(":document"))


    compileOnly("net.dv8tion:JDA:5.0.0-beta.5")
    compileOnly("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
}