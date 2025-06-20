package com.example.inf311_projeto09.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.ui.ScreenType
import com.example.inf311_projeto09.ui.components.EmptyEventCard
import com.example.inf311_projeto09.ui.components.EventCard
import com.example.inf311_projeto09.ui.components.NavBar
import com.example.inf311_projeto09.ui.components.NavBarOption
import com.example.inf311_projeto09.ui.notificationsMock
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    userName: String = "Erick", // TODO: pegar nome do banco de dados
    navController: NavHostController,
    currentEvent: Event? = null,
    nextEvents: List<Event> = emptyList()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBarSection(userName, navController)

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        AppColors().white,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                DateTimeSection()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Acontecendo agora",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors().darkGreen,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )

                    if (currentEvent != null) {
                        EventCard(event = currentEvent, isCurrentEvent = true)
                    } else {
                        EmptyEventCard("Nenhum evento acontecendo")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    NextEventsSection(nextEvents = nextEvents)

                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            NavBar(navController, NavBarOption.HOME)
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 80.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors().darkGreen)
                .clickable {
                    navController.navigate(ScreenType.QR_SCANNER.route)
                },
            contentAlignment = Alignment.Center
        ) {
            AppIcons.Outline.Target(25.dp, AppColors().lightGreen)
        }
    }
}

@Composable
fun TopBarSection(
    userName: String,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logotipo",
            modifier = Modifier.height(40.dp)
        )

        // TODO: colocar bolinha vermelha quando tem notificação não lida
        IconButton(
            onClick = { navController.navigate(ScreenType.NOTIFICATIONS.route) },
            modifier = Modifier.size(30.dp)
        ) {
            AppIcons.Outline.Bell(30.dp)
            if (!notificationsMock.areAllNotificationsRead())
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .offset(x = 6.dp, y = (-7).dp)
                        .background(AppColors().red, CircleShape)
                )
        }
    }

    Spacer(modifier = Modifier.height(30.dp))

    Column(
        modifier = Modifier.padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Olá, $userName",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().white,
            fontSize = 30.sp
        )

        Text(
            text = "Vamos fazer cada minuto valer a pena?",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Normal,
            color = AppColors().lightGrey,
            fontSize = 12.sp
        )
    }
}

@Composable
fun DateTimeSection() {
    val currentTime = remember { mutableStateOf(AppDateHelper().getCurrentTimeWithSeconds()) }
    val currentDayAndDate = remember { mutableStateOf(AppDateHelper().getCurrentDayAndDate()) }
    val currentMonthYear = remember { mutableStateOf(AppDateHelper().getCurrentMonthYear()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = AppDateHelper().getCurrentTimeWithSeconds()
            delay(1000)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(
                text = currentTime.value,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 16.sp
            )

            Text(
                text = "Brasil (UTC-3), Brasília",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().grey,
                fontSize = 10.sp
            )
        }

        VerticalDivider(
            color = AppColors().grey,
            thickness = 1.5.dp,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 5.dp)
        )

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = currentDayAndDate.value,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = currentMonthYear.value,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().grey,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun NextEventsSection(nextEvents: List<Event>) {
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val currentNextEventIndex by remember {
        derivedStateOf {
            if (lazyListState.layoutInfo.visibleItemsInfo.isEmpty()) 0
            else lazyListState.firstVisibleItemIndex
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "O que vem a seguir",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().darkGreen,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 15.dp)
        )
    }

    Spacer(modifier = Modifier.height(5.dp))

    if (nextEvents.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(nextEvents) { event ->
                EventCard(
                    event = event,
                    isCurrentEvent = false,
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }

        if (nextEvents.size > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                nextEvents.forEachIndexed { index, _ ->
                    CarouselDot(
                        isSelected = index == currentNextEventIndex,
                        selectedColor = AppColors().darkGreen,
                        unselectedColor = AppColors().lightGrey,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            scope.launch {
                                if (lazyListState.firstVisibleItemIndex != index) {
                                    lazyListState.animateScrollToItem(index)
                                }
                            }
                        }
                    )
                }
            }
        }
    } else {
        EmptyEventCard("Sem eventos agendados")
    }
}

@Composable
fun CarouselDot(
    isSelected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(dotSize)
            .clip(CircleShape)
            .background(if (isSelected) selectedColor else unselectedColor)
            .clickable(onClick = onClick)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(currentEvent = null, nextEvents = listOf(), navController = rememberNavController())
}