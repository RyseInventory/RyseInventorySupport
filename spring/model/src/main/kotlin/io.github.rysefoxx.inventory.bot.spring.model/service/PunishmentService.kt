/*
 * Copyright (c)  2023 Rysefoxx
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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