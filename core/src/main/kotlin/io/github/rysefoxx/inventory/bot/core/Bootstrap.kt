package io.github.rysefoxx.inventory.bot.core

import dev.minn.jda.ktx.util.SLF4J
import io.github.rysefoxx.inventory.bot.core.spring.event.BootstrapReadyEvent
import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.core.Application
import net.dv8tion.jda.api.JDA
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext

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