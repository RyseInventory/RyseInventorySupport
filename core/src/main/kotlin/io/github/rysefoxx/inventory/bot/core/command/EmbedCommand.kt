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

package io.github.rysefoxx.inventory.bot.core.command

import dev.minn.jda.ktx.interactions.components.StringSelectMenu
import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import io.github.rysefoxx.inveneetory.bot.command.StringConstants
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.extension.ChannelUtil
import net.dv8tion.jda.api.Permission
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

@Component
class EmbedCommand(

    @Autowired
    private val languageDocument: LanguageDocument

) : SlashCommand() {

    override val name: String
        get() = "embed"

    companion object {
        const val EMBED_NAME_OPTION_NAME = "embed-name"
    }

    private val words = arrayOf("rules", "language", "roles")

    override fun command(): CommandData {
        return Commands.slash(name, "Posts a predefined embed message.")
            .addOption(
                OptionType.STRING,
                EMBED_NAME_OPTION_NAME,
                "The embed name for the predefined message",
                true,
                true
            )
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val option = event.getOption(EMBED_NAME_OPTION_NAME) ?: return

        val placeHolder = mutableListOf<String>()

        if(option.asString.lowercase() == "rules") {
            placeHolder.add(EnvironmentHolder.data.getProperty("REPORT_ID"))
        }

        event.replyEmbeds(ChannelUtil.embed(option.asString.lowercase(), placeHolders = placeHolder))
            .addActionRow(
                when (option.asString.lowercase()) {
                    "rules" -> rulesSelection()
                    "language" -> languageSelection()
                    "roles" -> rolesSelection()
                    else -> return
                }
            )
            .addActionRow(
                Button.secondary(StringConstants.EMBED_TRANSLATE_BUTTON, languageDocument.getDefaultTranslation("translate_button_label"))
            )
            .queue()
    }

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val focusedOption = event.focusedOption.name
        if (focusedOption != EMBED_NAME_OPTION_NAME || event.name != name)
            return

        event.replyChoiceStrings(words.filter { it.startsWith(event.focusedOption.value) }).queue()
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