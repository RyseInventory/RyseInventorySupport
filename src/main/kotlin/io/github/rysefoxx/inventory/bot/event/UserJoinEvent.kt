package io.github.rysefoxx.inventory.bot.event

import io.github.rysefoxx.inventory.bot.log.Logger
import io.github.rysefoxx.inventory.bot.model.entity.UserEntity
import io.github.rysefoxx.inventory.bot.model.service.UserService
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserJoinEvent(

    @Autowired
    val userService: UserService

) : ListenerAdapter() {

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        Logger.info("New user joined: " + event.user.id)

        val entity = UserEntity(event.user.idLong)
        userService.saveUser(entity)
    }
}