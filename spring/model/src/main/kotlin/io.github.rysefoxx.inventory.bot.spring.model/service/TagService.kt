package io.github.rysefoxx.inventory.bot.spring.model.service

import io.github.rysefoxx.inventory.bot.logger.Logger
import io.github.rysefoxx.inventory.bot.spring.model.entity.TagEntity
import io.github.rysefoxx.inventory.bot.spring.model.entity.TagRequestEntity
import io.github.rysefoxx.inventory.bot.spring.model.repository.TagAliasRepository
import io.github.rysefoxx.inventory.bot.spring.model.repository.TagRepository
import io.github.rysefoxx.inventory.bot.spring.model.repository.TagRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagService(
    @Autowired
    private val tagRepository: TagRepository,

    @Autowired
    private val tagAliasRepository: TagAliasRepository,

    @Autowired
    private val tagRequestRepository: TagRequestRepository
) {

    fun exists(name: String): Boolean {
        return tagRepository.existsById(name)
    }

    fun findTagByNameOrAliases(name: String): TagEntity? {
        val tagOptional = tagRepository.findByName(name)
        if (tagOptional.isPresent) {
            return tagOptional.get()
        }

        val aliasOptional = tagAliasRepository.findByAlias(name)
        if (aliasOptional.isPresent) {
            return aliasOptional.get().tag
        }

        return null
    }

    fun saveTagRequest(tagRequestEntity: TagRequestEntity) {
        Logger.info("Saving tag request: $tagRequestEntity")
        tagRequestRepository.save(tagRequestEntity)
        Logger.info("Saved tag request: $tagRequestEntity")
    }

    fun saveTag(tagEntity: TagEntity) {
        Logger.info("Saving tag: $tagEntity")
        tagRepository.save(tagEntity)
        Logger.info("Saved tag: $tagEntity")
    }

    fun deleteTag(name: String) {
        Logger.info("Removing tag: $name")
        tagRepository.deleteById(name)
        Logger.info("Removed tag: $name")
    }
}