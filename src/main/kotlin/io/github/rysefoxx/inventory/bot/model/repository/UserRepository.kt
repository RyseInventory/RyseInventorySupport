package io.github.rysefoxx.inventory.bot.model.repository

import io.github.rysefoxx.inventory.bot.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
}