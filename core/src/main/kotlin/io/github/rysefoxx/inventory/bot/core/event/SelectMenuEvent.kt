package io.github.rysefoxx.inventory.bot.core.event

import io.github.rysefoxx.inveneetory.bot.command.StringConstants
import io.github.rysefoxx.inventory.bot.core.manager.RoleManager
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.document.LanguageDocument
import io.github.rysefoxx.inventory.bot.spring.model.service.UserService
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class SelectMenuEvent(

    @Autowired
    private val userService: UserService,

    @Autowired
    private val languageDocument: LanguageDocument

) : ListenerAdapter() {

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        when (event.componentId) {
            StringConstants.SELECT_MENU_LANGUAGE -> {
                if (event.values[0].equals(StringConstants.SELECT_MENU_OPTION_LANGUAGE)) {
                    event.deferEdit().queue()
                    return
                }

                val user = userService.getUserById(event.user.idLong)
                user.language = event.values[0].split("-")[1]
                userService.saveUser(user)

                event.reply(languageDocument.getTranslation("language_select", event.user.idLong, user.language))
                    .setEphemeral(true)
                    .queue()
            }

            StringConstants.SELECT_MENU_RULES -> {
                if (event.values[0].equals(StringConstants.SELECT_MENU_OPTION_RULES)) {
                    event.deferEdit().queue()
                    return
                }

                val value = event.values[0]
                val role = RoleManager.getRoleById(EnvironmentHolder.data.getProperty("RULES_ACCEPTED_ID"))

                if (value == StringConstants.SELECT_MENU_RULES_AGREE && role != null) {
                    if (event.member?.roles?.contains(role) == true) {
                        event.reply(languageDocument.getTranslation("rule_already_agreed", event.user.idLong))
                            .setEphemeral(true)
                            .queue()
                        return
                    }
                    event.guild?.addRoleToMember(event.user, role)?.queue()
                    event.reply(languageDocument.getTranslation("rule_agree", event.user.idLong))
                        .setEphemeral(true)
                        .queue()
                } else if (value == StringConstants.SELECT_MENU_RULES_DISAGREE && role != null) {
                    if (event.member?.roles?.contains(role) == false) {
                        event.reply(languageDocument.getTranslation("rule_not_agreed", event.user.idLong))
                            .setEphemeral(true)
                            .queue()
                        return
                    }
                    event.guild?.removeRoleFromMember(event.user, role)?.queue()
                    event.reply(languageDocument.getTranslation("rule_disagree", event.user.idLong))
                        .setEphemeral(true)
                        .queue()
                }
            }

            StringConstants.SELECT_MENU_ROLE -> {
                if (event.values[0].equals(StringConstants.SELECT_MENU_OPTION_ROLE)) {
                    event.deferEdit().queue()
                    return
                }

                val roleName = event.values[0].split("-")[1].replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                val role = RoleManager.getRoleByName(roleName)

                if (role != null) {
                    if (event.member?.roles?.contains(role) == true) {
                        event.guild?.removeRoleFromMember(event.user, role)?.queue()
                        event.reply(languageDocument.getTranslation("role_remove", event.user.idLong, roleName))
                            .setEphemeral(true)
                            .queue()
                        return
                    }
                    event.guild?.addRoleToMember(event.user, role)?.queue()
                    event.reply(languageDocument.getTranslation("role_apply", event.user.idLong, roleName))
                        .setEphemeral(true)
                        .queue()
                }
            }
        }
    }
}