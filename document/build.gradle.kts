import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

group = "io.github.rysefoxx.inventory.bot.document"
archivesName.set("Supportbot-Document")

repositories {
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly(project(":logger"))
    compileOnly(project(":spring:event"))
    compileOnly(project(":spring:model"))

    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
}