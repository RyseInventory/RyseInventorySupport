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

package io.github.rysefoxx.inventory.bot.core.event

import dev.minn.jda.ktx.messages.Embed
import io.github.rysefoxx.inveneetory.bot.command.StringConstants
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ButtonInteractEvent(
    @Autowired
    private val languageDocument: LanguageDocument,

    @Autowired
    private val embedDataService: EmbedDataService

) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.user.isBot)
            return

        when (event.componentId) {
            StringConstants.EMBED_TRANSLATE_BUTTON -> {
                val embed = event.message.embeds[0]
                val keyArray = embed.footer?.text?.split("_") ?: return

                val key = keyArray[0]
                var placeHolder = emptyList<String>()

                if (keyArray.size > 1) {
                    val embedId = try {
                        keyArray[1].toLong()
                    } catch (e: NumberFormatException) {
                        -1
                    }
                    if (embedId != -1L)
                        placeHolder = embedDataService.findById(embedId)?.data ?: emptyList()

                    if (placeHolder.isEmpty()) {
                        return event.reply(languageDocument.getTranslation("data_not_found", event.user.idLong))
                            .setEphemeral(true).queue()
                    }
                }

                val builder = Embed {
                    title = languageDocument.getTranslation("${key}_embed_title", event.user.idLong)
                    description =
                        languageDocument.getTranslationList("${key}_embed_description", event.user.idLong, placeHolder)
                            .joinToString("\n") { it }
                    color = embed.colorRaw
                }

                event.replyEmbeds(builder)
                    .setEphemeral(true)
                    .queue()
            }
        }
    }
}