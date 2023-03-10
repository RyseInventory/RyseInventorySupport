package io.github.rysefoxx.inventory.bot.spring.model.service

import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.model.entity.EmbedDataEntity
import io.github.rysefoxx.inventory.bot.spring.model.entity.PunishmentEntity
import io.github.rysefoxx.inventory.bot.spring.model.entity.UserEntity
import io.github.rysefoxx.inventory.bot.spring.model.repository.EmbedDataRepository
import io.github.rysefoxx.inventory.bot.spring.model.repository.PunishmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class EmbedDataService(
    @Autowired
    private val embedDataRepository: EmbedDataRepository
) {

    fun saveData(embedDataEntity: EmbedDataEntity) : EmbedDataEntity {
        Logger.info("Saving embed-data: ${embedDataEntity.id}")
        return embedDataRepository.save(embedDataEntity)
    }

    fun deleteData(id: Long) {
        Logger.info("Removing embed-data: $id")
        embedDataRepository.deleteById(id)
        Logger.info("Removed embed-data: $id")
    }

    fun findById(id: Long): EmbedDataEntity? {
        return embedDataRepository.findEmbedDataEntityById(id)
    }
}