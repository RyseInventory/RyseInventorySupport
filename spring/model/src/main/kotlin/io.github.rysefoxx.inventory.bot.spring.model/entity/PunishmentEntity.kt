package io.github.rysefoxx.inventory.bot.spring.model.entity

import io.github.rysefoxx.inventory.bot.spring.model.converter.DateConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "punishment")
class PunishmentEntity constructor(

    @Column(name = "user_id", nullable = false, unique = true)
    var userId: Long? = null,

    @Column(name = "from_id", nullable = false)
    var fromId: Long? = null,

    @Column(name = "embed_id", nullable = false)
    var embedId: Long? = null,

    @Column(nullable = false)
    var reason: String? = null,

    @Column(nullable = false)
    var system: String? = null,

    @Convert(converter = DateConverter::class)
    @Column(name = "time", nullable = false)
    var date: Date? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null
) {

    constructor() : this(userId = null, fromId = null, embedId = null, reason = null, system = null, date = null)

    override fun toString(): String {
        return "PunishmentEntity(id=$id, userId=$userId, fromId=$fromId, reason=$reason, system=$system, time=$date)"
    }
}