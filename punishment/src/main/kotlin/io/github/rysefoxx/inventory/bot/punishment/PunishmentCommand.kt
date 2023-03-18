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
import io.github.rysefoxx.inveneetory.bot.command.StringConstants
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.extension.ChannelUtil
import io.github.rysefoxx.inventory.bot.punishment.util.TimeUtil
import io.github.rysefoxx.inventory.bot.spring.model.entity.EmbedDataEntity
import io.github.rysefoxx.inventory.bot.spring.model.entity.PunishmentEntity
import io.github.rysefoxx.inventory.bot.spring.model.service.EmbedDataService
import io.github.rysefoxx.inventory.bot.spring.model.service.PunishmentService
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PunishmentCommand(

    @Autowired
    private val punishmentService: PunishmentService,

    @Autowired
    private val embedDataService: EmbedDataService,

    @Autowired
    private val languageDocument: LanguageDocument


) : SlashCommand() {

    override val name: String
        get() = "punishment"

    companion object {
        const val SYSTEM_OPTION_NAME = "system"
        const val USER_OPTION_NAME = "user"
        const val REASON_OPTION_NAME = "reason"
        const val TIME_OPTION_NAME = "time"
    }

    private val systems = arrayOf("ticket", "tag", "all")

    override fun command(): CommandData {
        return Commands.slash(name, "Punishes a user who has broken a rule.")
            .addOption(OptionType.USER, USER_OPTION_NAME, "Who should be punished?", true)
            .addOption(OptionType.STRING, SYSTEM_OPTION_NAME, "In what system should he be excluded?", true, true)
            .addOption(OptionType.STRING, REASON_OPTION_NAME, "Reason", true)
            .addOption(
                OptionType.STRING,
                TIME_OPTION_NAME,
                "For how long should he be banned? (e.g 1w, 2d, 3h, 4m, 5s)",
                true
            )
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val user = event.getOption(USER_OPTION_NAME)?.asUser ?: return
        val system = event.getOption(SYSTEM_OPTION_NAME)?.asString ?: return
        val reason = event.getOption(REASON_OPTION_NAME)?.asString ?: return
        val timeAsString = event.getOption(TIME_OPTION_NAME)?.asString ?: return

        if (reason.length > 50) {
            return event.reply(languageDocument.getTranslation("punishment_reason_too_long", user.idLong))
                .setEphemeral(true).queue()
        }

        if (user.isBot) {
            return event.reply(languageDocument.getTranslation("is_bot", user.idLong)).setEphemeral(true).queue()
        }

        if (user.isSystem) {
            return event.reply(languageDocument.getTranslation("is_system", user.idLong)).setEphemeral(true)
                .queue()
        }

        if (!systems.contains(system.lowercase())) {
            return event.reply(languageDocument.getTranslation("invalid_system", user.idLong)).setEphemeral(true)
                .queue()
        }

        if (punishmentService.isBanned(user.idLong, system)) {
            return event.reply(
                languageDocument.getTranslation(
                    "punishment_already_punished",
                    event.user.idLong,
                    listOf(user.asMention, punishmentService.punishTime(user.idLong, system))
                )
            ).setEphemeral(true).queue()
        }

        val time = TimeUtil.getCurrentTimePlusOffset(timeAsString)
            ?: return event.reply(languageDocument.getTranslation("punishment_invalid_time_format", user.idLong))
                .setEphemeral(true)
                .queue()

        val placeHolder = listOf(user.asMention, event.user.asMention, reason, system, time.toString())

        var embedData = EmbedDataEntity(data = placeHolder)
        embedData = embedDataService.saveData(embedData)

        val entity = PunishmentEntity(
            user.idLong,
            event.user.idLong,
            embedData.id,
            reason,
            system,
            time
        )

        punishmentService.savePunishment(entity)

        event.reply(languageDocument.getTranslation("punishment_execute", user.idLong, user.asMention))
            .setEphemeral(true).queue()

        ChannelUtil.embed(
            name, EnvironmentHolder.data.getProperty("BAN_LOG_ID"), event.jda, placeHolder, embedData.id.toString()
        )?.addActionRow(
            Button.secondary(StringConstants.EMBED_TRANSLATE_BUTTON, languageDocument.getDefaultTranslation("translate_button_label"))
        )?.queue()

    }

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val focusedOption = event.focusedOption.name

        if (focusedOption != SYSTEM_OPTION_NAME || event.name != name)
            return

        event.replyChoiceStrings(systems.filter { it.startsWith(event.focusedOption.value) }).queue()
    }
}