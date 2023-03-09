import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

group = "io.github.rysefoxx.inventory.bot.spring.core"
archivesName.set("Supportbot-Spring-Core")

dependencies {
    compileOnly(project(":logger"))
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")

    implementation("commons-io:commons-io:2.11.0")
}