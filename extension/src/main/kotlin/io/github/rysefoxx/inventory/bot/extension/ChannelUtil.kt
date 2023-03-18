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

package io.github.rysefoxx.inventory.bot.extension

import dev.minn.jda.ktx.messages.Embed
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.spring.core.Application
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction

object ChannelUtil {

    fun postEmbed(
        key: String,
        channelId: String,
        jda: JDA,
        placeHolders: List<String>? = null,
        embedId: String? = null,
        userId: Long? = null
    ) {
        jda.getTextChannelById(channelId)?.sendMessageEmbeds(embed(key, embedId, userId, placeHolders))?.queue()
    }

    fun embed(
        key: String,
        channelId: String,
        jda: JDA,
        placeHolders: List<String>? = null,
        embedId: String? = null,
        userId: Long? = null
    ): MessageCreateAction? {
        return jda.getTextChannelById(channelId)?.sendMessageEmbeds(embed(key, embedId, userId, placeHolders))
    }

    fun embed(
        key: String,
        embedId: String? = null,
        userId: Long? = null,
        placeHolders: List<String>? = null
    ): MessageEmbed {
        val languageDocument = Application.context.getBean(LanguageDocument::class.java) ?: return Embed()
        var configDescription =
            userId?.let { languageDocument.getTranslation("${key}_embed_description", userId) }
                ?: languageDocument.getDefaultTranslation("${key}_embed_description")

        if (placeHolders != null) {
            for (i in placeHolders.indices) {
                configDescription = configDescription.replaceFirst("{$i}", placeHolders[i])
            }
        }

        val finalKey = "$key${embedId?.let { "_$it" } ?: ""}"

        return Embed {
            title =
                userId?.let { languageDocument.getTranslation("${key}_embed_title", userId) }
                    ?: languageDocument.getDefaultTranslation("${key}_embed_title")
            description = configDescription
            color = 0xA87406
            footer(finalKey)
        }
    }
}