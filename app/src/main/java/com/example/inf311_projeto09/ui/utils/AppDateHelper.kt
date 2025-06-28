package com.example.inf311_projeto09.ui.utils

import com.example.inf311_projeto09.model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class AppDateHelper(
    val locale: Locale = DEFAULT_LOCALE,
    val timeZone: TimeZone = DEFAULT_TIME_ZONE
) {
    companion object {
        // Obtém dinamicamente a localidade e o fuso horário padrão do dispositivo
        private val DEFAULT_LOCALE: Locale get() = Locale.getDefault()
        private val DEFAULT_TIME_ZONE: TimeZone get() = TimeZone.getDefault()
    }

    fun getDateByDateStringAndTimeString(date: String, time: String): Date {
        val dateString = "$date $time"
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
        formatter.timeZone = timeZone
        return formatter.parse(dateString)!!
    }

    fun getCurrentCompleteTime(): String {
        return getCurrentTime("yyyy-MM-dd'T'HH:mm:ss")
    }

    fun getCurrentTime(): String {
        return getCurrentTime("HH:mm")
    }

    fun getCurrentTimeWithSeconds(): String {
        return getCurrentTime("HH:mm:ss")
    }

    private fun getCurrentTime(pattern: String): String {
        val format = SimpleDateFormat(pattern, locale)
        format.timeZone = timeZone
        return format.format(Date())
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
        val format = SimpleDateFormat(patternFormat, locale)
        format.timeZone = timeZone
        return format.format(date)
    }

    fun getCurrentDayAndDate(): String {
        val format = SimpleDateFormat("EEEE, dd", locale)
        format.timeZone = timeZone
        var formattedDay = format.format(Date())
        // A remoção de "-feira" é específica do português, pode ser ajustada se necessário
        if (locale.language == "pt") {
            formattedDay = formattedDay.replace("-feira", "")
        }
        return formattedDay.replaceFirstChar { it.uppercase() }
    }

    fun getCurrentMonthYear(): String {
        val format = SimpleDateFormat("MMMM 'de' yyyy", locale)
        format.timeZone = timeZone
        return format.format(Date()).replaceFirstChar { it.uppercase() }
    }

    fun getFullFormattedDate(): String {
        val format = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", locale)
        format.timeZone = timeZone
        return format.format(Date())
    }

    fun getCurrentMonth(): String {
        val format = SimpleDateFormat("MMMM", locale)
        format.timeZone = timeZone
        return format.format(Date()).replaceFirstChar { it.uppercase() }
    }

    fun getCurrentMonthNumber(): Int {
        val calendar = Calendar.getInstance(timeZone, locale)
        return calendar[Calendar.MONTH]
    }

    fun getCurrentYearNumber(): Int {
        val calendar = Calendar.getInstance(timeZone, locale)
        return calendar[Calendar.YEAR]
    }

    fun isThisMonth(date: Date): Boolean {
        val now = Calendar.getInstance(timeZone, locale)
        val cal = Calendar.getInstance(timeZone, locale).apply { time = date }
        return now.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
    }

    fun isInLastNMonths(date: Date, months: Int): Boolean {
        val cal = Calendar.getInstance(timeZone, locale)
        cal.add(Calendar.MONTH, -months)
        return date.after(cal.time)
    }

    fun isThisYear(date: Date): Boolean {
        val now = Calendar.getInstance(timeZone, locale)
        val cal = Calendar.getInstance(timeZone, locale).apply { time = date }
        return now.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
    }

    fun getCurrentYear(): String {
        val format = SimpleDateFormat("yyyy", locale)
        format.timeZone = timeZone
        return format.format(Date()).replaceFirstChar { it.uppercase() }
    }

    fun getFormattedDate(date: Date?): String {
        if (date == null) return "--/--/----"
        val format = SimpleDateFormat("dd/MM/yyyy", locale)
        format.timeZone = timeZone
        return format.format(date)
    }

    fun getWeekdayLetter(date: Calendar): String {
        val format = SimpleDateFormat("EEE", locale)
        format.timeZone = timeZone
        val day = format.format(date.time)
        return day.trim().take(3).replaceFirstChar { it.uppercase() }
    }

    fun getDayNumber(date: Calendar): String {
        val format = SimpleDateFormat("dd", locale)
        format.timeZone = timeZone
        return format.format(date.time)
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        val cal1 = Calendar.getInstance(timeZone, locale)
        cal1.time = date1
        val cal2 = Calendar.getInstance(timeZone, locale)
        cal2.time = date2
        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                cal1[Calendar.MONTH] == cal2[Calendar.MONTH] &&
                cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH]
    }

    fun getTime(hour: Int, minute: Int, second: Int = 0): Date {
        val calendar = Calendar.getInstance(timeZone, locale)
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    fun getDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance(timeZone, locale)
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day

        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    fun getFullDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date {
        val calendar = Calendar.getInstance(timeZone, locale)
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day

        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    fun getEventsForDate(events: List<Event>, date: Date): List<Event> {
        val cal1 = Calendar.getInstance(timeZone, locale).apply { time = date }

        return events.filter { event ->
            val cal2 = Calendar.getInstance(timeZone, locale).apply { time = event.beginTime }
            cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                    cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
        }
    }

    fun getCurrentEvent(events: List<Event>): Event? {
        val todayEvents = getEventsForDate(events, Calendar.getInstance(timeZone, locale).time)
        return todayEvents.firstOrNull { it.eventStage == Event.EventStage.CURRENT }
    }

    fun getTimeZoneReferenceText(): String {
        val countryName = locale.getDisplayCountry(locale).ifEmpty {
            timeZone.getDisplayName(locale)
        }

        val now = Date().time
        val offsetInMillis = timeZone.getOffset(now)
        val offsetHours = TimeUnit.MILLISECONDS.toHours(offsetInMillis.toLong())

        val utcOffsetText = String.format("UTC%+d", offsetHours)

        val representativeCity = timeZone.id.split('/').last().replace('_', ' ')
        return "$countryName ($utcOffsetText), $representativeCity"
    }
}