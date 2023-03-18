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

import io.github.rysefoxx.inveneetory.bot.command.StringConstants
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.extension.ChannelUtil
import io.github.rysefoxx.inventory.bot.spring.model.entity.EmbedDataEntity
import io.github.rysefoxx.inventory.bot.spring.model.entity.TagEntity
import io.github.rysefoxx.inventory.bot.spring.model.entity.TagRequestEntity
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import io.github.rysefoxx.inventory.bot.spring.model.service.TagService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class TagButtonInteractEvent(

    @Autowired
    private val languageDocument: LanguageDocument,

    @Autowired
    private val tagService: TagService,

    @Autowired
    private val embedDataService: EmbedDataService

) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.user.isBot) return
        val userId = event.user.idLong

        when (event.componentId) {
            StringConstants.BUTTON_TAG_CREATE -> {
                createTag(event)
            }

            StringConstants.BUTTON_TAG_SUGGEST -> {
                createTagSuggest(event)
            }

            StringConstants.BUTTON_TAG_CONTENT -> {
                contentModal(event)
            }

            StringConstants.BUTTON_TAG_ALIAS -> {
                aliasModal(event)
            }

            StringConstants.BUTTON_TAG_ABORT -> {
                abort(userId, event)
            }

            StringConstants.BUTTON_TAG_REQUEST_ACCEPT -> {
                acceptTag(event)
            }

            StringConstants.BUTTON_TAG_REQUEST_DENY -> {
               declineTag(event)
            }
        }
    }

    private fun declineTag(event: ButtonInteractionEvent) {
        val userId = event.user.idLong
        val embedId = findEmbedId(event)
        val embedData = findEmbedData(embedId)
        event.message.delete().queue()

        val data = embedData?.data

        if(!hasDataSet(event, embedId, data)) return

        val creatorId = data!!.getOrNull(0)?.toLong() ?: return
        val tagName = data.getOrNull(1) ?: return
        val tagContent = data.getOrNull(2) ?: return
        val alias = data.getOrNull(3) ?: return

        embedDataService.deleteData(embedId)

        event.reply(languageDocument.getTranslation("tags_declined", userId, tagName))
            .setEphemeral(true).queue()

        val creator = event.jda.retrieveUserById(creatorId).complete() ?: return

        creator.openPrivateChannel().complete()
            .sendMessageEmbeds(
                ChannelUtil.embed(
                    "tags.declined",
                    userId = userId,
                    placeHolders = listOf(event.user.asMention, tagName, tagContent, alias)
                )
            ).queue()
    }

    private fun acceptTag(event: ButtonInteractionEvent) {
        val userId = event.user.idLong
        val embedId = findEmbedId(event)
        val embedData = findEmbedData(embedId)

        val data = embedData?.data

        if(!hasDataSet(event, embedId, data)) return

        val creatorId = data!!.getOrNull(0)?.toLong() ?: return
        val tagName = data.getOrNull(1) ?: return
        val tagContent = data.getOrNull(2) ?: return
        val alias = data.getOrNull(3) ?: return

        if (tagService.exists(tagName)) {
            event.deferEdit()
            return
        }

        val entity = TagEntity(
            tagName,
            Date(),
            creatorId,
            tagContent
        )

        entity.aliases = entity.toAliases(alias)

        tagService.saveTag(entity)
        embedDataService.deleteData(embedId)

        event.message.delete().queue()

        event.reply(languageDocument.getTranslation("tags_created", userId, tagName))
            .setEphemeral(true).queue()

        val creator = event.jda.retrieveUserById(creatorId).complete() ?: return

        creator.openPrivateChannel().complete()
            .sendMessageEmbeds(
                ChannelUtil.embed(
                    "tags.accepted",
                    userId = userId,
                    placeHolders = listOf(event.user.asMention, tagName, tagContent, alias)
                )
            ).queue()
    }

    private fun aliasModal(event: ButtonInteractionEvent) {
        val userId = event.user.idLong
        event.replyModal(
            buildModal(
                userId,
                StringConstants.TEXT_TAG_ALIAS,
                "tags_text_alias_label",
                "tags_text_alias_placeholder",
                StringConstants.MODAL_TAG_ALIAS,
                "tags_modal_alias_title",
                TagsCommand.entityCache[userId]?.aliasesToStringNullable()
            )
        ).queue();
    }

    private fun contentModal(event: ButtonInteractionEvent) {
        val userId = event.user.idLong
        event.replyModal(
            buildModal(
                userId,
                StringConstants.TEXT_TAG_CONTENT,
                "tags_text_content_label",
                "tags_text_content_placeholder",
                StringConstants.MODAL_TAG_CONTENT,
                "tags_modal_content_title",
                TagsCommand.entityCache[userId]?.content
            )
        ).queue();
    }

    private fun createTagSuggest(event: ButtonInteractionEvent) {
        val userId = event.user.idLong
        val entity = TagsCommand.entityCache[userId] ?: return

        if (entity.content.isNullOrEmpty()) {
            return event.reply(languageDocument.getTranslation("tags_empty_content", userId)).setEphemeral(true)
                .queue()
        }

        val requestEntity = TagRequestEntity(tagEntity = entity)
        tagService.saveTagRequest(requestEntity)

        val placeHolder = mutableListOf(
            event.user.id,
            entity.name ?: "Null",
            entity.content ?: "Null",
            entity.aliasesToString("")
        )

        var embedData = EmbedDataEntity(data = placeHolder)
        embedData = embedDataService.saveData(embedData)

        placeHolder[0] = event.user.asMention

        abort(userId, event)

        ChannelUtil.embed(
            "tags.request",
            EnvironmentHolder.data.getProperty("TAG_REQUEST_ID"),
            event.jda,
            placeHolder,
            embedData.id.toString()
        )?.addActionRow(
            Button.success(
                StringConstants.BUTTON_TAG_REQUEST_ACCEPT,
                languageDocument.getDefaultTranslation("tags.request_button_accept_label")
            ),
            Button.danger(
                StringConstants.BUTTON_TAG_REQUEST_DENY,
                languageDocument.getDefaultTranslation("tags.request_button_deny_label")
            ),
            Button.secondary(
                StringConstants.EMBED_TRANSLATE_BUTTON,
                languageDocument.getDefaultTranslation("translate_button_label")
            )
        )?.queue()

        event.user.openPrivateChannel().complete()
            .sendMessageEmbeds(
                ChannelUtil.embed(
                    "tags.request.send",
                    userId = userId,
                    placeHolders = listOf(entity.name ?: "Null", entity.content ?: "Null", entity.aliasesToString(""))
                )
            ).queue()
    }

    private fun createTag(event: ButtonInteractionEvent) {
        val userId = event.user.idLong
        val entity = TagsCommand.entityCache[userId] ?: return

        if (entity.content.isNullOrEmpty()) {
            return event.reply(languageDocument.getTranslation("tags_empty_content", userId)).setEphemeral(true)
                .queue()
        }

        tagService.saveTag(entity)
        abort(userId, event)
    }

    private fun abort(userId: Long, event: ButtonInteractionEvent) {
        TagsCommand.entityCache.remove(userId)

        val oldEmbed = event.message.embeds[0]
        event.interaction.editMessageEmbeds(oldEmbed).setComponents().queue()
    }

    private fun buildModal(
        userId: Long,
        textId: String,
        textLabelKey: String,
        textPlaceholderKey: String,
        modalId: String,
        modalTitleKey: String,
        value: String? = null
    ): Modal {
        val body = TextInput.create(
            textId,
            languageDocument.getTranslation(textLabelKey, userId),
            TextInputStyle.PARAGRAPH
        )
            .setPlaceholder(languageDocument.getTranslation(textPlaceholderKey, userId))
            .setValue(value)
            .setMaxLength(200)
            .build()

        return Modal.create(
            modalId,
            languageDocument.getTranslation(modalTitleKey, userId)
        )
            .addActionRow(body)
            .build()
    }


    private fun findEmbedId(event: ButtonInteractionEvent): Long {
        val embed = event.message.embeds[0]
        val keyArray = embed.footer?.text?.split("_") ?: return -1L
        return try {
            keyArray[1].toLong()
        } catch (e: NumberFormatException) {
            -1
        }
    }

    private fun findEmbedData(embedId: Long): EmbedDataEntity? {
        return embedDataService.findById(embedId)
    }

    private fun hasDataSet(event: ButtonInteractionEvent, embedId: Long, data: List<String>?): Boolean {
        val userId = event.user.idLong
        if (embedId == -1L) {
            event.reply(languageDocument.getTranslation("data_not_found", userId))
                .setEphemeral(true).queue()
            return false
        }

        if (data == null || data.size < 4) {
            event.reply(languageDocument.getTranslation("data_not_found", userId))
                .setEphemeral(true)
                .queue()
            return false
        }
        return true
    }
}