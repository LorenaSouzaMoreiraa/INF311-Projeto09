package com.example.inf311_projeto09.ui.utils

import com.example.inf311_projeto09.model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AppDateHelper {
    // TODO: Considerar localidade atual
    companion object {
        val LOCALE_PT_BR: Locale = Locale("pt", "BR")
        val TIME_ZONE_SAO_PAULO: TimeZone = TimeZone.getTimeZone("America/Sao_Paulo")
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
        val format = SimpleDateFormat(pattern, LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
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
        val format = SimpleDateFormat(patternFormat, LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        return format.format(date)
    }

    fun getCurrentDayAndDate(): String {
        val format = SimpleDateFormat("EEEE, dd", LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        var formattedDay = format.format(Date())
        formattedDay = formattedDay.replace("-feira", "")
        return formattedDay.replaceFirstChar { it.uppercase() }
    }

    fun getCurrentMonthYear(): String {
        val format = SimpleDateFormat("MMMM 'de' yyyy", LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        return format.format(Date()).replaceFirstChar { it.uppercase() }
    }

    fun getCurrentMonth(): String {
        val format = SimpleDateFormat("MMMM", LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        return format.format(Date()).replaceFirstChar { it.uppercase() }
    }

    fun isThisMonth(date: Date): Boolean {
        val now = Calendar.getInstance()
        val cal = Calendar.getInstance().apply { time = date }
        return now.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
    }

    fun isInLastNMonths(date: Date, months: Int): Boolean {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -months)
        return date.after(cal.time)
    }

    fun isThisYear(date: Date): Boolean {
        val now = Calendar.getInstance()
        val cal = Calendar.getInstance().apply { time = date }
        return now.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
    }

    fun getCurrentYear(): String {
        val format = SimpleDateFormat("yyyy", LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        return format.format(Date()).replaceFirstChar { it.uppercase() }
    }

    fun getFormattedDate(date: Date?): String {
        if (date == null) return "--/--/----"
        val format = SimpleDateFormat("dd/MM/yyyy", LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        return format.format(date)
    }

    fun getWeekdayLetter(date: Calendar): String {
        val format = SimpleDateFormat("EEE", LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        val day = format.format(date.time)
        return day.replace("-feira", "").trim().take(3).replaceFirstChar { it.uppercase() }
    }

    fun getDayNumber(date: Calendar): String {
        val format = SimpleDateFormat("dd", LOCALE_PT_BR)
        format.timeZone = TIME_ZONE_SAO_PAULO
        return format.format(date.time)
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                cal1[Calendar.MONTH] == cal2[Calendar.MONTH] &&
                cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH]
    }

    fun getTime(hour: Int, minute: Int, second: Int = 0): Date {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    fun getDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day

        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    fun getEventsForDate(events: List<Event>, date: Date): List<Event> {
        val cal1 = Calendar.getInstance().apply { time = date }

        return events.filter { event ->
            val cal2 = Calendar.getInstance().apply { time = event.beginTime }
            cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                    cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
        }
    }

    fun getCurrentEvent(events: List<Event>): Event? {
        val todayEvents = getEventsForDate(events, Calendar.getInstance().time)
        return todayEvents.firstOrNull { it.eventStage == Event.EventStage.CURRENT }
    }
}