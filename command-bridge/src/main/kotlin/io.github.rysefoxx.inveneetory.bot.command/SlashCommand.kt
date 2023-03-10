package io.github.rysefoxx.inveneetory.bot.command

import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class SlashCommand : RespondEvents {

    abstract val name: String

    abstract fun command(): CommandData

}