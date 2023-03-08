package io.github.rysefoxx.inventory.bot.document

import io.github.rysefoxx.inventory.bot.model.service.UserService
import io.github.rysefoxx.inventory.bot.spring.event.BootstrapReadyEvent
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

    fun getTranslation(key: String, userId: Long): String {
        if(userId == -1L)
            return key

        val service = userService.getUserById(userId)
        val file = File("${service.language}.yml")

        if(!file.exists())
            return key

        val yaml = Yaml()

        val map = yaml.load<Map<String, Any>>(file.inputStream())
        val value = map[key] ?: return key

        return value.toString()
    }

    private fun copyFile(inputStream: InputStream, file: File) {
        inputStream.use { input ->
            file.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
    }

}