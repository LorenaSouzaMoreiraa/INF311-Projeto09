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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.model.Notification
import com.example.inf311_projeto09.model.NotificationsMock
import com.example.inf311_projeto09.ui.components.EmptyEventCard
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons

@Composable
fun NotificationsScreen(
    onBack: () -> Unit = {},
    notificationsMock: NotificationsMock
) {
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
                            notificationsMock.markAllNotificationsAsRead()
                        }
                )
            }

            if (notificationsMock.getNotificationsList().isEmpty()) {
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
                    items(notificationsMock.getNotificationsList()) { notification ->
                        NotificationItem(
                            notification = notification,
                            onNotificationClick = { clickedNotification ->
                                if (!clickedNotification.isRead) {
                                    notificationsMock.markNotificationAsRead(clickedNotification.id)
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
                color = if (notification.isRead) AppColors().white else AppColors().lightYellow,
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
                text = notification.description,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = notification.timeAgo,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Normal,
                color = AppColors().grey,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen(
        notificationsMock = NotificationsMock()
    )
}