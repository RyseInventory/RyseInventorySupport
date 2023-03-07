package io.github.rysefoxx.inventory.bot

import dev.minn.jda.ktx.jdabuilder.default
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
import io.github.rysefoxx.inventory.bot.log.Logger
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity

fun main() {
    Main()
}

class Main {

    init {
        EnvironmentHolder.load(this)

        Logger.info("Starting RyseInventory Bot.")
        val jda = default(EnvironmentHolder.data["TOKEN"] ?: "") {
            setActivity(Activity.watching("RyseInventory Source Code"))
            setStatus(OnlineStatus.ONLINE)
        }.awaitReady()
        Logger.info("RyseInventory Bot started.")
    }
}