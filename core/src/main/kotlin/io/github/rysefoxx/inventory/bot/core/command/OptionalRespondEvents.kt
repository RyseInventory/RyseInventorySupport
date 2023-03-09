package io.github.rysefoxx.inventory.bot.core.command

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

interface OptionalRespondEvents {

    fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {

    }
}
