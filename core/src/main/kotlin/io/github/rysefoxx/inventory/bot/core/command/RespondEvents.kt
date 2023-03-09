package io.github.rysefoxx.inventory.bot.core.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface RespondEvents : OptionalRespondEvents {

    fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)



}