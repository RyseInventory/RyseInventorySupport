package io.github.rysefoxx.inventory.bot.spring.model.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "tag_alias")
class TagAliasEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var alias: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_name", nullable = false)
    var tag: TagEntity? = null
) : Serializable {
    constructor() : this(null, null, null)

    override fun toString(): String {
        return "TagAliasEntity(id=$id, alias=$alias, tag-name=${tag?.name})"
    }
}