package io.github.rysefoxx.inventory.bot.document

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.rysefoxx.inventory.bot.Main
import io.github.rysefoxx.inventory.bot.log.Logger
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object EnvironmentHolder {

    var data = mutableMapOf<String, String>()

    @Suppress("UNCHECKED_CAST")
    fun load(main: Main) {
        Logger.info("Loading environment variables.")
        try {
            getResource(main).use {
                val mapper = ObjectMapper()
                data = mapper.readValue(it, MutableMap::class.java) as MutableMap<String, String>
            }
        } catch (e: IOException) {
            Logger.error("Could not load resource env.json", e)
            return
        }
        Logger.info("Loaded environment variables.")
    }

    private fun getResource(main: Main): InputStream? {
        return try {
            val url = main.javaClass.classLoader.getResource("env.json") ?: return null
            val connection = url.openConnection()
            connection.inputStream
        } catch (ex: IOException) {
            Logger.error("Could not load resource env.json", ex)
            null
        }
    }

}