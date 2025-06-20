package com.example.inf311_projeto09.model

import androidx.compose.runtime.mutableStateListOf
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import java.util.Calendar
import java.util.Date

// TODO: Apagar o mock
fun getMockEventsForDate(date: Date): List<Event> {
    val cal = Calendar.getInstance()
    cal.time = date

    val mockEvents = mutableListOf<Event>()

    if (AppDateHelper().isSameDay(date, AppDateHelper().getDate(2025, Calendar.JUNE, 20))) {
        mockEvents.add(
            Event(
                1,
                "INF 131 - Ecologia Básica",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(10, 0, 0),
                AppDateHelper().getTime(11, 40, 0),
                AppDateHelper().getTime(10, 0, 0),
                AppDateHelper().getTime(11, 40, 0),
                AppDateHelper().getTime(10, 3, 13),
                AppDateHelper().getTime(11, 35, 9),
                Event.EventStage.NEXT
            )
        )
    }

    if (AppDateHelper().isSameDay(date, AppDateHelper().getDate(2025, Calendar.JUNE, 21))) {
        mockEvents.add(
            Event(
                2,
                "INF 491 - Computação Musical",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(8, 0, 0),
                AppDateHelper().getTime(9, 40, 0),
                AppDateHelper().getTime(8, 0, 0),
                AppDateHelper().getTime(9, 40, 0),
                AppDateHelper().getTime(8, 15, 13),
                AppDateHelper().getTime(9, 51, 3),
                Event.EventStage.NEXT
            )
        )

        mockEvents.add(
            Event(
                3,
                "INF 311 - Programação Dispositivos móveis",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(10, 0, 0),
                AppDateHelper().getTime(11, 40, 0),
                AppDateHelper().getTime(10, 0, 0),
                AppDateHelper().getTime(11, 40, 0),
                AppDateHelper().getTime(10, 1, 4),
                null,
                Event.EventStage.NEXT
            )
        )

        mockEvents.add(
            Event(
                4,
                "ERU 324 - Metodologia de Pesquisa",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(14, 0, 0),
                AppDateHelper().getTime(15, 40, 0),
                null,
                null,
                null,
                null,
                Event.EventStage.NEXT
            )
        )

        mockEvents.add(
            Event(
                5,
                "Workshop Rubeus",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(16, 0, 0),
                AppDateHelper().getTime(17, 40, 0),
                null,
                null,
                null,
                null,
                Event.EventStage.NEXT
            )
        )
    }

    if (AppDateHelper().isSameDay(date, AppDateHelper().getDate(2025, Calendar.JUNE, 22))) {
        mockEvents.add(
            Event(
                6,
                "INF 498 - Seminário I",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(10, 0, 0),
                AppDateHelper().getTime(11, 40, 0),
                null,
                null,
                null,
                null,
                Event.EventStage.NEXT
            )
        )

        mockEvents.add(
            Event(
                7,
                "BIO 131 - Ecologia Básica",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(15, 0, 0),
                AppDateHelper().getTime(15, 50, 0),
                null,
                null,
                null,
                null,
                Event.EventStage.NEXT
            )
        )
    }

    if (AppDateHelper().isSameDay(date, AppDateHelper().getDate(2025, Calendar.JUNE, 23))) {
        mockEvents.add(
            Event(
                8,
                "INF 491 - Computação Musical",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(10, 0, 0),
                AppDateHelper().getTime(11, 40, 0),
                null,
                null,
                null,
                null,
                Event.EventStage.NEXT
            )
        )

        mockEvents.add(
            Event(
                9,
                "ERU 324 - Metodologia de Pesquisa",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(16, 0, 0),
                AppDateHelper().getTime(17, 40, 0),
                null,
                null,
                null,
                null,
                Event.EventStage.NEXT
            )
        )
    }

    if (AppDateHelper().isSameDay(date, AppDateHelper().getDate(2025, Calendar.JUNE, 24))) {
        mockEvents.add(
            Event(
                10,
                "INF 311 - Programação Dispositivos móveis",
                "Descrição do evento",
                "Aula",
                "qr-code",
                100,
                AppDateHelper().getTime(10, 0, 0),
                AppDateHelper().getTime(11, 40, 0),
                null,
                null,
                null,
                null,
                Event.EventStage.NEXT
            )
        )
    }

    return mockEvents
}

fun getCurrentEvent(): Event {
    return Event(
        3,
        "INF 311 - Programação Dispositivos móveis",
        "Descrição do evento",
        "Aula",
        "qr-code",
        100,
        AppDateHelper().getTime(10, 0, 0),
        AppDateHelper().getTime(11, 40, 0),
        AppDateHelper().getTime(10, 10, 0),
        AppDateHelper().getTime(11, 30, 0),
        AppDateHelper().getTime(10, 11, 4),
        null,
        Event.EventStage.NEXT
    )
}

fun getNextEvents(): List<Event> {
    val mockEvents = mutableListOf<Event>()

    mockEvents.add(
        Event(
            4,
            "ERU 324 - Metodologia de Pesquisa",
            "Descrição do evento",
            "Aula",
            "qr-code",
            100,
            AppDateHelper().getTime(14, 0, 0),
            AppDateHelper().getTime(15, 40, 0),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT
        )
    )

    mockEvents.add(
        Event(
            5,
            "Workshop Rubeus",
            "Descrição do evento",
            "Aula",
            "qr-code",
            100,
            AppDateHelper().getTime(16, 0, 0),
            AppDateHelper().getTime(17, 40, 0),
            AppDateHelper().getTime(16, 0, 0),
            AppDateHelper().getTime(17, 40, 0),
            null,
            null,
            Event.EventStage.NEXT
        )
    )

    return mockEvents
}

fun getNotifications(): List<Notification> {
    val mockNotifications = mutableListOf<Notification>()

    mockNotifications.add(
        Notification(
            1,
            "Não se atrase!",
            "INF 311 - Programação Dispositivos móveis irá começar dentro de 10 minutos.",
            "1h atrás",
            false
        )
    )
    mockNotifications.add(
        Notification(
            2,
            "Não se atrase!",
            "BIO 131 - Ecologia Básica irá começar dentro de 10 minutos.",
            "23h atrás",
            true
        )
    )
    mockNotifications.add(
        Notification(
            3,
            "Lembrete!",
            "INF-311 Programação Dispositivos Móveis é amanhã, às 10:00.",
            "1 dia atrás",
            true
        )
    )

    return mockNotifications
}

class NotificationsMock {

    private val notifications = mutableStateListOf<Notification>().apply {
        addAll(getNotifications())
    }

    fun getNotificationsList(): List<Notification> {
        return notifications
    }

    fun markNotificationAsRead(notificationId: Int) {
        val index = notifications.indexOfFirst { it.id == notificationId }
        if (index != -1 && !notifications[index].isRead) {
            notifications[index] = notifications[index].copy(isRead = true)
        }
    }

    fun markAllNotificationsAsRead() {
        notifications.forEachIndexed { index, notification ->
            if (!notification.isRead) {
                notifications[index] = notification.copy(isRead = true)
            }
        }
    }

    fun areAllNotificationsRead(): Boolean {
        return notifications.all { it.isRead }
    }
}

class UniversitiesMock {
    private val universities = mutableStateListOf<String>().apply {
        addAll(
            listOf(
                "Universidade Federal de Viçosa (UFV)",
                "Universidade Federal de Minas Gerais (UFMG)",
                "Universidade de São Paulo (USP)",
                "Universidade Estadual de Campinas (UNICAMP)",
                "Universidade Federal do Rio de Janeiro (UFRJ)",
                "Universidade Federal de Ouro Preto (UFOP)",
                "Pontifícia Universidade Católica de Minas Gerais (PUC Minas)"
            )
        )
    }

    fun getUniversitiesList(): List<String> {
        return universities
    }
}
