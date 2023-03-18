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