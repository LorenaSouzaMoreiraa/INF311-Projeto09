package com.example.inf311_projeto09.ui.screens.user

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.ui.ScreenType
import com.example.inf311_projeto09.ui.components.NavBar
import com.example.inf311_projeto09.ui.components.NavBarOption
import com.example.inf311_projeto09.ui.components.SeeEventsCard
import com.example.inf311_projeto09.ui.components.StatisticCard
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateFormatter
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import java.util.Calendar
import kotlin.random.Random

@Composable
fun ProfileScreen(
    userId: String = "idUsuario", // TODO: pegar nome do banco de dados
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        TopBarProfile(userId, navController)

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    AppColors().white,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CardStatistics(userId, navController)

            Spacer(modifier = Modifier.height(30.dp))

            MonthStatistics(userId)
        }

        NavBar(navController, NavBarOption.PROFILE)
    }
}

@Composable
fun TopBarProfile(
    userid: String,
    navController: NavHostController
) {
    // TODO: pegar pelo id do usuário
    val profileImage = 1
    val email = "erick@rubeus.com"
    val userName = "Erick Soares"
    val institution = "Universidade Federal de Viçosa"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // TODO: remover a senha
        Box(
            modifier = Modifier
                .size(30.dp)
                .clickable { navController.navigate(ScreenType.LOGIN.route) },
            contentAlignment = Alignment.Center
        ) {
            AppIcons.Outline.LogOut(30.dp)
        }

        Box(
            modifier = Modifier
                .size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            if (profileImage == null) {
                AppIcons.Outline.CircleUserRound(120.dp)
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Foto do usuário",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(118.dp)
                    )
                }
            }
        }

        // TODO: fazer tela de configurações
        Box(
            modifier = Modifier
                .size(30.dp)
                .clickable { /*navController.navigate(ScreenType.LOGIN.route)*/ },
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
            text = email,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            color = AppColors().lightGrey,
            fontSize = 12.sp
        )

        Text(
            text = userName,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().white,
            fontSize = 20.sp
        )

        Text(
            text = institution,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            color = AppColors().lightGrey,
            fontSize = 12.sp
        )
    }
}

@Composable
fun CardStatistics(
    userid: String,
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
            StatisticCard(12, "eventos participados", modifier = Modifier.weight(1f))
            StatisticCard(8, "faltas em eventos", modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatisticCard(5, "atrasos no registro de presença", modifier = Modifier.weight(1f))
            SeeEventsCard(modifier = Modifier.weight(1f), navController)
        }
    }
}

enum class PresenceStatus {
    PRESENT_FULL,
    PRESENT_75,
    PRESENT_50,
    PRESENT_25,
    ABSENT
}

data class CalendarDay(
    val dayOfMonth: Int,
    val status: PresenceStatus
)

@Composable
fun MonthStatistics(
    userid: String
) {
    // TODO: pegar dados reais, por agora está fictícil
    val currentMonth = AppDateFormatter().getCurrentMonth()
    val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

    val totalEvents = 10
    val participationPercentage = 98
    val lateCount = 3

    val days: List<CalendarDay> = (1..daysInMonth).map { day ->
        val status = if (day <= 25) {
            when (Random.nextInt(5)) {
                0 -> PresenceStatus.PRESENT_FULL
                1 -> PresenceStatus.PRESENT_75
                2 -> PresenceStatus.PRESENT_50
                3 -> PresenceStatus.PRESENT_25
                else -> PresenceStatus.ABSENT
            }
        } else {
            PresenceStatus.ABSENT
        }
        CalendarDay(day, status)
    }

    val presentFullColor = AppColors().darkGreen
    val present75Color = AppColors().darkPastelGreen
    val present50Color = AppColors().pastelGreen
    val present25Color = AppColors().lightPastelGreen
    val absentColor = AppColors().darkGrey

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
                                    PresenceStatus.PRESENT_FULL -> presentFullColor
                                    PresenceStatus.PRESENT_75 -> present75Color
                                    PresenceStatus.PRESENT_50 -> present50Color
                                    PresenceStatus.PRESENT_25 -> present25Color
                                    PresenceStatus.ABSENT -> absentColor
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
                    text = "$totalEvents eventos",
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
    ProfileScreen(navController = rememberNavController())
}