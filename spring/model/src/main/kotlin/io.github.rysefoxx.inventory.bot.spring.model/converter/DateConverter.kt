package io.github.rysefoxx.inventory.bot.spring.model.converter

import java.time.Instant
import java.util.Date
import javax.persistence.AttributeConverter

class DateConverter : AttributeConverter<Date, String> {
    override fun convertToDatabaseColumn(attribute: Date?): String {
        return attribute?.let { Instant.ofEpochMilli(it.time).toString() } ?: "null"
    }

    override fun convertToEntityAttribute(dbData: String?): Date {
        return dbData?.let { Date.from(Instant.parse(dbData)) } ?: Date()
    }
}