import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

group = "io.github.rysefoxx.inventory.bot.command"
archivesName.set("Supportbot-CommandBridge")

dependencies {
    compileOnly("net.dv8tion:JDA:5.0.0-beta.5")

    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")

    compileOnly(project(":spring:event"))
    compileOnly(project(":logger"))
}