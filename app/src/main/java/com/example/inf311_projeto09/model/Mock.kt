package com.example.inf311_projeto09.model

import com.example.inf311_projeto09.ui.utils.AppDateFormatter
import java.util.Calendar
import java.util.Date

// TODO: Apagar o mock
fun getMockEventsForDate(date: Date): List<Event> {
    val cal = Calendar.getInstance()
    cal.time = date

    val mockEvents = mutableListOf<Event>()

    if (AppDateFormatter().isSameDay(date, AppDateFormatter().getDate(2025, Calendar.JUNE, 9))) {
        mockEvents.add(
            Event(
                id = "1",
                title = "INF 131 - Ecologia Básica",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(10, 0, 0),
                endTime = AppDateFormatter().getTime(11, 40, 0),
                checkInEnable = AppDateFormatter().getTime(10, 0, 0),
                checkOutEnable = AppDateFormatter().getTime(11, 40, 0),
                checkInTime = AppDateFormatter().getTime(10, 3, 13),
                checkOutTime = AppDateFormatter().getTime(11, 35, 9)
            )
        )
    }

    if (AppDateFormatter().isSameDay(date, AppDateFormatter().getDate(2025, Calendar.JUNE, 10))) {
        mockEvents.add(
            Event(
                id = "2",
                title = "INF 491 - Computação Musical",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(8, 0, 0),
                endTime = AppDateFormatter().getTime(9, 40, 0),
                checkInEnable = AppDateFormatter().getTime(8, 0, 0),
                checkOutEnable = AppDateFormatter().getTime(9, 40, 0),
                checkInTime = AppDateFormatter().getTime(8, 15, 13),
                checkOutTime = AppDateFormatter().getTime(9, 51, 3)
            )
        )

        mockEvents.add(
            Event(
                id = "3",
                title = "INF 311 - Programação Dispositivos móveis",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(10, 0, 0),
                endTime = AppDateFormatter().getTime(11, 40, 0),
                checkInEnable = AppDateFormatter().getTime(10, 0, 0),
                checkOutEnable = AppDateFormatter().getTime(11, 40, 0),
                checkInTime = AppDateFormatter().getTime(10, 1, 4),
                checkOutTime = null
            )
        )

        mockEvents.add(
            Event(
                id = "4",
                title = "ERU 324 - Metodologia de Pesquisa",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(14, 0, 0),
                endTime = AppDateFormatter().getTime(15, 40, 0),
                checkInEnable = null,
                checkOutEnable = null,
                checkInTime = null,
                checkOutTime = null
            )
        )

        mockEvents.add(
            Event(
                id = "5",
                title = "Workshop Rubeus",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(16, 0, 0),
                endTime = AppDateFormatter().getTime(17, 40, 0),
                checkInEnable = null,
                checkOutEnable = null,
                checkInTime = null,
                checkOutTime = null
            )
        )
    }

    if (AppDateFormatter().isSameDay(date, AppDateFormatter().getDate(2025, Calendar.JUNE, 11))) {
        mockEvents.add(
            Event(
                id = "6",
                title = "INF 498 - Seminário I",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(10, 0, 0),
                endTime = AppDateFormatter().getTime(11, 40, 0),
                checkInEnable = null,
                checkOutEnable = null,
                checkInTime = null,
                checkOutTime = null
            )
        )

        mockEvents.add(
            Event(
                id = "7",
                title = "BIO 131 - Ecologia Básica",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(15, 0, 0),
                endTime = AppDateFormatter().getTime(15, 50, 0),
                checkInEnable = null,
                checkOutEnable = null,
                checkInTime = null,
                checkOutTime = null
            )
        )
    }

    if (AppDateFormatter().isSameDay(date, AppDateFormatter().getDate(2025, Calendar.JUNE, 12))) {
        mockEvents.add(
            Event(
                id = "8",
                title = "INF 491 - Computação Musical",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(10, 0, 0),
                endTime = AppDateFormatter().getTime(11, 40, 0),
                checkInEnable = null,
                checkOutEnable = null,
                checkInTime = null,
                checkOutTime = null
            )
        )

        mockEvents.add(
            Event(
                id = "9",
                title = "ERU 324 - Metodologia de Pesquisa",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(16, 0, 0),
                endTime = AppDateFormatter().getTime(17, 40, 0),
                checkInEnable = null,
                checkOutEnable = null,
                checkInTime = null,
                checkOutTime = null
            )
        )
    }

    if (AppDateFormatter().isSameDay(date, AppDateFormatter().getDate(2025, Calendar.JUNE, 13))) {
        mockEvents.add(
            Event(
                id = "10",
                title = "INF 311 - Programação Dispositivos móveis",
                type = "Aula",
                beginTime = AppDateFormatter().getTime(10, 0, 0),
                endTime = AppDateFormatter().getTime(11, 40, 0),
                checkInEnable = null,
                checkOutEnable = null,
                checkInTime = null,
                checkOutTime = null
            )
        )
    }

    return mockEvents
}

fun getCurrentEvent(): Event {
    return Event(
        id = "3",
        title = "INF 311 - Programação Dispositivos móveis",
        type = "Aula",
        beginTime = AppDateFormatter().getTime(10, 0, 0),
        endTime = AppDateFormatter().getTime(11, 40, 0),
        checkInEnable = AppDateFormatter().getTime(10, 10, 0),
        checkOutEnable = AppDateFormatter().getTime(11, 30, 0),
        checkInTime = AppDateFormatter().getTime(10, 11, 4),
        checkOutTime = null
    )
}

fun getNextEvents(): List<Event> {
    val mockEvents = mutableListOf<Event>()

    mockEvents.add(
        Event(
            id = "4",
            title = "ERU 324 - Metodologia de Pesquisa",
            type = "Aula",
            beginTime = AppDateFormatter().getTime(14, 0, 0),
            endTime = AppDateFormatter().getTime(15, 40, 0),
            checkInEnable = null,
            checkOutEnable = null,
            checkInTime = null,
            checkOutTime = null
        )
    )

    mockEvents.add(
        Event(
            id = "5",
            title = "Workshop Rubeus",
            type = "Aula",
            beginTime = AppDateFormatter().getTime(16, 0, 0),
            endTime = AppDateFormatter().getTime(17, 40, 0),
            checkInEnable = AppDateFormatter().getTime(16, 0, 0),
            checkOutEnable = AppDateFormatter().getTime(17, 40, 0),
            checkInTime = null,
            checkOutTime = null
        )
    )

    return mockEvents
}
