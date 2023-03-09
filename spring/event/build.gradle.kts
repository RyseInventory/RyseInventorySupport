import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

group = "io.github.rysefoxx.inventory.bot.spring.event"
archivesName.set("Supportbot-Spring-Event")

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
}