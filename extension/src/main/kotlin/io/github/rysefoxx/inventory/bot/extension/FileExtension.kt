package io.github.rysefoxx.inventory.bot.extension

import java.io.File

fun File.readTextOrNull(): String? = takeIf { it.exists() }?.inputStream()?.bufferedReader()?.use { it.readText() }
