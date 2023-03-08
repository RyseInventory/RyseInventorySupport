package io.github.rysefoxx.inventory.bot.model.service

import io.github.rysefoxx.inventory.bot.log.Logger
import io.github.rysefoxx.inventory.bot.model.entity.UserEntity
import io.github.rysefoxx.inventory.bot.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository
) {

    fun getUserById(id: Long): UserEntity {
        return userRepository.findById(id).orElseGet {
            val user = UserEntity(id)
            saveUser(user)
            user
        }
    }

    fun saveUser(userEntity: UserEntity) {
        Logger.info("Saving user: ${userEntity.id}")
        userRepository.save(userEntity)
        Logger.info("Saved user: ${userEntity.id}")
    }

    fun deleteUser(id: Long) {
        Logger.info("Removing user: $id")
        userRepository.deleteById(id)
        Logger.info("Removed user: $id")
    }
}