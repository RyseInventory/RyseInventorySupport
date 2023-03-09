package io.github.rysefoxx.inventory.bot.core.document

import io.github.rysefoxx.inventory.bot.core.extension.readTextOrNull
import io.github.rysefoxx.inventory.bot.core.model.service.UserService
import io.github.rysefoxx.inventory.bot.core.spring.event.BootstrapReadyEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream

@Component
class LanguageDocument(

    @Autowired
    val userService: UserService

) {

    private val languages = listOf(
        "german",
        "english"
    )

    @EventListener(BootstrapReadyEvent::class)
    @Order(3)
    fun loadAll() {
        languages.forEach {
            val resourcePath = "translation/${it}.yml"
            val file = File("${it}.yml")
            val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(resourcePath)

            inputStream?.let { stream -> copyFile(stream, file) }
        }
    }

    fun getTranslation(key: String, userId: Long?): String {
        val userLanguage = userId?.let { userService.getUserById(it).language } ?: return key

        return loadTranslations("$userLanguage.yml")?.get(key) as? String ?: key
    }

    @Suppress("UNCHECKED_CAST")
    fun getTranslationList(key: String, userId: Long?): List<String> {
        val userLanguage = userId?.let { userService.getUserById(it).language } ?: return listOf(key)

        return loadTranslations("${userLanguage}.yml")?.get(key) as? List<String> ?: listOf(key)
    }

    fun getDefaultTranslation(key: String): String {
        return loadTranslations("english.yml")?.get(key) as? String ?: key
    }

    @Suppress("UNCHECKED_CAST")
    fun getDefaultTranslationList(key: String): List<String> {
        return loadTranslations("english.yml")?.get(key) as? List<String> ?: listOf(key)
    }

    private fun loadTranslations(languageFile: String): Map<String, Any>? {
        val yaml = Yaml()

        return File(languageFile).readTextOrNull()?.let { yamlContent ->
            yaml.load(yamlContent)
        }
    }

    private fun copyFile(inputStream: InputStream, file: File) {
        inputStream.use { input ->
            file.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
    }

}