package io.github.rysefoxx.inventory.bot.core.command.impl

import dev.minn.jda.ktx.interactions.components.ButtonDefaults
import dev.minn.jda.ktx.interactions.components.StringSelectMenu
import dev.minn.jda.ktx.messages.Embed
import io.github.rysefoxx.inventory.bot.core.command.SlashCommand
import io.github.rysefoxx.inventory.bot.core.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.core.util.StringConstants
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.awt.Color
import java.util.UUID

@Component
class EmbedCommand(

    @Autowired
    private val languageDocument: LanguageDocument

) : SlashCommand() {

    private val words = arrayOf("rules", "language", "roles")
    override fun command(): CommandData {
        return Commands.slash("embed", "Postet eine vordefinierte embed Nachricht.")
            .addOption(OptionType.STRING, "embed-name", "Der Embed Name für die vordefinierte Nachricht", true, true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val option = event.getOption("embed-name") ?: return

        event.replyEmbeds(embed(option.asString.lowercase()))
            .addActionRow(
                when (option.asString.lowercase()) {
                    "rules" -> rulesSelection()
                    "language" -> languageSelection()
                    "roles" -> rolesSelection()
                    else -> return
                }
            )
            .addActionRow(translateButton())
            .queue()
    }

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        if (event.focusedOption.name != "embed-name")
            return

        event.replyChoiceStrings(words.filter { it.startsWith(event.focusedOption.value) }).queue()
    }

    private fun translateButton(): Button {
        return Button.secondary("${UUID.randomUUID()}_${StringConstants.EMBED_TRANSLATE_BUTTON}", "Translate")
    }

    private fun embed(key: String): MessageEmbed {
        return Embed {
            title = languageDocument.getDefaultTranslation("${key}_embed_title")
            description = languageDocument.getDefaultTranslationList("${key}_embed_description")
                .joinToString("\n") { it }
            color = 0xA87406
            footer(key)
        }
    }

    private fun languageSelection(): StringSelectMenu {
        return StringSelectMenu(StringConstants.SELECT_MENU_LANGUAGE) {
            placeholder = languageDocument.getDefaultTranslation("language_select_menu_placeholder")
            addOptions(
                SelectOption.of(
                    languageDocument.getDefaultTranslation("language_select_menu_selection_label"),
                    StringConstants.SELECT_MENU_OPTION_LANGUAGE
                )
                    .withDescription(languageDocument.getDefaultTranslation("language_select_menu_selection_description"))
                    .withDefault(true),
                SelectOption.of(
                    languageDocument.getDefaultTranslation("language_select_menu_english_label"),
                    StringConstants.SELECT_MENU_LANGUAGE_ENGLISH
                )
                    .withDescription(languageDocument.getDefaultTranslation("language_select_menu_english_description"))
                    .withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")),
                SelectOption.of(
                    languageDocument.getDefaultTranslation("language_select_menu_german_label"),
                    StringConstants.SELECT_MENU_LANGUAGE_GERMAN
                )
                    .withDescription(languageDocument.getDefaultTranslation("language_select_menu_german_description"))
                    .withEmoji(Emoji.fromUnicode("\uD83C\uDDE9\uD83C\uDDEA"))
            )
        }
    }

    private fun rolesSelection(): StringSelectMenu {
        return StringSelectMenu(StringConstants.SELECT_MENU_ROLE) {
            placeholder = languageDocument.getDefaultTranslation("roles_select_menu_placeholder")
            addOptions(
                SelectOption.of(
                    languageDocument.getDefaultTranslation("roles_select_menu_selection_label"),
                    StringConstants.SELECT_MENU_OPTION_ROLE
                )
                    .withDescription(languageDocument.getDefaultTranslation("roles_select_menu_selection_description"))
                    .withDefault(true),
                SelectOption.of(
                    languageDocument.getDefaultTranslation("roles_select_menu_java_label"),
                    StringConstants.SELECT_MENU_ROLE_JAVA
                )
                    .withDescription(languageDocument.getDefaultTranslation("roles_select_menu_java_description"))
                    .withEmoji(Emoji.fromFormatted("<:java:1082796411948638328>")),
                SelectOption.of(
                    languageDocument.getDefaultTranslation("roles_select_menu_kotlin_label"),
                    StringConstants.SELECT_MENU_ROLE_KOTLIN
                )
                    .withDescription(languageDocument.getDefaultTranslation("roles_select_menu_kotlin_description"))
                    .withEmoji(Emoji.fromFormatted("<:kotlin:1082795811760508938>")),
            )
        }
    }

    private fun rulesSelection(): StringSelectMenu {
        return StringSelectMenu(StringConstants.SELECT_MENU_RULES) {
            placeholder = languageDocument.getDefaultTranslation("rules_select_menu_placeholder")
            addOptions(
                SelectOption.of(
                    languageDocument.getDefaultTranslation("rules_select_menu_selection_label"),
                    StringConstants.SELECT_MENU_OPTION_RULES
                )
                    .withDescription("Select an option. You can cancel the setting at any time.")
                    .withDefault(true),
                SelectOption.of(
                    languageDocument.getDefaultTranslation("rules_select_menu_agree_label"),
                    StringConstants.SELECT_MENU_RULES_AGREE
                )
                    .withDescription(languageDocument.getDefaultTranslation("rules_select_menu_agree_description"))
                    .withEmoji(Emoji.fromFormatted("<:pepeyes:1083038431770312849>")),
                SelectOption.of(
                    languageDocument.getDefaultTranslation("rules_select_menu_disagree_label"),
                    StringConstants.SELECT_MENU_RULES_DISAGREE
                )
                    .withDescription(languageDocument.getDefaultTranslation("rules_select_menu_disagree_description"))
                    .withEmoji(Emoji.fromFormatted("<:pepeno:1083038445393412148>")),
            )
        }
    }
}