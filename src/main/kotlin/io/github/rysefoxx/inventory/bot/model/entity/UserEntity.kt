package io.github.rysefoxx.inventory.bot.model.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_data")
class UserEntity constructor(
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    var id: Long? = null,

    @Column(nullable = false)
    var language: String = "en"
) {
    constructor() : this(id = null)
}