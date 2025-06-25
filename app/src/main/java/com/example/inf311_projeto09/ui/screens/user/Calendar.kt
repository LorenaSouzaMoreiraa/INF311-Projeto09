package com.example.inf311_projeto09.ui.screens.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.model.Event.EventStage
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.ScreenType
import com.example.inf311_projeto09.ui.components.EmptyEventCard
import com.example.inf311_projeto09.ui.components.NavBar
import com.example.inf311_projeto09.ui.components.NavBarOption
import com.example.inf311_projeto09.ui.components.ReusableDatePickerDialog
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Date

@Composable
fun CalendarScreen(
    user: User,
    navController: NavHostController,
    allEvents: List<Event>
) {
    val today = remember { Calendar.getInstance() }
    val calendarViewModel = remember { CalendarViewModel() }
    var selectedDate by remember { mutableStateOf(today) }
    val lazyListState = rememberLazyListState()
    val currentEvent = AppDateHelper().getCurrentEvent(allEvents)

    var eventsForSelectedDay = AppDateHelper().getEventsForDate(allEvents, selectedDate.time)

    val scrollToToday: () -> Unit = {
        selectedDate = today
        eventsForSelectedDay = AppDateHelper().getEventsForDate(allEvents, today.time)
        calendarViewModel.setToWeekOf(today)
    }

    val scannedCode = remember {
        mutableStateOf<String?>(null)
    }

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
                    RubeusApi.checkIn(user.id, currentEvent, checkTime)
                } else if (currentEvent.checkOutTime == null) {
                    RubeusApi.checkOut(user.id, currentEvent, checkTime)
                }
            } else {
                // TODO: código inválido, fazer também outras mensagens, igual checkout precisa de checkin
            }
        }
    }

    LaunchedEffect(Unit) {
        scrollToToday()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CalendarTopBarSection(
                onPinClick = scrollToToday,
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    eventsForSelectedDay = AppDateHelper().getEventsForDate(allEvents, date.time)
                    calendarViewModel.setToWeekOf(date)
                },
                calendarViewModel = calendarViewModel
            )

            Spacer(modifier = Modifier.height(15.dp))

            DaySelectorSection(
                selectedDay = selectedDate,
                calendarViewModel = calendarViewModel,
                lazyListState = lazyListState,
                onDaySelected = { date ->
                    selectedDate = date
                    eventsForSelectedDay = AppDateHelper().getEventsForDate(allEvents, date.time)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        AppColors().offWhite,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                DayEventsSection(events = eventsForSelectedDay)

                Spacer(modifier = Modifier.height(20.dp))
            }

            NavBar(
                navController = navController,
                NavBarOption.CALENDAR,
                user
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 80.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors().darkGreen)
                .clickable {
                    if (currentEvent?.verificationMethod == Event.EventVerificationMethod.VERIFICATION_CODE)
                        navController.navigate(ScreenType.VERIFICATION_CODE.route)
                    else if (currentEvent?.verificationMethod == Event.EventVerificationMethod.QR_CODE)
                        navController.navigate(ScreenType.QR_SCANNER.route)
                    // TODO: colocar mensagem que não tem evento atual
                    // TODO: tela de checkout (não precisa de código)
                },
            contentAlignment = Alignment.Center
        ) {
            AppIcons.Outline.Target(25.dp, AppColors().lightGreen)
        }
    }
}

@Composable
fun CalendarTopBarSection(
    onPinClick: () -> Unit,
    selectedDate: Calendar,
    onDateSelected: (Calendar) -> Unit,
    calendarViewModel: CalendarViewModel
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPinClick,
            modifier = Modifier.size(24.dp)
        ) {
            AppIcons.Outline.Pin(24.dp)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = calendarViewModel.getVisibleYears(),
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().lightGrey,
                fontSize = 12.sp
            )

            Text(
                text = calendarViewModel.getVisibleMonths(),
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().white,
                fontSize = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { showDatePickerDialog = true }
        ) {
            AppIcons.Outline.CalendarDays(24.dp)
        }
    }

    ReusableDatePickerDialog(
        showDialog = showDatePickerDialog,
        initialDate = selectedDate,
        onDismiss = { showDatePickerDialog = false },
        onDateSelected = {
            onDateSelected(it)
        })
}

@Composable
fun DaySelectorSection(
    selectedDay: Calendar,
    calendarViewModel: CalendarViewModel,
    lazyListState: LazyListState,
    onDaySelected: (Calendar) -> Unit
) {
    val scope = rememberCoroutineScope()

    val onPreviousWeekClick: () -> Unit = {
        scope.launch {
            val firstVisibleDay = calendarViewModel.allDays[lazyListState.firstVisibleItemIndex]
            val previousWeek = firstVisibleDay.clone() as Calendar
            previousWeek.add(Calendar.DAY_OF_MONTH, -7)
            calendarViewModel.setToWeekOf(previousWeek)
        }
    }

    val onNextWeekClick: () -> Unit = {
        scope.launch {
            scope.launch {
                val firstVisibleDay = calendarViewModel.allDays[lazyListState.firstVisibleItemIndex]
                val nextWeek = firstVisibleDay.clone() as Calendar
                nextWeek.add(Calendar.DAY_OF_MONTH, 7)
                calendarViewModel.setToWeekOf(nextWeek)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPreviousWeekClick) {
                AppIcons.Outline.CircleArrowLeft(24.dp)
            }

            LazyRow(
                state = lazyListState,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(calendarViewModel.allDays) { dayItem ->
                    val isSelected = AppDateHelper().isSameDay(dayItem.time, selectedDay.time)

                    DayItemCard(dayItem = dayItem, isSelected = isSelected) {
                        onDaySelected(dayItem)
                    }
                }
            }

            IconButton(onClick = onNextWeekClick) {
                AppIcons.Outline.CircleArrowRight(24.dp)
            }
        }
    }
}

@Composable
fun DayItemCard(
    dayItem: Calendar,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) AppColors().lightGreen else AppColors().transparent
    val titleTextColor = if (isSelected) AppColors().black else AppColors().white
    val subtitleTextColor = if (isSelected) AppColors().black else AppColors().lightGrey

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = AppDateHelper().getWeekdayLetter(dayItem),
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            color = subtitleTextColor,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = AppDateHelper().getDayNumber(dayItem),
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = titleTextColor,
            fontSize = 18.sp
        )
    }
}

@Composable
fun DayEventsSection(
    events: List<Event>
) {
    if (events.isNotEmpty()) {
        val sortedEvents = events.sortedBy { it.beginTime }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            sortedEvents.forEach { event ->
                DayEventsSectionItem(
                    event.checkInTime,
                    "Check-in",
                    event.title,
                    event.eventStage == EventStage.CURRENT && event.checkInTime == null
                )

                DayEventsSectionItem(
                    event.checkOutTime,
                    "Check-out",
                    event.title,
                    event.eventStage == EventStage.CURRENT && event.checkInTime != null && event.checkOutTime == null
                )
            }
        }
    } else {
        EmptyEventCard("Nenhum evento agendado para esta data.")
    }
}

@Composable
fun DayEventsSectionItem(
    checkTime: Date?,
    checkType: String,
    eventTitle: String,
    isCurrentEvent: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(AppColors().darkGreen)
            )

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(AppColors().grey)
            )
        }

        Column(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .weight(1f)
                .border(
                    BorderStroke(
                        width = 1.dp,
                        color = if (isCurrentEvent) AppColors().darkGreen else AppColors().transparent
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = AppDateHelper().getTimeFormattedWithSeconds(checkTime),
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().darkGreen,
                fontSize = 12.sp
            )

            Text(
                text = checkType,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().grey,
                fontSize = 12.sp
            )

            Text(
                text = eventTitle,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 12.sp
            )
        }
    }
}

class CalendarViewModel {
    private val _allDays = mutableStateListOf<Calendar>()
    val allDays: List<Calendar> get() = _allDays

    init {
        val today = Calendar.getInstance()
        _allDays.addAll(generate7DaysAround(today))
    }

    private fun generate7DaysAround(reference: Calendar): List<Calendar> {
        val days = mutableListOf<Calendar>()
        val start = reference.clone() as Calendar
        start.add(Calendar.DAY_OF_MONTH, -3)

        for (i in 0 until 7) {
            val day = start.clone() as Calendar
            day.add(Calendar.DAY_OF_MONTH, i)
            days.add(day)
        }
        return days
    }

    fun setToWeekOf(date: Calendar) {
        _allDays.clear()
        val monday = date.clone() as Calendar
        while (monday[Calendar.DAY_OF_WEEK] != Calendar.MONDAY) {
            monday.add(Calendar.DAY_OF_MONTH, -1)
        }
        for (i in 0 until 7) {
            val day = monday.clone() as Calendar
            day.add(Calendar.DAY_OF_MONTH, i)
            _allDays.add(day)
        }
    }

    fun getVisibleMonths(): String {
        val uniqueMonths = getUniqueMonths()

        return when (uniqueMonths.size) {
            1 -> {
                val (month, _) = uniqueMonths.first()
                getMonthName(month)
            }

            2 -> {
                val (month1, _) = uniqueMonths[0]
                val (month2, _) = uniqueMonths[1]
                "${getMonthName(month1)} - ${getMonthName(month2)}"
            }

            else -> {
                ""
            }
        }
    }

    fun getVisibleYears(): String {
        val uniqueMonths = getUniqueMonths()

        return when (uniqueMonths.size) {
            1 -> {
                val (_, year) = uniqueMonths.first()
                "$year"
            }

            2 -> {
                val (_, year1) = uniqueMonths[0]
                val (_, year2) = uniqueMonths[1]

                if (year1 == year2) "$year1"
                else "$year1 - $year2"
            }

            else -> {
                ""
            }
        }
    }

    private fun getUniqueMonths(): List<Pair<Int, Int>> {
        return _allDays.map {
            it[Calendar.MONTH] to it[Calendar.YEAR]
        }.distinct().sortedWith(compareBy({ it.second }, { it.first }))
    }

    private fun getMonthName(month: Int): String {
        val locale = AppDateHelper.LOCALE_PT_BR
        return DateFormatSymbols(locale).months[month].replaceFirstChar { it.uppercaseChar() }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarScreenPreview() {
    CalendarScreen(
        user = User(0, "Erick", User.UserRole.USER, "teste@teste.com", "cpf", "UFV", "****", true),
        navController = rememberNavController(),
        allEvents = emptyList()
    )
}