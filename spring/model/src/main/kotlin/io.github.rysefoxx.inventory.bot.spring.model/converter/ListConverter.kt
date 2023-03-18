package io.github.rysefoxx.inventory.bot.spring.model.converter

import javax.persistence.AttributeConverter

class ListConverter : AttributeConverter<List<*>, String> {
    override fun convertToDatabaseColumn(attribute: List<*>?): String {
        return attribute?.joinToString(separator = ";") ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<*> {
        return dbData?.split(";")?.map { it.trim() } ?: emptyList<Any>()
    }

}