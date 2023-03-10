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
        embedId: String? = null
    ) {
        jda.getTextChannelById(channelId)?.sendMessageEmbeds(embed(key, embedId, placeHolders))?.queue()
    }

    fun embed(
        key: String,
        channelId: String,
        jda: JDA,
        placeHolders: List<String>? = null,
        embedId: String? = null
    ): MessageCreateAction? {
        return jda.getTextChannelById(channelId)?.sendMessageEmbeds(embed(key, embedId, placeHolders))
    }

    fun embed(key: String, embedId: String? = null, placeHolders: List<String>? = null): MessageEmbed {
        val languageDocument = Application.context.getBean(LanguageDocument::class.java) ?: return Embed()
        var configDescription =
            languageDocument.getDefaultTranslationList("${key}_embed_description").joinToString("\n") { it }

        if (placeHolders != null) {
            for (i in placeHolders.indices) {
                configDescription = configDescription.replaceFirst("{$i}", placeHolders[i])
            }
        }

        val finalKey = "$key${embedId?.let { "_$it" } ?: ""}"

        return Embed {
            title = languageDocument.getDefaultTranslation("${key}_embed_title")
            description = configDescription
            color = 0xA87406
            footer(finalKey)
        }
    }
}