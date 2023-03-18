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

package io.github.rysefoxx.inventory.bot.core.manager

import io.github.rysefoxx.inventory.bot.core.Bootstrap
import io.github.rysefoxx.inventory.bot.document.EnvironmentHolder
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