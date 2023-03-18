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

package io.github.rysefoxx.inventory.bot.spring.model.entity

import io.github.rysefoxx.inventory.bot.spring.model.converter.DateConverter
import java.io.*
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tag")
class TagEntity(
    @Id
    @Column(nullable = false, length = 20, unique = true)
    var name: String? = null,

    @Convert(converter = DateConverter::class)
    @Column(nullable = false, length = 30)
    var created: Date? = null,

    @Column(name = "from_id", nullable = false)
    var fromId: Long? = null,

    @Column(nullable = false)
    var content: String? = null,

    @OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], orphanRemoval = true)
    var aliases: MutableSet<TagAliasEntity> = mutableSetOf()
) : Serializable {
    constructor() : this(null, null, null, null)

    fun serializeToString(): String {
        val stream = ByteArrayOutputStream()
        ObjectOutputStream(stream).use { it.writeObject(this) }
        return Base64.getEncoder().encodeToString(stream.toByteArray())
    }

    companion object {
        fun deserializeFromString(serializedString: String): TagEntity {
            val bytes = Base64.getDecoder().decode(serializedString)
            ObjectInputStream(ByteArrayInputStream(bytes)).use { return it.readObject() as TagEntity }
        }
    }

    fun toAliases(aliases: String) : MutableSet<TagAliasEntity> {
        if(aliases.isEmpty() || aliases == "❔") {
            return mutableSetOf()
        }

        val tagAliasEntities: MutableSet<TagAliasEntity>
        val array = aliases.split(",")

        tagAliasEntities = array.map {
            TagAliasEntity(
                alias = it,
                tag = this
            )
        }.toMutableSet()

        return tagAliasEntities
    }

    fun aliasesToString(default: String): String {
        if(aliases.isEmpty())
            return default

        return aliases.distinctBy { it.alias.toString().lowercase() }.joinToString(", ") { it.alias.toString() }
    }

    fun aliasesToStringNullable(): String? {
        if(aliases.isEmpty())
            return null

        return aliases.distinctBy { it.alias.toString().lowercase() }.joinToString(", ") { it.alias.toString() }
    }

    override fun toString(): String {
        return "TagEntity(name=$name, content=$content, created=$created, fromId=$fromId, aliases=$aliases)"
    }

}