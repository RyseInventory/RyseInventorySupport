package io.github.rysefoxx.inventory.bot.document

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop
import io.github.rysefoxx.inventory.bot.Bootstrap
import io.github.rysefoxx.inventory.bot.log.Logger
import io.github.rysefoxx.inventory.bot.spring.event.BootstrapReadyEvent
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.util.Properties

@Component
object EnvironmentHolder {

    var data = Properties()

    @Suppress("UNCHECKED_CAST")
    @EventListener(BootstrapReadyEvent::class)
    @Order(1)
    fun load() {
        Logger.info("Loading environment variables.")
        try {
            val properties = getResource(Bootstrap.instance) ?: return
            data = properties
        } catch (e: IOException) {
            Logger.error("Could not load resource .env", e)
            return
        }
        Logger.info("Loaded environment variables.")
    }

    private fun getResource(main: Bootstrap): Properties? {
        return try {
            val url = main.javaClass.classLoader.getResourceAsStream(".env") ?: return null
            val prop = Properties()
            prop.load(url)
            prop
        } catch (ex: IOException) {
            Logger.error("Could not load resource .env", ex)
            null
        }
    }

}