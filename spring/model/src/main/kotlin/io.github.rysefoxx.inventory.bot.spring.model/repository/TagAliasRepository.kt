package io.github.rysefoxx.inventory.bot.spring.model.repository

import io.github.rysefoxx.inventory.bot.spring.model.entity.TagAliasEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagAliasRepository : JpaRepository<TagAliasEntity, Long> {

    fun findByAlias(alias: String): Optional<TagAliasEntity>

}