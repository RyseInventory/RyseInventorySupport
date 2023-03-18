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

import io.github.rysefoxx.inveneetory.bot.command.SlashCommand
import io.github.rysefoxx.inventory.bot.core.Bootstrap
import io.github.rysefoxx.inventory.bot.core.spring.event.DiscordApplicationReadyEvent
import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.core.Application
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
object SlashCommandManager : ListenerAdapter() {

    private val commands = mutableMapOf<String, SlashCommand>()

    @EventListener(DiscordApplicationReadyEvent::class)
    @Order(1)
    fun registerAll() {
        findAllCommands()
        addCommands()
    }

    private fun findAllCommands() {
        Logger.info("Trying to find all commands.")
        Application.context.getBeansOfType(SlashCommand::class.java).values.forEach {
            Logger.info("Found command [${it.command().name}|${it.javaClass.simpleName}]")
            commands[it.command().name] = it
        }
    }

    private fun addCommands() {
        Logger.info("Trying to register ${commands.size} commands.")
        Bootstrap.jda.updateCommands().addCommands(commands.values.map { it.command() }).queue {
            Logger.info("Registered all commands.")
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = commands[event.fullCommandName.split(" ")[0]] ?: return
        command.onSlashCommandInteraction(event)
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val command = commands[event.fullCommandName.split(" ")[0]] ?: return
        command.onAutoCompleteInteraction(event)
    }
}