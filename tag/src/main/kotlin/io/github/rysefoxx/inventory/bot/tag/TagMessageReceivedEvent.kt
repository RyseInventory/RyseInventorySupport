package io.github.rysefoxx.inventory.bot.tag

import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.extension.ChannelUtil
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import io.github.rysefoxx.inventory.bot.spring.model.service.PunishmentService
import io.github.rysefoxx.inventory.bot.spring.model.service.TagService
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TagMessageReceivedEvent(

    @Autowired
    private val languageDocument: LanguageDocument,

    @Autowired
    private val tagService: TagService,

    @Autowired
    private val punishmentService: PunishmentService,

    @Autowired
    private val embedDataService: EmbedDataService

) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot)
            return

        val userId = event.author.idLong

        val message = event.message.contentRaw
        if (!message.startsWith("tag", true))
            return

        val array = message.split(" ")
        if (array.size < 2)
            return

        val tagName = array[1]
        val tagContent = tagService.findTagByNameOrAliases(tagName)?.content
            ?: return

        if (punishmentService.isBanned(userId, "tag")) {
            val model = punishmentService.banModel(userId, "tag")
            val from = event.jda.retrieveUserById(model.fromId ?: 0L).complete().asMention

            val placeHolder = listOf(from, model.reason ?: "Null", model.date.toString())

            event.message.addReaction(Emoji.fromFormatted("<:pepeban:1083772052164579389>")).queue()

            return event.author.openPrivateChannel().complete().sendMessageEmbeds(
                ChannelUtil.embed("banned", userId = userId, placeHolders = placeHolder)
            ).queue()
        }

        event.channel.sendMessage(tagContent).queue()
    }
}