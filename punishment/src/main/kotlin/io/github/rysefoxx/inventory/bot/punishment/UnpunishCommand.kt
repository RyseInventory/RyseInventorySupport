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

package io.github.rysefoxx.inventory.bot.punishment

import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import io.github.rysefoxx.inventory.bot.spring.model.service.PunishmentService
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UnpunishCommand(

    @Autowired
    private val punishmentService: PunishmentService,

    @Autowired
    private val embedDataService: EmbedDataService,

    @Autowired
    private val languageDocument: LanguageDocument

) : SlashCommand() {

    override val name: String
        get() = "unpunish"

    companion object {
        const val SYSTEM_OPTION_NAME = "system"
        const val USER_OPTION_NAME = "user"
    }

    private val systems = arrayOf("ticket", "tag", "all")

    override fun command(): CommandData {
        return Commands.slash(name, "Removes a punishment.")
            .addOption(OptionType.USER, USER_OPTION_NAME, "Which player should have the punishment removed?", true)
            .addOption(
                OptionType.STRING,
                SYSTEM_OPTION_NAME,
                "In which system should the punishment be removed?",
                true,
                true
            )
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val user = event.getOption(USER_OPTION_NAME)?.asUser ?: return
        val system = event.getOption(SYSTEM_OPTION_NAME)?.asString ?: return

        if (user.isBot) {
            return event.reply(languageDocument.getTranslation("is_bot", user.idLong)).setEphemeral(true).queue()
        }

        if (user.isSystem) {
            return event.reply(languageDocument.getTranslation("is_system", user.idLong)).setEphemeral(true).queue()
        }

        if (!systems.contains(system.lowercase())) {
            return event.reply(languageDocument.getTranslation("invalid_system", user.idLong)).setEphemeral(true)
                .queue()
        }

        if (!punishmentService.isBanned(user.idLong, system)) {
            return event.reply(
                languageDocument.getTranslation(
                    "unpunishment_not_punished",
                    user.idLong,
                    user.asMention
                )
            ).setEphemeral(true).queue()
        }

        val entity = punishmentService.punishmentRepository.findByUserIdAndSystem(user.idLong, system).get()

        punishmentService.deletePunishment(entity)
        entity.embedId?.let { embedDataService.deleteData(it) }

        event.reply(
            languageDocument.getTranslation(
                "unpunishment_execute",
                user.idLong,
                user.asMention
            )
        ).setEphemeral(true).queue()
    }

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val focusedOption = event.focusedOption.name

        if (focusedOption != SYSTEM_OPTION_NAME || event.name != name)
            return

        event.replyChoiceStrings(systems.filter { it.startsWith(event.focusedOption.value) }).queue()
    }
}