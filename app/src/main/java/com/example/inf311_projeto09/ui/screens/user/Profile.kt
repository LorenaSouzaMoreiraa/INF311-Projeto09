package com.example.inf311_projeto09.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.helper.EventHelper
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.ScreenType
import com.example.inf311_projeto09.ui.components.NavBar
import com.example.inf311_projeto09.ui.components.NavBarOption
import com.example.inf311_projeto09.ui.components.SeeEventsCard
import com.example.inf311_projeto09.ui.components.StatisticCard
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import java.util.Calendar

@Composable
fun ProfileScreen(
    user: User,
    allEvents: List<Event>,
    onLogout: () -> Unit = {},
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        TopBarProfile(user, onLogout, navController)

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    AppColors().offWhite,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CardStatistics(allEvents, navController)

            Spacer(modifier = Modifier.height(30.dp))

            MonthStatistics(allEvents)
        }

        NavBar(navController, NavBarOption.PROFILE, user)
    }
}

@Composable
fun TopBarProfile(
    user: User,
    onLogout: () -> Unit = {},
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clickable { onLogout() },
            contentAlignment = Alignment.Center
        ) {
            AppIcons.Outline.LogOut(30.dp)
        }

        Box(
            modifier = Modifier
                .size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.imageUrl)
                    .crossfade(true)
                    .crossfade(300)
                    .build(),
                contentDescription = "Foto do usuário",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.profile_image),
                error = painterResource(R.drawable.profile_image),
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .clickable { navController.navigate(ScreenType.EDIT_PROFILE.route) },
            contentAlignment = Alignment.Center
        ) {
            AppIcons.Outline.Settings(30.dp)
        }
    }

    Spacer(modifier = Modifier.height(30.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = user.email,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            color = AppColors().lightGrey,
            fontSize = 12.sp
        )

        Text(
            text = user.name,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().white,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 40.dp)
        )

        Text(
            text = user.school.replace(Regex("\\s*\\([^)]*\\)\$"), ""),
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            color = AppColors().lightGrey,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 40.dp)
        )
    }
}

@Composable
fun CardStatistics(
    allEvents: List<Event>,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatisticCard(
                EventHelper.numberOfEventsParticipated(allEvents),
                "eventos participados",
                modifier = Modifier.weight(1f)
            )
            StatisticCard(
                EventHelper.numberOfMissedEvents(allEvents),
                "faltas em eventos",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatisticCard(
                EventHelper.numberOfDelayedEvents(allEvents),
                "atrasos no registro de presença",
                modifier = Modifier.weight(1f)
            )
            SeeEventsCard(modifier = Modifier.weight(1f), navController)
        }
    }
}

enum class DatEventsStatus {
    NONE_EVENT,
    ONE_EVENT,
    TWO_EVENTS,
    THREE_EVENTS,
    FOUR_MORE_EVENTS
}

data class CalendarDay(
    val dayOfMonth: Int,
    val status: DatEventsStatus
)

@Composable
fun MonthStatistics(
    allEvents: List<Event>
) {
    val currentMonth = AppDateHelper().getCurrentMonth()
    val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

    val currentMonthNumber = AppDateHelper().getCurrentMonthNumber()
    val currentYearNumber = AppDateHelper().getCurrentYearNumber()

    val totalEvents = EventHelper.numberOfEventsParticipatedInMonth(
        allEvents,
        currentMonthNumber,
        currentYearNumber
    )
    val participationPercentage = if (totalEvents == 0) 0
    else ((totalEvents - EventHelper.numberOfMissedEventsInMonth(
        allEvents,
        currentMonthNumber,
        currentYearNumber
    )) * 100 / totalEvents)
    val lateCount = EventHelper.numberOfDelayedEventsInMonth(
        allEvents,
        currentMonthNumber,
        currentYearNumber
    )

    val days: List<CalendarDay> = (1..daysInMonth).map { day ->
        val eventCount = EventHelper.numberOfEventsParticipatedInDay(
            allEvents,
            day,
            currentMonthNumber,
            currentYearNumber
        )
        val status = when {
            eventCount >= 4 -> DatEventsStatus.FOUR_MORE_EVENTS
            eventCount == 3 -> DatEventsStatus.THREE_EVENTS
            eventCount == 2 -> DatEventsStatus.TWO_EVENTS
            eventCount == 1 -> DatEventsStatus.ONE_EVENT
            else -> DatEventsStatus.NONE_EVENT
        }
        CalendarDay(day, status)
    }

    val noneEventColor = AppColors().darkGrey
    val oneEventColor = AppColors().lightPastelGreen
    val twoEventsColor = AppColors().pastelGreen
    val threeEventsColor = AppColors().darkPastelGreen
    val fourOrMoreEventsColor = AppColors().darkGreen

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Estatísticas do mês",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = AppColors().darkGreen
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = currentMonth,
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = AppColors().black
                )
            }

            PresenceLegend()
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.weight(0.45f)
            ) {
                Column {
                    days.chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            week.forEach { day ->
                                val color = when (day.status) {
                                    DatEventsStatus.FOUR_MORE_EVENTS -> fourOrMoreEventsColor
                                    DatEventsStatus.THREE_EVENTS -> threeEventsColor
                                    DatEventsStatus.TWO_EVENTS -> twoEventsColor
                                    DatEventsStatus.ONE_EVENT -> oneEventColor
                                    DatEventsStatus.NONE_EVENT -> noneEventColor
                                }

                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            color,
                                            RoundedCornerShape(3.dp)
                                        )
                                )
                            }

                            repeat(7 - week.size) {
                                Spacer(modifier = Modifier.size(12.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.1f),
                contentAlignment = Alignment.Center
            ) {
                VerticalDivider(
                    color = AppColors().black,
                    thickness = 1.dp
                )
            }

            Column(
                modifier = Modifier.weight(0.45f)
            ) {
                Text(
                    text = "Overview",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = AppColors().grey
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$totalEvents evento(s)",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = AppColors().black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "$participationPercentage% participação",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = AppColors().black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "$lateCount atraso(s)",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = AppColors().black
                )
            }
        }
    }
}

@Composable
fun PresenceLegend() {
    val presentFullColor = AppColors().darkGreen
    val present75Color = AppColors().darkPastelGreen
    val present50Color = AppColors().pastelGreen
    val present25Color = AppColors().lightPastelGreen
    val absentColor = AppColors().darkGrey

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Menos",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = AppColors().grey
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(12.dp)
                .background(absentColor, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(present25Color, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(present50Color, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(present75Color, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(presentFullColor, RoundedCornerShape(3.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Mais",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = AppColors().grey
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        user = User(
            0,
            "Erick",
            User.UserRole.USER,
            "teste@teste.com",
            "cpf",
            "UFV",
            "****",
            true,
            null,
            listOf()
        ),
        allEvents = listOf(),
        navController = rememberNavController()
    )
}