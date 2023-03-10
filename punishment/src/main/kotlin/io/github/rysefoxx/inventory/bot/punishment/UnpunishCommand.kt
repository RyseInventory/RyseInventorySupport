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

        event.replyChoiceStrings(systems.filter { it.startsWith(focusedOption) }).queue()
    }
}