package com.example.inf311_projeto09.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.components.FilterDialog
import com.example.inf311_projeto09.ui.components.NavBar
import com.example.inf311_projeto09.ui.components.NavBarOption
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons

enum class EventFilter(val label: String) {
    ONLINE("Online"),
    IN_PERSON("Presencial"),
    HYBRID("Híbrido"),
    CURRENT("Em progresso"),
    NEXT("Pendente"),
    ENDED("Finalizado"),
    THIS_MONTH("Neste mês"),
    LAST_THREE_MONTHS("Nos últimos 3 meses"),
    LAST_SIX_MONTHS("Nos últimos 6 meses"),
    THIS_YEAR("Neste ano"),
    PREVIOUS_YEARS("Nos anos anteriores")
}

// TODO: fixar evento atual no topo
// TODO: gerar arquivo de saída

@Composable
fun EventsScreen(
    user: User,
    allEvents: List<Event> = emptyList(),
    navController: NavHostController
) {
    val selectedFilters = remember { mutableStateOf(setOf<EventFilter>()) }
    val showFilterDialog = remember { mutableStateOf(false) }

    EventsContent(user, navController, allEvents, selectedFilters, showFilterDialog)
}

@Composable
fun EventsContent(
    user: User,
    navController: NavHostController,
    allEvents: List<Event>,
    selectedFilters: MutableState<Set<EventFilter>>,
    showFilterDialog: MutableState<Boolean>
) {
    val helper = AppDateHelper()

    val filteredEvents = remember(allEvents, selectedFilters.value) {
        if (selectedFilters.value.isEmpty()) {
            allEvents
        } else {
            allEvents.filter { event ->
                selectedFilters.value.all { filter ->
                    when (filter) {
                        EventFilter.ONLINE -> event.type.contains("Online", ignoreCase = true)
                        EventFilter.IN_PERSON -> event.type.contains(
                            "Presencial",
                            ignoreCase = true
                        )

                        EventFilter.HYBRID -> event.type.contains("Híbrido", ignoreCase = true)
                        EventFilter.CURRENT -> event.eventStage == Event.EventStage.CURRENT
                        EventFilter.NEXT -> event.eventStage == Event.EventStage.NEXT
                        EventFilter.ENDED -> event.eventStage == Event.EventStage.ENDED
                        EventFilter.THIS_MONTH -> helper.isThisMonth(event.beginTime)
                        EventFilter.LAST_THREE_MONTHS -> helper.isInLastNMonths(event.beginTime, 3)
                        EventFilter.LAST_SIX_MONTHS -> helper.isInLastNMonths(event.beginTime, 6)
                        EventFilter.THIS_YEAR -> helper.isThisYear(event.beginTime)
                        EventFilter.PREVIOUS_YEARS -> !helper.isThisYear(event.beginTime)
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(top = 50.dp, start = 30.dp, end = 30.dp)) {
                Text(
                    text = "Eventos",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors().white,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Gerencie e registre presenças em eventos de forma simples e rápida, sem papelada ou complicações.",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.Normal,
                    color = AppColors().lightGrey,
                    fontSize = 12.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.81f)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        selectedFilters.value.forEach { filter ->
                            FilterChip(text = filter.label) {
                                selectedFilters.value = selectedFilters.value - filter
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable { showFilterDialog.value = true }
                                .padding(start = 4.dp)
                        ) {
                            AppIcons.Outline.Filter(24.dp)
                        }

                        Box(
                            modifier = Modifier
                                .clickable { }
                        ) {
                            AppIcons.Outline.FileGenerator(24.dp)
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        AppColors().offWhite,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (filteredEvents.isEmpty()) {
                        Text(
                            text = "Nenhum evento encontrado com os filtros selecionados.",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = AppColors().lightBlack
                        )
                    } else {
                        filteredEvents.forEach { event ->
                            EventCard(event)
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                    }
                }
            }

            NavBar(navController, NavBarOption.CALENDAR, user)
        }

        if (showFilterDialog.value) {
            FilterDialog(
                selected = selectedFilters.value,
                onDismiss = { showFilterDialog.value = false },
                onConfirm = {
                    selectedFilters.value = it
                    showFilterDialog.value = false
                }
            )
        }
    }
}

@Composable
fun FilterChip(text: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(end = 8.dp)
            .background(AppColors().lightGreen, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            color = AppColors().black
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onRemove, modifier = Modifier.size(18.dp)) {
            AppIcons.Outline.Erase(18.dp)
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    onEventDetails: () -> Unit = {}
) {
    val currentEvent = event.eventStage == Event.EventStage.CURRENT
    val backgroundColor = if (currentEvent) AppColors().darkGreen else AppColors().white
    val titleColor = if (currentEvent) AppColors().white else AppColors().black
    val dividerColor = if (currentEvent) AppColors().darkGrey else AppColors().lightBlack
    val subTitleColor = if (currentEvent) AppColors().lightGrey else AppColors().lightBlack
    val seeMoreColor = if (currentEvent) AppColors().lightGreen else AppColors().darkGreen
    val helper = AppDateHelper()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = event.title,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = titleColor
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = dividerColor)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = helper.getFormattedDate(event.beginTime),
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = subTitleColor
            )
            Text(
                text = "${helper.getTimeFormatted(event.beginTime)} - ${
                    helper.getTimeFormatted(
                        event.endTime
                    )
                } | ${event.type}",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = subTitleColor
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(thickness = 1.dp, color = dividerColor)

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Ver mais >>",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = seeMoreColor,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { onEventDetails() }
                    .padding(0.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EventsPreview() {
    val sampleEvents = listOf(
        Event(
            1,
            "INF 311 - Programação Dispositivos móveis",
            "Aula prática de Kotlin",
            "Online",
            Event.EventVerificationMethod.QR_CODE,
            "ABC123",
            false,
            "Localização",
            AppDateHelper().getDate(2025, 2, 25),
            AppDateHelper().getDate(2025, 2, 25),
            null,
            null,
            null,
            null,
            Event.EventStage.CURRENT,
            listOf()
        ),
        Event(
            2,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Presencial",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            false,
            "Localização",
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT,
            listOf()
        ),
        Event(
            2,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Presencial",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            false,
            "Localização",
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT,
            listOf()
        ),
        Event(
            3,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Online",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            false,
            "Localização",
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT,
            listOf()
        ),
        Event(
            4,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Online",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            false,
            "Localização",
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT,
            listOf()
        ),
        Event(
            5,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Presencial",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            false,
            "Localização",
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT,
            listOf()
        )
    )

    EventsScreen(
        navController = rememberNavController(), allEvents = sampleEvents, user = User(
            0,
            "Erick Soares",
            User.UserRole.USER,
            "teste@teste.com",
            "12345678900",
            "Universidade Federal de Viçosa (UFV)",
            "****",
            true,
            null
        )
    )
}
