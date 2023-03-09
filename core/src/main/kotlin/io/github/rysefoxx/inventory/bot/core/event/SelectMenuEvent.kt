package io.github.rysefoxx.inventory.bot.core.event

import io.github.rysefoxx.inventory.bot.core.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.core.extension.replyTranslatedAndQueue
import io.github.rysefoxx.inventory.bot.core.model.service.UserService
import io.github.rysefoxx.inventory.bot.core.role.RoleManager
import io.github.rysefoxx.inventory.bot.core.util.StringConstants
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class SelectMenuEvent(

    @Autowired
    private val userService: UserService

) : ListenerAdapter() {

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        when (event.componentId) {
            StringConstants.SELECT_MENU_LANGUAGE -> {
                if(event.values[0].equals(StringConstants.SELECT_MENU_OPTION_LANGUAGE)) {
                    return
                }

                val user = userService.getUserById(event.user.idLong)
                user.language = event.values[0].split("-")[1]
                userService.saveUser(user)

                event.replyTranslatedAndQueue("language_select", user.language)
            }

            StringConstants.SELECT_MENU_RULES -> {
                if(event.values[0].equals(StringConstants.SELECT_MENU_OPTION_RULES)) {
                    return
                }

                val value = event.values[0]
                val role = RoleManager.getRoleById(EnvironmentHolder.data.getProperty("RULES_ACCEPTED_ID"))

                if (value == StringConstants.SELECT_MENU_RULES_AGREE && role != null) {
                    if(event.member?.roles?.contains(role) == true) {
                        event.replyTranslatedAndQueue("rule_already_agreed")
                        return
                    }
                    event.guild?.addRoleToMember(event.user, role)?.queue()
                    event.replyTranslatedAndQueue("rule_agree")
                } else if (value == StringConstants.SELECT_MENU_RULES_DISAGREE && role != null) {
                    if(event.member?.roles?.contains(role) == false) {
                        event.replyTranslatedAndQueue("rule_not_agreed")
                        return
                    }
                    event.guild?.removeRoleFromMember(event.user, role)?.queue()
                    event.replyTranslatedAndQueue("rule_disagree")
                }
            }

            StringConstants.SELECT_MENU_ROLE -> {
                if(event.values[0].equals(StringConstants.SELECT_MENU_OPTION_ROLE)) {
                    return
                }

                val roleName = event.values[0].split("-")[1].replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                val role = RoleManager.getRoleByName(roleName)

                if (role != null) {
                    if(event.member?.roles?.contains(role) == true) {
                        event.guild?.removeRoleFromMember(event.user, role)?.queue()
                        event.replyTranslatedAndQueue("role_remove", roleName)
                        return
                    }
                    event.guild?.addRoleToMember(event.user, role)?.queue()
                    event.replyTranslatedAndQueue("role_apply", roleName)
                }
            }
        }
    }
}