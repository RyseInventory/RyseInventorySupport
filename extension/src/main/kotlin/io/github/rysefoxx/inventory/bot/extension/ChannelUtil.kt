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