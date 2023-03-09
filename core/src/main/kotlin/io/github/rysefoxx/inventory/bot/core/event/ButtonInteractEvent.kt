package io.github.rysefoxx.inventory.bot.core.event

import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.EmbedBuilder
import io.github.rysefoxx.inventory.bot.core.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.core.model.service.UserService
import io.github.rysefoxx.inventory.bot.core.util.StringConstants
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ButtonInteractEvent(
    @Autowired
    private val languageDocument: LanguageDocument

) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.user.isBot)
            return

        when (event.componentId.split("_")[1]) {
            StringConstants.EMBED_TRANSLATE_BUTTON -> {
                val embed = event.message.embeds[0]
                val key = embed.footer?.text ?: return

                val builder = EmbedBuilder().apply {
                    setTitle(languageDocument.getTranslation("${key}_embed_title", event.user.idLong))
                    setDescription(languageDocument.getTranslationList("${key}_embed_description", event.user.idLong)
                        .joinToString("\n") { it })
                    setColor(embed.color)
                }

                event.replyEmbeds(builder.build())
                    .setEphemeral(true)
                    .queue()
            }
        }
    }
}