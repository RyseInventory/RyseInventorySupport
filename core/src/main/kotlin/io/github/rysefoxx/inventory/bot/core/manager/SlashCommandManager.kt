package io.github.rysefoxx.inventory.bot.core.manager

import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import io.github.rysefoxx.inventory.bot.core.Bootstrap
import io.github.rysefoxx.inventory.bot.core.spring.event.DiscordApplicationReadyEvent
import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.core.Application
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
object SlashCommandManager : ListenerAdapter() {

    private val commands = mutableMapOf<String, SlashCommand>()

    @EventListener(DiscordApplicationReadyEvent::class)
    @Order(1)
    fun registerAll() {
        findAllCommands()
        addCommands()
    }

    private fun findAllCommands() {
        Logger.info("Trying to find all commands.")
        Application.context.getBeansOfType(SlashCommand::class.java).values.forEach {
            Logger.info("Found command [${it.command().name}|${it.javaClass.simpleName}]")
            commands[it.command().name] = it
        }
    }

    private fun addCommands() {
        Logger.info("Trying to register ${commands.size} commands.")
        Bootstrap.jda.updateCommands().addCommands(commands.values.map { it.command() }).queue {
            Logger.info("Registered all commands.")
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = commands[event.fullCommandName] ?: return
        command.onSlashCommandInteraction(event)
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val command = commands[event.fullCommandName] ?: return
        command.onAutoCompleteInteraction(event)
    }
}