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
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.extension.ChannelUtil
import io.github.rysefoxx.inventory.bot.spring.model.entity.TagEntity
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TagModalInteractEvent(

    @Autowired
    private val languageDocument: LanguageDocument

) : ListenerAdapter() {

    override fun onModalInteraction(event: ModalInteractionEvent) {
        val userId = event.user.idLong;
        val entity = TagsCommand.entityCache[userId] ?: return

        when(event.modalId) {
            StringConstants.MODAL_TAG_ALIAS -> {
                val mapping = event.getValue(StringConstants.TEXT_TAG_ALIAS) ?: return
                val alias = mapping.asString

                if(alias.equals(entity.name, true))  {
                    return event.reply(languageDocument.getTranslation("tags_alias_identical", userId)).setEphemeral(true).queue()
                }

                entity.aliases = entity.toAliases(alias)

                return update(event, entity, entity.aliasesToString("❔"))
            }

            StringConstants.MODAL_TAG_CONTENT -> {
                val mapping = event.getValue(StringConstants.TEXT_TAG_CONTENT) ?: return
                entity.content = mapping.asString

                update(event, entity, entity.aliasesToString("❔"))
            }
        }
    }

    private fun update(event: ModalInteractionEvent, entity: TagEntity, aliases: String) {
        event.editMessageEmbeds( ChannelUtil.embed(
            "tags",
            userId = event.user.idLong,
            placeHolders = listOf(entity.name ?: "❔", entity.content ?: "❔", aliases)
        )).queue()
    }
}