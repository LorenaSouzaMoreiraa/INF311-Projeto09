package com.example.inf311_projeto09.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AppDateFormatter {
    fun getCurrentTime(): String {
        return getCurrentTime("HH:mm")
    }

    fun getCurrentTimeWithSeconds(): String {
        return getCurrentTime("HH:mm:ss")
    }

    private fun getCurrentTime(pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale("pt", "BR"))
        dateFormat.timeZone = TimeZone.getTimeZone("America/Sao_Paulo")
        return dateFormat.format(Date())
    }

    fun getTimeFormatted(date: Date?): String {
        if (date == null)
            return "--:--"

        return getTimeFormatted(date, false)
    }

    fun getTimeFormattedWithSeconds(date: Date?): String {
        if (date == null)
            return "--:--:--"

        return getTimeFormatted(date, true)
    }

    private fun getTimeFormatted(date: Date, withSeconds: Boolean): String {
        val patternFormat = "HH:mm" + if (withSeconds) ":ss" else ""
        val timeFormatter = SimpleDateFormat(patternFormat, Locale("pt", "BR"))
        return timeFormatter.format(date)
    }

    fun getCurrentDayAndDate(): String {
        val dayFormat = SimpleDateFormat("EEEE, dd", Locale("pt", "BR"))
        var formattedDay = dayFormat.format(Date())
        formattedDay = formattedDay.replace("-feira", "")
        return formattedDay.replaceFirstChar { it.uppercase() }
    }

    fun getCurrentMonthYear(): String {
        val monthYearFormat = SimpleDateFormat("MMMM 'de' yyyy", Locale("pt", "BR"))
        return monthYearFormat.format(Date()).replaceFirstChar { it.uppercase() }
    }
}