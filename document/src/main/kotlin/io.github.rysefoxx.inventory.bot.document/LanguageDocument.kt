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

package io.github.rysefoxx.inventory.bot.document

import io.github.rysefoxx.inventory.bot.core.spring.event.BootstrapReadyEvent
import io.github.rysefoxx.inventory.bot.spring.model.service.UserService
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

    private val yaml = Yaml()

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

    fun getTranslation(key: String, userId: Long?, placeHolder: List<String?>? = null): String {
        val userLanguage = userId?.let { userService.getUserById(it).language } ?: return key
        var value = when (val translation = loadTranslations("${userLanguage}.yml")?.get(key)) {
            is String -> translation
            is List<*> -> translation.joinToString("\n") { it.toString() }
            else -> key
        }

        placeHolder?.forEachIndexed() { index, ph ->
            value = ph?.let { value.replaceFirst("{$index}", it) } ?: value
        }

        return value
    }

    fun getTranslation(key: String, userId: Long?, placeHolder: String? = null): String {
        return getTranslation(key, userId, listOf(placeHolder))
    }

    fun getTranslation(key: String, userId: Long?): String {
        return getTranslation(key, userId, listOf(null))
    }

    @Suppress("UNCHECKED_CAST")
    fun getTranslationList(key: String, userId: Long?, placeHolder: List<String>? = null): List<String> {
        val userLanguage = userId?.let { userService.getUserById(it).language } ?: return listOf(key)

        val result = (loadTranslations("${userLanguage}.yml")?.get(key) as? List<String> ?: listOf(key)).toMutableList()

        if (placeHolder != null) {
            var i = 0
            result.forEachIndexed { index, value ->
                when {
                    value.contains("{$i}") && placeHolder.size > i -> {
                        result[index] = value.replaceFirst("{$i}", placeHolder[i])
                        i++
                    }
                }
            }
        }

        return result
    }

    fun getDefaultTranslation(key: String, placeHolder: String? = null): String {
        return when (val translation = loadTranslations("english.yml")?.get(key)) {
            is String -> translation.replaceFirst("{0}", placeHolder ?: "{0}")
            is List<*> -> translation.joinToString("\n") { it.toString().replaceFirst("{0}", placeHolder ?: "{0}") }
            else -> key
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getDefaultTranslationList(key: String): List<String> {
        return loadTranslations("english.yml")?.get(key) as? List<String> ?: listOf(key)
    }

    private fun loadTranslations(languageFile: String): Map<String, Any>? {
        return readTextOrNull(File(languageFile))?.let { yamlContent ->
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

    private fun readTextOrNull(file: File): String? =
        file.takeIf { it.exists() }?.inputStream()?.bufferedReader()?.use { it.readText() }

}