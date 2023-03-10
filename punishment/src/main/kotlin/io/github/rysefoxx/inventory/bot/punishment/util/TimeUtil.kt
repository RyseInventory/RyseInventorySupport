package io.github.rysefoxx.inventory.bot.punishment.util

import java.lang.Exception
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