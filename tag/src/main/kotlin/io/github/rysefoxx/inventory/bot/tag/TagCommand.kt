package io.github.rysefoxx.inventory.bot.tag

import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import io.github.rysefoxx.inveneetory.bot.command.StringConstants
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.extension.ChannelUtil
import io.github.rysefoxx.inventory.bot.spring.model.entity.EmbedDataEntity
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import io.github.rysefoxx.inventory.bot.spring.model.service.PunishmentService
import io.github.rysefoxx.inventory.bot.spring.model.service.TagService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TagCommand(

    @Autowired
    private val languageDocument: LanguageDocument,

    @Autowired
    private val tagService: TagService,

    @Autowired
    private val punishmentService: PunishmentService,

    @Autowired
    private val embedDataService: EmbedDataService

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
        val userId = event.user.idLong

        if (punishmentService.isBanned(userId, "tag")) {
            val model = punishmentService.banModel(userId, "tag")
            val from = event.jda.retrieveUserById(model.fromId ?: 0L).complete().asMention

            val placeHolder = listOf(from, model.reason ?: "Null", model.date.toString())

            var embedData = EmbedDataEntity(data = placeHolder)
            embedData = embedDataService.saveData(embedData)

            return event.replyEmbeds(
                ChannelUtil.embed(
                    "banned",
                    embedData.id.toString(),
                    userId,
                    placeHolder
                )
            ).addActionRow(
                Button.secondary(
                    StringConstants.EMBED_TRANSLATE_BUTTON,
                    languageDocument.getDefaultTranslation("translate_button_label")
                )
            ).setEphemeral(true).queue()
        }

        val tagContent = tagService.findTagByNameOrAliases(tagName)?.content
            ?: return event.reply(languageDocument.getTranslation("tags_dont_exists", event.user.idLong, tagName))
                .setEphemeral(true).queue()

        event.reply(tagContent).queue()
    }
}