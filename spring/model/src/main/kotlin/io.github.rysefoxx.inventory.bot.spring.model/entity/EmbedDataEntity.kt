package io.github.rysefoxx.inventory.bot.spring.model.entity

import io.github.rysefoxx.inventory.bot.spring.model.converter.ListConverter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "embed")
class EmbedDataEntity constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Convert(converter = ListConverter::class)
    @Column(nullable = false)
    var data: List<String>? = null

) {
    constructor() : this(data = null)
}