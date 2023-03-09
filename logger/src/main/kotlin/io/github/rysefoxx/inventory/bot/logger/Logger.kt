package io.github.rysefoxx.inventory.bot.logger

import org.slf4j.LoggerFactory

object Logger {
    private val log: org.slf4j.Logger = LoggerFactory.getLogger(Logger::class.java)

    fun info(message: String) = log.info(message)
    fun warn(message: String) = log.warn(message)
    fun error(message: String) = log.error(message)
    fun error(message: String, exception: Exception) = log.error(message, exception)
    fun debug(message: String) = log.debug(message)

}