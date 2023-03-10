package io.github.rysefoxx.inveneetory.bot.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface RespondEvents : OptionalRespondEvents {

    fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)


}