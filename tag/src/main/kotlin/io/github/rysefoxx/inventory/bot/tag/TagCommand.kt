package io.github.rysefoxx.inventory.bot.tag

import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class TagCommand : SlashCommand() {
    override fun command(): CommandData {
        return Commands.slash("tag", "Ruft einen Tag ab.")
            .addOption(OptionType.STRING, "embed-name", "The embed name for the predefined message", true, true)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        TODO("Not yet implemented")
    }

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        TODO("Not yet implemented")
    }
}