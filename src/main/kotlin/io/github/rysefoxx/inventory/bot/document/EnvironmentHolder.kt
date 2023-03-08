package io.github.rysefoxx.inventory.bot.document

import io.github.rysefoxx.inventory.bot.Bootstrap
import io.github.rysefoxx.inventory.bot.log.Logger
import io.github.rysefoxx.inventory.bot.spring.event.BootstrapReadyEvent
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
            val url = Bootstrap.instance.javaClass.classLoader.getResourceAsStream(".env") ?: return null
            val prop = Properties()
            prop.load(url)
            prop
        } catch (ex: IOException) {
            Logger.error("Could not load resource .env", ex)
            null
        }
    }

}