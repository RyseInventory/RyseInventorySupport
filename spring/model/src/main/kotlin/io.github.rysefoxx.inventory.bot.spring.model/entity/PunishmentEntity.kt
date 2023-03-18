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