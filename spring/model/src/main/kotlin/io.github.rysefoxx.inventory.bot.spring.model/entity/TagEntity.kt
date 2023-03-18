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