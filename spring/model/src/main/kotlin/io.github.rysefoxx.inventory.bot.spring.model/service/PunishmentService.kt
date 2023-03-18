package io.github.rysefoxx.inventory.bot.spring.model.service

import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.model.entity.PunishmentEntity
import io.github.rysefoxx.inventory.bot.spring.model.repository.PunishmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PunishmentService(
    @Autowired
    val punishmentRepository: PunishmentRepository
) {

    fun banModel(userId: Long, system: String): PunishmentEntity {
        return punishmentRepository.findByUserIdAndSystem(userId, system).orElse(null)
    }

    fun isBanned(userId: Long, system: String): Boolean {
        val entity = punishmentRepository.findByUserIdAndSystem(userId, system).orElse(null) ?: return false
        val time = entity.date?.time ?: 0

        return time == -1L || time > System.currentTimeMillis()
    }

    fun punishTime(userId: Long, system: String): String {
        val entity = punishmentRepository.findByUserIdAndSystem(userId, system).orElse(null) ?: return "false"
        return entity.date.toString()
    }

    fun savePunishment(punishmentEntity: PunishmentEntity) {
        Logger.info("Saving punishment: $punishmentEntity")
        punishmentRepository.save(punishmentEntity)
        Logger.info("Saved punishment: $punishmentEntity")
    }

    fun deletePunishment(punishmentEntity: PunishmentEntity) {
        val id = punishmentEntity.id
        val system = punishmentEntity.system

        Logger.info("Removing punishment: ${id}_$system")
        punishmentRepository.delete(punishmentEntity)
        Logger.info("Removed punishment: ${id}_$system")
    }


}