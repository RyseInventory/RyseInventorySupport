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

package io.github.rysefoxx.inventory.bot.punishment.util

import java.util.*

object TimeUtil {

    private fun convertToSeconds(input: String): Long {
        var totalSeconds = 0L
        val timeValues = input.split(" ")
        for (i in timeValues.indices) {

            val value = try {
                timeValues[i].dropLast(1).toInt()
            }catch (ex : Exception) {
                totalSeconds = -1
                break
            }

            when (timeValues[i].last()) {
                'd' -> totalSeconds += value * 24 * 60 * 60
                'h' -> totalSeconds += value * 60 * 60
                'm' -> totalSeconds += value * 60
                's' -> totalSeconds += value
                else -> {
                    totalSeconds = -1
                    break
                }
            }
        }
        return totalSeconds
    }

    private fun getTimeOffset(input: String): Long {
        return convertToSeconds(input) * 1000
    }

    fun getCurrentTimePlusOffset(input: String): Date? {
        val offset = getTimeOffset(input)

        if(offset == -1000L)
            return null

        val currentTimeMillis = System.currentTimeMillis()
        val adjustedTimeMillis = currentTimeMillis + offset
        return Date(adjustedTimeMillis)
    }
}