package io.github.rysefoxx.inventory.bot.command.impl

import io.github.rysefoxx.inventory.bot.command.SlashCommand
import io.github.rysefoxx.inventory.bot.model.entity.UserEntity
import io.github.rysefoxx.inventory.bot.model.service.UserService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TestCommand(

    @Autowired
    private val userService: UserService

) : SlashCommand() {
    override fun command(): CommandData {
        return Commands.slash("test", "Führt ein Test aus")
            .addOption(OptionType.STRING, "test", "test beschreibung", true)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        event.reply("Test erfolgreich!").queue()


        val entity = UserEntity(event.idLong, "en")
        userService.saveUser(entity)

    }
}