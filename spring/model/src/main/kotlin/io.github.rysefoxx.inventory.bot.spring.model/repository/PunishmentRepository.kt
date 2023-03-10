package io.github.rysefoxx.inventory.bot.spring.model.repository

import io.github.rysefoxx.inventory.bot.spring.model.entity.PunishmentEntity
import io.github.rysefoxx.inventory.bot.spring.model.service.PunishmentService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import javax.transaction.Transactional

@Repository
interface PunishmentRepository : JpaRepository<PunishmentEntity, Long> {

    fun findByUserIdAndSystem(id: Long, system: String): Optional<PunishmentEntity>
    @Transactional
    fun deleteByUserIdAndSystem(id: Long, system: String)

}