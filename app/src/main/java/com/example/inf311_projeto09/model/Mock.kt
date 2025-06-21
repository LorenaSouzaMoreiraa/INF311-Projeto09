package com.example.inf311_projeto09.model

import androidx.compose.runtime.mutableStateListOf

// TODO: Apagar o mock

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
