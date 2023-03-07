package io.github.rysefoxx.inventory.bot

import dev.minn.jda.ktx.jdabuilder.default
import io.github.rysefoxx.inventory.bot.command.SlashCommandManager
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.log.Logger
import io.github.rysefoxx.inventory.bot.spring.Application
import io.github.rysefoxx.inventory.bot.spring.event.BootstrapReadyEvent
import io.github.rysefoxx.inventory.bot.spring.event.DiscordApplicationReadyEvent
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

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