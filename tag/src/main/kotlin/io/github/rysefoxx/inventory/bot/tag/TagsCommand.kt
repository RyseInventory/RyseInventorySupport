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
import io.github.rysefoxx.inventory.bot.spring.model.entity.TagEntity
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import io.github.rysefoxx.inventory.bot.spring.model.service.PunishmentService
import io.github.rysefoxx.inventory.bot.spring.model.service.TagService
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


@Component
class TagsCommand(

    @Autowired
    private val tagService: TagService,

    @Autowired
    private val punishmentService: PunishmentService,

    @Autowired
    private val embedDataService: EmbedDataService,

    @Autowired
    private val languageDocument: LanguageDocument

) : SlashCommand() {

    override val name: String
        get() = "tags"

    companion object {
        const val TAGS_CREATE = "create"
        const val TAGS_DELETE = "delete"
        const val TAGS_LIST = "list"
        const val TAGS_INFO = "info"

        const val TAGS_OPTION_NAME = "tag-name"

        val entityCache = mutableMapOf<Long, TagEntity>()
    }

    override fun command(): CommandData {
        return Commands.slash(name, "Performs different actions.")
            .addSubcommands(subCommands())
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val tagName = event.getOption(TAGS_OPTION_NAME)?.asString

        if (tagName == null && event.subcommandName != TAGS_LIST)
            return

        when (event.subcommandName) {
            TAGS_CREATE -> {
                createTag(event, tagName!!)
            }

            TAGS_DELETE -> {
                deleteTag(event, tagName!!)
            }

            TAGS_LIST -> {
                listTags(event)
            }
        }
    }

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val focusedOption = event.focusedOption.name
        val value = event.focusedOption.value

        if (focusedOption != TAGS_OPTION_NAME || event.subcommandName != TAGS_DELETE) return

        val allTags = tagService.findAll()

        if (value.isEmpty()) {
            return event.replyChoiceStrings(allTags.map { it.name }
                .subList(0, if (allTags.size < 25) allTags.size else 25)).queue()
        }

        val list = allTags.map { it.name }.filter { it?.startsWith(value) ?: false }

        event.replyChoiceStrings(list.subList(0, list.size)).queue()
    }

    private fun listTags(event: SlashCommandInteractionEvent) {
        val userId = event.user.idLong

        val allTags = tagService.findAll()

        if (allTags.isEmpty()) {
            return event.reply(languageDocument.getTranslation("tags_list_empty", userId))
                .setEphemeral(true).queue()
        }

        val tagNames = StringBuilder()
        allTags.forEach { tagEntity ->
            tagNames.append("`").append("${tagEntity.name}`").append(", ")
        }
        val result = tagNames.dropLast(2).toString()

        event.reply(languageDocument.getTranslation("tags_list", userId, result))
            .setEphemeral(true).queue()
    }

    private fun deleteTag(event: SlashCommandInteractionEvent, tagName: String) {
        val userId = event.user.idLong
        val hasPermission = event.member?.hasPermission(Permission.ADMINISTRATOR) ?: false

        if (!hasPermission) {
            return event.reply(languageDocument.getTranslation("tags_delete_no_permission", userId))
                .setEphemeral(true).queue()
        }

        if (!tagService.exists(tagName)) {
            return event.reply(languageDocument.getTranslation("tags_dont_exists", userId, tagName))
                .setEphemeral(true).queue()
        }

        tagService.deleteTag(tagName);
        event.reply(languageDocument.getTranslation("tags_deleted", userId, tagName))
            .setEphemeral(true).queue()
    }

    private fun createTag(event: SlashCommandInteractionEvent, tagName: String) {
        val userId = event.user.idLong
        val hasPermission = event.member?.hasPermission(Permission.ADMINISTRATOR) ?: false

        if (tagService.exists(tagName)) {
            return event.reply(languageDocument.getTranslation("tags_exists", userId, tagName))
                .setEphemeral(true).queue()
        }

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

        if (entityCache.containsKey(userId)) {
            return event.reply(languageDocument.getTranslation("tags_in_progress", userId))
                .setEphemeral(true).queue()
        }

        val embedTemplate = ChannelUtil.embed(name, userId = userId, placeHolders = listOf(tagName, "❔", "❔"))
        val hook = event.replyEmbeds(embedTemplate).addActionRow(
            Button.success(
                if (hasPermission) StringConstants.BUTTON_TAG_CREATE else StringConstants.BUTTON_TAG_SUGGEST,
                languageDocument.getTranslation(
                    "tags_embed_button_${if (hasPermission) "create" else "suggest"}_label",
                    userId
                )
            ),
            Button.primary(
                StringConstants.BUTTON_TAG_CONTENT,
                languageDocument.getTranslation("tags_embed_button_content_label", userId)
            ),
            Button.primary(
                StringConstants.BUTTON_TAG_ALIAS,
                languageDocument.getTranslation("tags_embed_button_alias_label", userId)
            ),
            Button.danger(
                StringConstants.BUTTON_TAG_ABORT,
                languageDocument.getTranslation("tags_embed_button_abort_label", userId)
            )
        ).setEphemeral(true).complete()

        task(hook, userId, tagName)

        val entity = TagEntity(
            tagName,
            Date(),
            userId
        )
        entityCache[userId] = entity
    }

    private fun task(hook: InteractionHook, userId: Long, tagName: String) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (entityCache.containsKey(userId)) {
                    hook.editOriginalEmbeds(
                        ChannelUtil.embed(
                            name,
                            userId = userId,
                            placeHolders = listOf(
                                tagName,
                                entityCache[userId]!!.content ?: "❔",
                                entityCache[userId]!!.aliasesToString("❔")
                            )
                        )
                    ).setComponents().queue()

                    entityCache.remove(userId)
                } else {
                    cancel()
                }
            }
        }, 300 * 1000)
    }

    private fun subCommands(): List<SubcommandData> {
        return listOf(
            SubcommandData(TAGS_CREATE, "Creates a new tag.").apply {
                addOption(OptionType.STRING, TAGS_OPTION_NAME, "The tag name", true, false)
            },
            SubcommandData(TAGS_DELETE, "Deletes a tag").apply {
                addOption(OptionType.STRING, TAGS_OPTION_NAME, "The tag name", true, true)
            },
            SubcommandData(TAGS_LIST, "Lists all tags")
        )
    }
}