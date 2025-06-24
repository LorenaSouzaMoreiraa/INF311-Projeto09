package com.example.inf311_projeto09.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.ui.components.*
import com.example.inf311_projeto09.ui.utils.*

enum class EventFilter(val label: String) {
    ONLINE("Online"),
    PRESENCIAL("Presencial"),
    HIBRIDO("Híbrido"),
    EM_PROGRESSO("Em progresso"),
    PROXIMOS_EVENTOS("Pendente"),
    FINALIZADO("Finalizado"),
    NESTE_MES("Neste mês"),
    ULTIMOS_TRES_MESES("Nos últimos 3 meses"),
    ULTIMOS_SEIS_MESES("Nos últimos 6 meses"),
    NESTE_ANO("Neste ano"),
    ANOS_ANTERIORES("Nos anos anteriores")
}

@Composable
fun EventsScreen(
    navController: NavHostController,
    todayEvents: List<Event> = emptyList()
) {
    val currentEvent = todayEvents.firstOrNull { it.eventStage == Event.EventStage.CURRENT }
    val scannedCode = remember { mutableStateOf<String?>(null) }
    val selectedFilters = remember { mutableStateOf(setOf<EventFilter>()) }
    val showFilterDialog = remember { mutableStateOf(false) }

    LaunchedEffect(navController) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<String>("scannedCode")?.observeForever { code ->
            scannedCode.value = code
            savedStateHandle.remove<String>("scannedCode")
        }
    }

    LaunchedEffect(scannedCode.value) {
        val code = scannedCode.value
        if (code != null) {
            scannedCode.value = null
            if (currentEvent != null && code == currentEvent.checkInCode) {
                val checkTime = AppDateHelper().getCurrentCompleteTime()
                if (currentEvent.checkInTime == null) {
                    RubeusApi.checkIn(22, currentEvent, checkTime)
                } else if (currentEvent.checkOutTime == null) {
                    RubeusApi.checkOut(22, currentEvent, checkTime)
                }
            }
        }
    }

    EventsContent(navController, todayEvents, selectedFilters, showFilterDialog)
}

@Composable
fun EventsContent(
    navController: NavHostController,
    todayEvents: List<Event>,
    selectedFilters: MutableState<Set<EventFilter>>,
    showFilterDialog: MutableState<Boolean>
) {
    val helper = AppDateHelper()

    val filteredEvents = remember(todayEvents, selectedFilters.value) {
        if (selectedFilters.value.isEmpty()) {
            todayEvents
        } else {
            todayEvents.filter { event ->
                selectedFilters.value.all { filter ->
                    when (filter) {
                        EventFilter.ONLINE -> event.type.contains("Online", ignoreCase = true)
                        EventFilter.PRESENCIAL -> event.type.contains("Presencial", ignoreCase = true)
                        EventFilter.HIBRIDO -> event.type.contains("Híbrido", ignoreCase = true)
                        EventFilter.EM_PROGRESSO -> event.eventStage == Event.EventStage.CURRENT
                        EventFilter.PROXIMOS_EVENTOS -> event.eventStage == Event.EventStage.NEXT
                        EventFilter.FINALIZADO -> event.eventStage == Event.EventStage.ENDED
                        EventFilter.NESTE_MES -> helper.isThisMonth(event.beginTime)
                        EventFilter.ULTIMOS_TRES_MESES -> helper.isInLastNMonths(event.beginTime, 3)
                        EventFilter.ULTIMOS_SEIS_MESES -> helper.isInLastNMonths(event.beginTime, 6)
                        EventFilter.NESTE_ANO -> helper.isThisYear(event.beginTime)
                        EventFilter.ANOS_ANTERIORES -> !helper.isThisYear(event.beginTime)
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

            NavBar(navController, NavBarOption.CALENDAR)
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
){
    val helper = AppDateHelper()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(AppColors().white, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = event.title,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = AppColors().black
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = AppColors().lightBlack, thickness = 1.dp)

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
                color = AppColors().lightBlack
            )
            Text(
                text = "${helper.getTimeFormatted(event.beginTime)} - ${helper.getTimeFormatted(event.endTime)} | ${event.type}",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = AppColors().lightBlack
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = AppColors().lightBlack, thickness = 1.dp)

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
                color = AppColors().darkGreen,
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
            "XYZ789",
            0,
            AppDateHelper().getDate(2025, 2, 25),
            AppDateHelper().getDate(2025, 2, 25),
            null,
            null,
            null,
            null,
            Event.EventStage.CURRENT
        ),
        Event(
            2,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Presencial",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            "UVW123",
            0,
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT
        ),
        Event(
            2,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Presencial",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            "UVW123",
            0,
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT
        ),
        Event(
            3,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Online",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            "UVW123",
            0,
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT
        ),
        Event(
            4,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Online",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            "UVW123",
            0,
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT
        ),
        Event(
            5,
            "INF 410 - Tópicos Especiais",
            "Painel com convidados",
            "Presencial",
            Event.EventVerificationMethod.QR_CODE,
            "DEF456",
            "UVW123",
            0,
            AppDateHelper().getDate(2025, 2, 26),
            AppDateHelper().getDate(2025, 2, 26),
            null,
            null,
            null,
            null,
            Event.EventStage.NEXT
        )
    )

    EventsScreen(navController = rememberNavController(), todayEvents = sampleEvents)
}
