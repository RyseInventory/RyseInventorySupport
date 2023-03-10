package io.github.rysefoxx.inventory.bot.core

import dev.minn.jda.ktx.jdabuilder.default
import io.github.rysefoxx.inventory.bot.core.spring.event.BootstrapReadyEvent
import io.github.rysefoxx.inventory.bot.core.spring.event.DiscordApplicationReadyEvent
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.core.Application
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
class DiscordApplication {

    @EventListener(BootstrapReadyEvent::class)
    @Order(2)
    fun start() {
        Logger.info("Starting RyseInventory Bot.")
        val jda = default(EnvironmentHolder.data.getProperty("TOKEN")) {
            setActivity(Activity.watching("RyseInventory Source Code"))
            setStatus(OnlineStatus.ONLINE)
            enableIntents(GatewayIntent.GUILD_MEMBERS)
            setMemberCachePolicy(MemberCachePolicy.ALL)
        }.awaitReady()
        Bootstrap.jda = jda
        Logger.info("RyseInventory Bot started.")

        Logger.info("Publishing event DiscordApplicationReadyEvent.")
        Application.context.publishEvent(DiscordApplicationReadyEvent())
    }
}