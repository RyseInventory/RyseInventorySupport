package io.github.rysefoxx.inventory.bot.document

import io.github.rysefoxx.inventory.bot.core.spring.event.BootstrapReadyEvent
import io.github.rysefoxx.inventory.bot.logger.Logger
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

@Component
object EnvironmentHolder {

    var data = Properties()

    @Suppress("UNCHECKED_CAST")
    @EventListener(BootstrapReadyEvent::class)
    @Order(1)
    fun load() {
        Logger.info("Loading environment variables.")
        try {
            val properties = getResource() ?: return
            data = properties
        } catch (e: IOException) {
            Logger.error("Could not load resource .env", e)
            return
        }
        Logger.info("Loaded environment variables.")
    }

    private fun getResource(): Properties? {
        return try {
            val url = this.javaClass.classLoader.getResourceAsStream(".env") ?: null
            val prop = Properties()
            prop.load(url)
            prop
        } catch (ex: IOException) {
            Logger.error("Could not load resource .env", ex)
            null
        }
    }

}