package io.github.rysefoxx.inventory.bot.core

import io.github.rysefoxx.inventory.bot.core.spring.event.BootstrapReadyEvent
import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.core.Application
import net.dv8tion.jda.api.JDA
import org.springframework.boot.SpringApplication

fun main() {
    Bootstrap()
}

open class Bootstrap {

    companion object {
        lateinit var jda: JDA
        lateinit var instance: Bootstrap
    }

    init {
        instance = this
        Logger.info("Trying to start Spring Boot Application.")
        Application.context = SpringApplication.run(Application::class.java)
        Logger.info("Spring Boot Application started.")

        publishBootstrapEvent()
    }

    private fun publishBootstrapEvent() {
        Logger.info("Publishing event BootstrapReadyEvent.")
        Application.context.publishEvent(BootstrapReadyEvent())
    }
}