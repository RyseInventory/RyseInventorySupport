package io.github.rysefoxx.inventory.bot.spring.model.converter

import io.github.rysefoxx.inventory.bot.spring.model.entity.TagEntity
import javax.persistence.AttributeConverter

class TagConverter : AttributeConverter<TagEntity, String> {
    override fun convertToDatabaseColumn(attribute: TagEntity?): String {
        return attribute?.serializeToString() ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): TagEntity {
        return dbData?.let { TagEntity.deserializeFromString(it) } ?: TagEntity()
    }

}