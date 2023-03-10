package io.github.rysefoxx.inventory.bot.extension

import java.time.Instant
import java.util.*


fun Date.toDatabaseString(): String {
    return Instant.ofEpochMilli(this.time).toString()
}