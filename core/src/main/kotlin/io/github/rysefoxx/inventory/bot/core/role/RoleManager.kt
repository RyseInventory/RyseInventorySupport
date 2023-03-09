package io.github.rysefoxx.inventory.bot.core.role

import io.github.rysefoxx.inventory.bot.core.Bootstrap
import io.github.rysefoxx.inventory.bot.core.document.EnvironmentHolder
import net.dv8tion.jda.api.entities.Role

object RoleManager {

    fun getRoleById(id: String): Role? {
        val list =
            Bootstrap.jda.getGuildById(EnvironmentHolder.data.getProperty("GUILD_ID"))?.run { roles.toMutableList() }
                ?: mutableListOf()

        return list.find { it.id == id }
    }
    fun getRoleByName(name: String): Role? {
        val list =
            Bootstrap.jda.getGuildById(EnvironmentHolder.data.getProperty("GUILD_ID"))?.run { roles.toMutableList() }
                ?: mutableListOf()

        return list.find { it.name.equals(name, true) }
    }
}