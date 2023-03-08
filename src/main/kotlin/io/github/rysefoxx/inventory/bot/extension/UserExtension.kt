package io.github.rysefoxx.inventory.bot.extension

import io.github.rysefoxx.inventory.bot.Bootstrap
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction


fun StringSelectInteractionEvent.replyTranslated(key: String): ReplyCallbackAction {
    val document = Bootstrap.context.getBean(LanguageDocument::class.java)
    return this.reply(document.getTranslation(key, this.member?.idLong ?: -1))
}

fun StringSelectInteractionEvent.replyTranslatedAndQueue(key: String) {
    replyTranslatedAndQueue(key, null)
}

fun StringSelectInteractionEvent.replyTranslatedAndQueue(key: String, placeHolder: Any?) {
    val document = Bootstrap.context.getBean(LanguageDocument::class.java)
    val memberId = this.member?.idLong ?: -1
    val translatedString = document.getTranslation(key, memberId).replace("{PH}", placeHolder?.toString() ?: "")
    translatedString.let {
        this.reply(it)
            .setEphemeral(true)
            .queue()
    }
}