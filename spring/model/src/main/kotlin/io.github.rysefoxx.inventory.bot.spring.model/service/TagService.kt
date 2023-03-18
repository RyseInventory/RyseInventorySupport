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

    fun findAll(): List<TagEntity> {
        return tagRepository.findAll()
    }

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