import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

group = "io.github.rysefoxx.inventory.bot.spring.model"
archivesName.set("Supportbot-Spring-Model")

dependencies {
    compileOnly(project(":logger"))
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
}