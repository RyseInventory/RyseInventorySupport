package io.github.rysefoxx.inventory.bot

import io.github.rysefoxx.inventory.bot.log.Logger
import io.github.rysefoxx.inventory.bot.spring.Application
import io.github.rysefoxx.inventory.bot.spring.event.BootstrapReadyEvent
import net.dv8tion.jda.api.JDA
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    Bootstrap()
}

class Bootstrap {

    companion object {
        lateinit var context: ConfigurableApplicationContext
        lateinit var jda: JDA
        lateinit var instance: Bootstrap
    }

    init {
        instance = this
        Logger.info("Trying to start Spring Boot Application.")
        context = SpringApplication.run(Application::class.java)
        Logger.info("Spring Boot Application started.")

        publishBootstrapEvent()
    }

    private fun publishBootstrapEvent() {
        Logger.info("Publishing event BootstrapReadyEvent.")
        context.publishEvent(BootstrapReadyEvent())
    }
}