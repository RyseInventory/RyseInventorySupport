package io.github.rysefoxx.inventory.bot.command.impl

import io.github.rysefoxx.inventory.bot.command.SlashCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class LanguageCommand : SlashCommand() {
    override fun command(): CommandData {
        return Commands.slash("language", "Sendet ein Embed mit allen Sprachen zur Auswahl")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        event.reply("Sprachen zur Auswahl:").queue()
    }
}