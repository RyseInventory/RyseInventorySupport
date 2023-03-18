package io.github.rysefoxx.inventory.bot.spring.model.repository

import io.github.rysefoxx.inventory.bot.spring.model.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagRepository : JpaRepository<TagEntity, String> {

    fun findByName(name: String): Optional<TagEntity>

}