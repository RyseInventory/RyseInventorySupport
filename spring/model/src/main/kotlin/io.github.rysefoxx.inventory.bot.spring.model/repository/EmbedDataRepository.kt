package io.github.rysefoxx.inventory.bot.spring.model.repository

import io.github.rysefoxx.inventory.bot.spring.model.entity.EmbedDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmbedDataRepository : JpaRepository<EmbedDataEntity, Long> {

    fun findEmbedDataEntityById(id: Long): EmbedDataEntity?

}