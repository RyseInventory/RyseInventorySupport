package io.github.rysefoxx.inventory.bot.spring.model.entity

import io.github.rysefoxx.inventory.bot.spring.model.converter.TagConverter
import javax.persistence.*

@Entity
@Table(name = "tag_request")
class TagRequestEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Convert(converter = TagConverter::class)
    @Column(name = "tag_entity",nullable = false, length = 30)
    var tagEntity: TagEntity? = null

) {
    constructor() : this(null, null)


    override fun toString(): String {
        return "TagRequestEntity(id=$id, tagEntity=${tagEntity?.name})"
    }
}