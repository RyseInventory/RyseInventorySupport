/*
 * Copyright (c)  2023 Rysefoxx
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.rysefoxx.inventory.bot.tag

import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import io.github.rysefoxx.inveneetory.bot.command.StringConstants
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.extension.ChannelUtil
import io.github.rysefoxx.inventory.bot.spring.model.entity.EmbedDataEntity
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import io.github.rysefoxx.inventory.bot.spring.model.service.PunishmentService
import io.github.rysefoxx.inventory.bot.spring.model.service.TagService
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
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

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val focusedOption = event.focusedOption.name
        val value = event.focusedOption.value

        if (focusedOption != TAGS_OPTION_NAME || event.name != name) return

        val allTags = tagService.findAll()

        if (value.isEmpty()) {
            return event.replyChoiceStrings(allTags.map { it.name }.subList(0, if (allTags.size < 25) allTags.size else 25)).queue()
        }

        val list = allTags.map { it.name }.filter { it?.startsWith(value) ?: false }

        event.replyChoiceStrings(list.subList(0, list.size)).queue()
    }
}