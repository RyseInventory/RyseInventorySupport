package io.github.rysefoxx.inventory.bot.command

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent

interface OptionalRespondEvents {

    fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {

    }
}