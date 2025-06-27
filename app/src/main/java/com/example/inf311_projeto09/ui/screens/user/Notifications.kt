package com.example.inf311_projeto09.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.model.Notification
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.components.EmptyEventCard
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import java.time.Duration
import java.time.Instant
import java.util.Date

@Composable
fun NotificationsScreen(
    user: User,
    onBack: () -> Unit = {}
) {
    val notifications =
        remember { mutableStateListOf<Notification>().apply { addAll(user.notifications) } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().offWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(30.dp)
                    ) {
                        AppIcons.Outline.CircleArrowLeft(30.dp, AppColors().darkGreen)
                    }

                    Text(
                        text = "Notificações",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors().black,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }

                Text(
                    text = "Marcar todas\ncomo lidas",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors().darkGreen,
                    fontSize = 15.sp,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            notifications.forEachIndexed { index, notification ->
                                if (!notification.read) {
                                    notifications[index] = notification.readNotification()
                                    user.notifications[index].read = true
                                }
                            }

                            RubeusApi.updateUserNotifications(user, user.notifications)
                        }
                )
            }

            if (notifications.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyEventCard("Não existem notificações no momento.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(notifications) { index, notification ->
                        NotificationItem(
                            notification = notification,
                            onNotificationClick = { clickedNotification ->
                                if (!clickedNotification.read) {
                                    notifications[index] = notification.readNotification()
                                    user.notifications[index].read = true
                                    RubeusApi.updateUserNotifications(user, user.notifications)
                                }
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppColors().transparent,
                            AppColors().offWhite
                        )
                    )
                )
        )
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onNotificationClick: (Notification) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 8.dp)
            .background(
                color = if (notification.read) AppColors().white else AppColors().lightYellow,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onNotificationClick(notification) }
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = notification.title,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().darkGreen,
                fontSize = 16.sp
            )
            Text(
                text = notification.subtitle,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = timeAgo(notification.notificationTime),
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Normal,
                color = AppColors().grey,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun timeAgo(date: Date): String {
    val now = Instant.now()
    val dataInstant = date.toInstant()

    val duration = Duration.between(dataInstant, now)

    val seconds = duration.seconds

    return when {
        seconds < 60 -> "${seconds}s atrás"
        seconds < 3600 -> "${seconds / 60}min atrás"
        seconds < 86400 -> "${seconds / 3600}h atrás"
        else -> "${seconds / 86400}d atrás"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen(
        User(
            0,
            "Erick Soares",
            User.UserRole.USER,
            "teste@teste.com",
            "12345678900",
            "Universidade Federal de Viçosa (UFV)",
            "****",
            true,
            null,
            listOf()
        )
    )
}