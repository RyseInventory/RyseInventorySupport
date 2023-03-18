package io.github.rysefoxx.inventory.bot.tag

import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.spring.model.service.TagService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TagCommand(

    @Autowired
    private val languageDocument: LanguageDocument,

    @Autowired
    private val tagService: TagService

) : SlashCommand() {

    override val name: String
        get() = "tag"

    companion object {
        const val TAGS_OPTION_NAME = "tag-name"
    }

    override fun command(): CommandData {
        return Commands.slash(name, "Retrieves a tag.")
            .addOption(OptionType.STRING, TAGS_OPTION_NAME, "The tag to be executed.", true, true)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val tagName = event.getOption(TAGS_OPTION_NAME)?.asString ?: return

        val tagContent = tagService.findTagByNameOrAliases(tagName)?.content
            ?: return event.reply(languageDocument.getTranslation("tags_dont_exists", event.user.idLong, tagName))
                .setEphemeral(true).queue()

        event.reply(tagContent).queue()
    }
}