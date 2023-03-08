package io.github.rysefoxx.inventory.bot.command.impl

import io.github.rysefoxx.inventory.bot.command.SlashCommand
import io.github.rysefoxx.inventory.bot.util.StringConstants
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import org.springframework.stereotype.Component
import java.awt.Color

@Component
class EmbedCommand : SlashCommand() {

    private val words = arrayOf("rules", "language", "roles")
    override fun command(): CommandData {
        return Commands.slash("embed", "Postet eine vordefinierte embed Nachricht.")
            .addOption(OptionType.STRING, "embed-name", "Der Embed Name für die vordefinierte Nachricht", true, true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val option = event.getOption("embed-name") ?: return

        when (option.asString.lowercase()) {
            "rules" -> {
                event.replyEmbeds(rulesEmbed().build())
                    .addActionRow(rulesSelection())
                    .queue()
            }

            "language" -> {
                event.replyEmbeds(languageEmbed().build())
                    .addActionRow(languageSelection())
                    .queue()
            }

            "roles" -> {
                event.replyEmbeds(rolesEmbed().build())
                    .addActionRow(rolesSelection())
                    .queue()
            }
        }
    }

    override fun onAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        if (event.focusedOption.name != "embed-name")
            return

        event.replyChoiceStrings(words.filter { it.startsWith(event.focusedOption.value) }).queue()
    }

    private fun rulesEmbed(): EmbedBuilder {
        return EmbedBuilder()
            .setTitle("\uD83D\uDED1 Rules for the Discord Server")
            .setDescription(
                """
                > **1. Respect every player on this server**
                > **2. NSFW content has nothing to do on this server**
                > **3. Advertising will be allowed only in the form of Github link and in conjunction with RyseInventory**
                > **4. Spamming is undesirable and may be punished with a timeout**
                > **5. Don't bother users with a RyseInventory problem via private message**
                > **6. The highlighting of names in the member list (e.g. !Rysefoxx) is prohibited.**
                
                <:discord:1083031502004162622> __Discord ToS:__ https://discord.com/terms
                <:discord:1083031502004162622> __Discord Guidelines:__ https://discord.com/guidelines
                
                > **If a player breaks the rules from your point of view, we ask you to report it immediately via the RyseInventory bot.**
                
                > _ Note that Discord is only allowed from a certain age depending on the country. So think twice before revealing your age._
                
                Depending on the violation, the team takes the right to punish you as follows:
                > **System Abuse (abusive tickets, bug reports, or reporting players incorrectly)**
                > | A (temporary) exclusion will occur. You will no longer be able to use the system
                > **Server exclusion**
                > **Timeout**
                
                If you agree with everything, you can confirm below. You will be unlocked.
            """.trimIndent()
            )
            .setColor(Color.getColor("#a87406"))
    }

    private fun rulesSelection(): StringSelectMenu {
        return StringSelectMenu.create(StringConstants.SELECT_MENU_RULES)
            .setPlaceholder("Do you agree with the rules?")
            .addOptions(
                SelectOption.of("Selection", StringConstants.SELECT_MENU_OPTION_RULES)
                    .withDescription("Select an option. You can cancel the setting at any time.")
                    .withDefault(true),
                SelectOption.of("I agree", StringConstants.SELECT_MENU_RULES_AGREE)
                    .withDescription("You agree to the rules and receive all channel.")
                    .withEmoji(Emoji.fromFormatted("<:pepeyes:1083038431770312849>")),
                SelectOption.of("I disagree", StringConstants.SELECT_MENU_RULES_DISAGREE)
                    .withDescription("You do not agree to the rules and lose all channels.")
                    .withEmoji(Emoji.fromFormatted("<:pepeno:1083038445393412148>")),
            )
            .build()
    }

    private fun languageEmbed(): EmbedBuilder {
        return EmbedBuilder()
            .setTitle("\uD83C\uDDFA\uD83C\uDDF8 Languages for the Discord Bot \uD83C\uDDE9\uD83C\uDDEA")
            .setDescription(
                """
               The bot offers you the possibility to translate the messages into your language. Note that not every language is available.

                **These languages are currently available:**
                > German 🇩🇪
                > English 🇺🇸
                
                **Your language is not included?**
                > If you would like to have your native language integrated, I would ask you to translate the [translation file](https://github.com/RyseInventory/RyseInventorySupport) into your language. Then you can create a [PR](https://github.com/RyseInventory/RyseInventorySupport/pulls).
                > I will accept your PullRequest in time and at the next bot restart, your language is implemented.
                
                **My language is not integrated and I have no idea about programming**
                > If you are not able to create a [PR](https://github.com/RyseInventory/RyseInventorySupport/pulls) yourself, please send the translated text to the team. The developer will integrate this language for you.
                
                **Not all messages are translated**
                > Please note that it is not possible to translate everything. All private embeds/messages will be translated.
                > Public embeds/messages will be available in **English** language only. 
                
                **How to change the language**
                > Very simple, select under the message the menu and then your language.
                
                **Translation error**
                > Translation errors may well occur. I would ask you to fix this yourself and report it via [PR](https://github.com/RyseInventory/RyseInventorySupport/pulls). If this does not work, I would ask you to inform a team member so that a developer can make the change.
                
                Suggestions for improvement are welcome :)     
            """.trimIndent()
            )
            .setColor(Color.getColor("#a87406"))
    }

    private fun languageSelection(): StringSelectMenu {
        return StringSelectMenu.create(StringConstants.SELECT_MENU_LANGUAGE)
            .setPlaceholder("Which language do you choose?")
            .addOptions(
                SelectOption.of("Selection", StringConstants.SELECT_MENU_OPTION_LANGUAGE)
                    .withDescription("Choose a language. You can change it at any time.")
                    .withDefault(true),
                SelectOption.of("English", StringConstants.SELECT_MENU_LANGUAGE_ENGLISH)
                    .withDescription("The messages are then in English.")
                    .withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")),
                SelectOption.of("German", StringConstants.SELECT_MENU_LANGUAGE_GERMAN)
                    .withDescription("The messages are then in German.")
                    .withEmoji(Emoji.fromUnicode("\uD83C\uDDE9\uD83C\uDDEA")),
            )
            .build()
    }

    private fun rolesEmbed(): EmbedBuilder {
        return EmbedBuilder()
            .setTitle("<:java:1082796411948638328> Manage your roles <:kotlin:1082795811760508938>")
            .setDescription(
                """
               Want to customize your roles the way you customize languages? No problem. Here you can remove or add all possible roles

                **What kind of roles can I add/remove?**
                > You can add or remove everything from programming roles to the Announcment role.
                
                **You wish for a role?**
                > No problem. Create a corresponding request via the bot. The team decides internally if the role makes sense. If yes, a vote will be opened where you can decide again if you really want to have it.
                
                **Functionality of individual roles**
                > _You can (de)activate this functionality as you wish._
                --------------------------------------------------------------------------
                > Programming role: If a thread is opened with the tag 'Java' and you have the Java role, you can set whether you want to be pinged.
                
                **I have more ideas for functionalities**
                > Very cool! We are always happy when users want to integrate their own ideas.
                > The procedure is the same as for the section **You wish for a role?**. The team first decides to what extent the functionality in the role makes sense. If the functionality convinces us, we will open a vote here, where you can vote again.  
            """.trimIndent()
            )
            .setColor(Color.getColor("#a87406"))
    }

    private fun rolesSelection(): StringSelectMenu {
        return StringSelectMenu.create(StringConstants.SELECT_MENU_ROLE)
            .setPlaceholder("What roles would you like to have?")
            .addOptions(
                SelectOption.of("Selection", StringConstants.SELECT_MENU_OPTION_ROLE)
                    .withDescription("Choose a role. You can remove it at any time.")
                    .withDefault(true),
                SelectOption.of("Java", StringConstants.SELECT_MENU_ROLE_JAVA)
                    .withDescription("Removes or adds the Java role.")
                    .withEmoji(Emoji.fromFormatted("<:java:1082796411948638328>")),
                SelectOption.of("Kotlin", StringConstants.SELECT_MENU_ROLE_KOTLIN)
                    .withDescription("Removes or adds the Kotlin role.")
                    .withEmoji(Emoji.fromFormatted("<:kotlin:1082795811760508938>")),
            )
            .build()
    }
}