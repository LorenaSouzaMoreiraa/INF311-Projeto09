package com.example.inf311_projeto09.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.model.Event.EventVerificationMethod
import com.example.inf311_projeto09.ui.components.ReusableDatePickerDialog
import com.example.inf311_projeto09.ui.components.ReusableTimePickerDialog
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EventDetailsScreen(
    navController: NavHostController,
    event: Event
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale("pt", "BR")) }

    val focusManager: FocusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var eventName by remember { mutableStateOf(event.title) }
    var startDateCalendar by remember {
        mutableStateOf(
            Calendar.getInstance().apply { time = event.beginTime })
    }
    var startTimeCalendar by remember {
        mutableStateOf(
            Calendar.getInstance().apply { time = event.beginTime })
    }
    var endDateCalendar by remember {
        mutableStateOf(
            Calendar.getInstance().apply { time = event.endTime })
    }
    var endTimeCalendar by remember {
        mutableStateOf(
            Calendar.getInstance().apply { time = event.endTime })
    }
    var selectedEventType by remember { mutableStateOf(event.type) }
    var selectedAuthMethod by remember {
        mutableStateOf(
            when (event.verificationMethod) {
                EventVerificationMethod.QR_CODE -> EventVerificationMethod.QR_CODE
                EventVerificationMethod.VERIFICATION_CODE -> EventVerificationMethod.VERIFICATION_CODE
                else -> EventVerificationMethod.NONE
            }
        )
    }
    var autoCheck by remember { mutableStateOf(event.autoCheck) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var eventTypeExpanded by remember { mutableStateOf(false) }

    var startDateText by remember { mutableStateOf(dateFormatter.format(event.beginTime)) }
    var startTimeText by remember { mutableStateOf(timeFormatter.format(event.beginTime)) }
    var endDateText by remember { mutableStateOf(dateFormatter.format(event.endTime)) }
    var endTimeText by remember { mutableStateOf(timeFormatter.format(event.endTime)) }

    var isEditingMode by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Dados") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            )
            .focusable(false)
    ) {
        ScreenHeader(onBack = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth()
                .background(AppColors().darkGreen)
        ) {
            ScreenTitleAndSubtitle(eventName = eventName)

            Spacer(modifier = Modifier.height(30.dp))

            MainContent(
                eventName = eventName,
                onEventNameChange = { eventName = it },
                startDateText = startDateText,
                onShowStartDatePicker = { showStartDatePicker = true },
                startTimeText = startTimeText,
                onShowStartTimePicker = { showStartTimePicker = true },
                endDateText = endDateText,
                onShowEndDatePicker = { showEndDatePicker = true },
                endTimeText = endTimeText,
                onShowEndTimePicker = { showEndTimePicker = true },
                eventTypeExpanded = eventTypeExpanded,
                onEventTypeExpandedChange = { eventTypeExpanded = it },
                eventTypes = listOf("Online", "Presencial", "Híbrido"),
                selectedEventType = selectedEventType,
                onSelectedEventTypeChange = { selectedEventType = it },
                selectedAuthMethod = selectedAuthMethod,
                onSelectedAuthMethodChange = { selectedAuthMethod = it },
                autoCheckInOut = autoCheck,
                onAutoCheckInOutChange = { autoCheck = it },
                scrollState = scrollState,
                isEditingMode = isEditingMode,
                onToggleEditing = { isEditingMode = !isEditingMode },
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }

    DateAndTimePickers(
        showStartDatePicker = showStartDatePicker,
        onDismissStartDatePicker = { showStartDatePicker = false },
        initialStartDate = startDateCalendar,
        onStartDateSelected = { selectedCal ->
            startDateCalendar = selectedCal
            startDateText = dateFormatter.format(selectedCal.time)
            showStartDatePicker = false
        },
        showStartTimePicker = showStartTimePicker,
        onDismissStartTimePicker = { showStartTimePicker = false },
        initialStartTime = startTimeCalendar,
        onStartTimeSelected = { hour, minute ->
            val newTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            startTimeCalendar = newTime
            startTimeText = timeFormatter.format(newTime.time)
            showStartTimePicker = false
        },
        showEndDatePicker = showEndDatePicker,
        onDismissEndDatePicker = { showEndDatePicker = false },
        initialEndDate = endDateCalendar,
        onEndDateSelected = { selectedCal ->
            endDateCalendar = selectedCal
            endDateText = dateFormatter.format(selectedCal.time)
            showEndDatePicker = false
        },
        showEndTimePicker = showEndTimePicker,
        onDismissEndTimePicker = { showEndTimePicker = false },
        initialEndTime = endTimeCalendar,
        onEndTimeSelected = { hour, minute ->
            val newTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            endTimeCalendar = newTime
            endTimeText = timeFormatter.format(newTime.time)
            showEndTimePicker = false
        }
    )
}

@Composable
fun ScreenHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(30.dp)
        ) {
            AppIcons.Outline.CircleArrowLeft(30.dp, AppColors().white)
        }
    }
}

@Composable
fun ScreenTitleAndSubtitle(eventName: String) {
    Text(
        text = eventName,
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.SemiBold,
        color = AppColors().white,
        fontSize = 25.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 30.dp)
    )

    Spacer(modifier = Modifier.height(5.dp))

    Text(
        text = "Gerencie e registre presenças em eventos de forma simples e rápida, sem papelada ou complicações.",
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.Medium,
        color = AppColors().lightGrey,
        fontSize = 12.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 30.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    startDateText: String,
    onShowStartDatePicker: () -> Unit,
    startTimeText: String,
    onShowStartTimePicker: () -> Unit,
    endDateText: String,
    onShowEndDatePicker: () -> Unit,
    endTimeText: String,
    onShowEndTimePicker: () -> Unit,
    eventTypeExpanded: Boolean,
    onEventTypeExpandedChange: (Boolean) -> Unit,
    eventTypes: List<String>,
    selectedEventType: String,
    onSelectedEventTypeChange: (String) -> Unit,
    selectedAuthMethod: EventVerificationMethod,
    onSelectedAuthMethodChange: (EventVerificationMethod) -> Unit,
    autoCheckInOut: Boolean,
    onAutoCheckInOutChange: (Boolean) -> Unit,
    scrollState: androidx.compose.foundation.ScrollState,
    isEditingMode: Boolean,
    onToggleEditing: () -> Unit,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val customDropdownShape =
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AppColors().offWhite,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTab = selectedTab, onTabSelected = onTabSelected)

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onToggleEditing() }
                    ) {
                        AppIcons.Outline.EditIcon(
                            boxSize = 24.dp,
                            colorIcon = if (isEditingMode) AppColors().green else AppColors().black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                when (selectedTab) {
                    "Dados" -> {
                        EventNameField(
                            eventName = eventName,
                            onEventNameChange = onEventNameChange,
                            isEditingMode = isEditingMode
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        DateTimeFields(
                            isStartDateTime = true,
                            startDateText = startDateText,
                            onShowStartDatePicker = onShowStartDatePicker,
                            startTimeText = startTimeText,
                            onShowStartTimePicker = onShowStartTimePicker,
                            isEditingMode = isEditingMode
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        DateTimeFields(
                            isStartDateTime = false,
                            startDateText = endDateText,
                            onShowStartDatePicker = onShowEndDatePicker,
                            startTimeText = endTimeText,
                            onShowStartTimePicker = onShowEndTimePicker,
                            isEditingMode = isEditingMode
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        EventTypeDropdown(
                            eventTypeExpanded = eventTypeExpanded,
                            onEventTypeExpandedChange = onEventTypeExpandedChange,
                            eventTypes = eventTypes,
                            selectedEventType = selectedEventType,
                            onSelectedEventTypeChange = onSelectedEventTypeChange,
                            customDropdownShape = customDropdownShape,
                            isEditingMode = isEditingMode
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        AuthMethodSelection(
                            selectedAuthMethod = selectedAuthMethod,
                            onSelectedAuthMethodChange = onSelectedAuthMethodChange,
                            isEditingMode = isEditingMode
                        )

                        AutoCheckInOutCheckbox(
                            autoCheckInOut = autoCheckInOut,
                            onAutoCheckInOutChange = onAutoCheckInOutChange,
                            isEditingMode = isEditingMode
                        )

                        if (isEditingMode) {
                            Spacer(modifier = Modifier.height(10.dp))

                            ActionButton(onSaveClick = {
                                // TODO: implementar logica para salvar alterações
                            })
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    "Participantes" -> {
                        Text(
                            text = "Conteúdo da aba Participantes",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = AppColors().black,
                            modifier = Modifier.padding(top = 50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventNameField(eventName: String, onEventNameChange: (String) -> Unit, isEditingMode: Boolean) {
    OutlinedTextField(
        value = eventName,
        onValueChange = onEventNameChange,
        label = {
            Text(
                "Nome do evento",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = AppColors().grey
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AppColors().black,
            unfocusedTextColor = AppColors().black,
            focusedBorderColor = AppColors().lightGrey,
            unfocusedBorderColor = AppColors().lightGrey,
            cursorColor = AppColors().black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        readOnly = !isEditingMode
    )
}

@Composable
fun DateTimeFields(
    isStartDateTime: Boolean,
    startDateText: String,
    onShowStartDatePicker: () -> Unit,
    startTimeText: String,
    onShowStartTimePicker: () -> Unit,
    isEditingMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .clickable { onShowStartDatePicker() }
        ) {
            OutlinedTextField(
                value = startDateText,
                onValueChange = {},
                label = {
                    Text(
                        text = if (isStartDateTime) "Data início" else "Data fim",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = AppColors().grey
                    )
                },
                readOnly = true,
                enabled = !isEditingMode,
                leadingIcon = {
                    AppIcons.Outline.CalendarField(24.dp, AppColors().black)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppColors().black,
                    unfocusedTextColor = AppColors().black,
                    focusedBorderColor = AppColors().lightGrey,
                    unfocusedBorderColor = AppColors().lightGrey,
                    cursorColor = AppColors().black,
                    disabledTextColor = AppColors().black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onShowStartTimePicker() }
        ) {
            OutlinedTextField(
                value = startTimeText,
                onValueChange = {},
                label = {
                    Text(
                        text = if (isStartDateTime) "Hora início" else "Hora fim",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = AppColors().grey
                    )
                },
                readOnly = true,
                enabled = !isEditingMode,
                leadingIcon = {
                    AppIcons.Outline.Clock(24.dp, AppColors().black)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppColors().black,
                    unfocusedTextColor = AppColors().black,
                    focusedBorderColor = AppColors().lightGrey,
                    unfocusedBorderColor = AppColors().lightGrey,
                    cursorColor = AppColors().black,
                    disabledTextColor = AppColors().black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTypeDropdown(
    eventTypeExpanded: Boolean,
    onEventTypeExpandedChange: (Boolean) -> Unit,
    eventTypes: List<String>,
    selectedEventType: String,
    onSelectedEventTypeChange: (String) -> Unit,
    customDropdownShape: RoundedCornerShape,
    isEditingMode: Boolean
) {
    if (isEditingMode) {
        ExposedDropdownMenuBox(
            expanded = eventTypeExpanded,
            onExpandedChange = onEventTypeExpandedChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedEventType,
                onValueChange = { },
                readOnly = true,
                label = {
                    Text(
                        "Tipo do evento",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = AppColors().grey
                    )
                },
                trailingIcon = {
                    AppIcons.Outline.ArrowDown(
                        boxSize = 24.dp,
                        colorIcon = AppColors().grey,
                        modifier = Modifier.graphicsLayer(rotationZ = if (eventTypeExpanded) 180f else 0f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppColors().black,
                    unfocusedTextColor = AppColors().black,
                    focusedBorderColor = AppColors().lightGrey,
                    unfocusedBorderColor = AppColors().lightGrey,
                    cursorColor = AppColors().black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = eventTypeExpanded,
                onDismissRequest = { onEventTypeExpandedChange(false) },
                modifier = Modifier
                    .background(AppColors().offWhite),
                shape = customDropdownShape
            ) {
                eventTypes.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                fontFamily = AppFonts().montserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = AppColors().black
                            )
                        },
                        onClick = {
                            onSelectedEventTypeChange(item)
                            onEventTypeExpandedChange(false)
                        }
                    )

                    if (index < eventTypes.size - 1) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.8.dp)
                                .background(AppColors().lightGrey)
                        )
                    }
                }
            }
        }
    } else {
        OutlinedTextField(
            value = selectedEventType,
            onValueChange = {},
            label = {
                Text(
                    "Tipo do evento",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = AppColors().grey
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppColors().black,
                unfocusedTextColor = AppColors().black,
                focusedBorderColor = AppColors().lightGrey,
                unfocusedBorderColor = AppColors().lightGrey,
                cursorColor = AppColors().black
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
    }
}

@Composable
fun AuthMethodSelection(
    selectedAuthMethod: EventVerificationMethod,
    onSelectedAuthMethodChange: (EventVerificationMethod) -> Unit,
    isEditingMode: Boolean
) {
    Text(
        text = "Método de autenticação",
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.SemiBold,
        color = AppColors().black,
        fontSize = 16.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = (selectedAuthMethod == EventVerificationMethod.QR_CODE),
            onCheckedChange = { isChecked ->
                if (isChecked) onSelectedAuthMethodChange(EventVerificationMethod.QR_CODE)
                else if (selectedAuthMethod == EventVerificationMethod.QR_CODE) onSelectedAuthMethodChange(
                    EventVerificationMethod.NONE
                )
            },
            enabled = isEditingMode,
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors().darkGreen,
                uncheckedColor = AppColors().lightGrey,
                checkmarkColor = AppColors().darkGreen,
                disabledCheckedColor = AppColors().darkGreen,
                disabledUncheckedColor = AppColors().lightGrey
            ),
            modifier = Modifier.offset(x = (-13).dp)
        )

        Text(
            text = "QrCode",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = AppColors().black,
            modifier = Modifier
                .clickable { onSelectedAuthMethodChange(if (selectedAuthMethod == EventVerificationMethod.QR_CODE) EventVerificationMethod.NONE else EventVerificationMethod.QR_CODE) }
                .offset(x = (-13).dp)
        )

        Spacer(modifier = Modifier.width(70.dp))

        Checkbox(
            checked = (selectedAuthMethod == EventVerificationMethod.VERIFICATION_CODE),
            onCheckedChange = { isChecked ->
                if (isChecked) onSelectedAuthMethodChange(EventVerificationMethod.VERIFICATION_CODE)
                else if (selectedAuthMethod == EventVerificationMethod.VERIFICATION_CODE) onSelectedAuthMethodChange(
                    EventVerificationMethod.NONE
                )
            },
            enabled = isEditingMode,
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors().darkGreen,
                uncheckedColor = AppColors().lightGrey,
                checkmarkColor = AppColors().darkGreen,
                disabledCheckedColor = AppColors().darkGreen,
                disabledUncheckedColor = AppColors().lightGrey
            )
        )

        Text(
            text = "Código único",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = AppColors().black,
            modifier = Modifier.clickable {
                onSelectedAuthMethodChange(if (selectedAuthMethod == EventVerificationMethod.VERIFICATION_CODE) EventVerificationMethod.NONE else EventVerificationMethod.VERIFICATION_CODE)
            }
        )
    }
}

@Composable
fun AutoCheckInOutCheckbox(
    autoCheckInOut: Boolean,
    onAutoCheckInOutChange: (Boolean) -> Unit,
    isEditingMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = autoCheckInOut,
            onCheckedChange = onAutoCheckInOutChange,
            enabled = isEditingMode,
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors().darkGreen,
                uncheckedColor = AppColors().lightGrey,
                checkmarkColor = AppColors().darkGreen,
                disabledCheckedColor = AppColors().darkGreen,
                disabledUncheckedColor = AppColors().lightGrey
            ),
            modifier = Modifier.offset(x = (-13).dp)
        )

        Text(
            text = "Iniciar Check-in e Check-out automáticos",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = AppColors().black,
            modifier = Modifier
                .clickable { onAutoCheckInOutChange(!autoCheckInOut) }
                .offset(x = (-13).dp)
        )
    }
}

@Composable
fun ActionButton(onSaveClick: () -> Unit) {
    Button(
        onClick = {
            onSaveClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors().green
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Salvar alterações",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().black,
            fontSize = 16.sp
        )
    }
}

@Composable
fun DateAndTimePickers(
    showStartDatePicker: Boolean,
    onDismissStartDatePicker: () -> Unit,
    initialStartDate: Calendar?,
    onStartDateSelected: (Calendar) -> Unit,
    showStartTimePicker: Boolean,
    onDismissStartTimePicker: () -> Unit,
    initialStartTime: Calendar?,
    onStartTimeSelected: (Int, Int) -> Unit,
    showEndDatePicker: Boolean,
    onDismissEndDatePicker: () -> Unit,
    initialEndDate: Calendar?,
    onEndDateSelected: (Calendar) -> Unit,
    showEndTimePicker: Boolean,
    onDismissEndTimePicker: () -> Unit,
    initialEndTime: Calendar?,
    onEndTimeSelected: (Int, Int) -> Unit
) {
    if (showStartDatePicker) {
        ReusableDatePickerDialog(
            showDialog = showStartDatePicker,
            initialDate = initialStartDate ?: Calendar.getInstance(),
            onDismiss = onDismissStartDatePicker,
            onDateSelected = onStartDateSelected
        )
    }

    if (showStartTimePicker) {
        ReusableTimePickerDialog(
            showDialog = showStartTimePicker,
            initialHour = initialStartTime?.get(Calendar.HOUR_OF_DAY) ?: Calendar.getInstance()
                .get(Calendar.HOUR_OF_DAY),
            initialMinute = initialStartTime?.get(Calendar.MINUTE) ?: Calendar.getInstance()
                .get(Calendar.MINUTE),
            onDismiss = onDismissStartTimePicker,
            onTimeSelected = onStartTimeSelected
        )
    }

    if (showEndDatePicker) {
        ReusableDatePickerDialog(
            showDialog = showEndDatePicker,
            initialDate = initialEndDate ?: Calendar.getInstance(),
            onDismiss = onDismissEndDatePicker,
            onDateSelected = onEndDateSelected
        )
    }

    if (showEndTimePicker) {
        ReusableTimePickerDialog(
            showDialog = showEndTimePicker,
            initialHour = initialEndTime?.get(Calendar.HOUR_OF_DAY) ?: Calendar.getInstance()
                .get(Calendar.HOUR_OF_DAY),
            initialMinute = initialEndTime?.get(Calendar.MINUTE) ?: Calendar.getInstance()
                .get(Calendar.MINUTE),
            onDismiss = onDismissEndTimePicker,
            onTimeSelected = onEndTimeSelected
        )
    }
}

@Composable
fun TabRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Dados", "Participantes")
    val selectedTabIndex = tabs.indexOf(selectedTab)

    var tabRowWidthPx by remember { mutableIntStateOf(0) }
    var tabWidthPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    val indicatorWidth: Dp by remember(tabWidthPx) {
        derivedStateOf { with(density) { tabWidthPx.toDp() } }
    }

    val indicatorOffset by animateDpAsState(
        targetValue = if (selectedTabIndex == 0) 0.dp else indicatorWidth,
        animationSpec = tween(durationMillis = 300),
        label = "indicatorOffsetAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                AppColors().offWhite,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
            .onGloballyPositioned { coordinates ->
                tabRowWidthPx = coordinates.size.width
                tabWidthPx = tabRowWidthPx / tabs.size
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(1.dp)
                .background(AppColors().lightGrey)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tabText ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(tabText) }
                        )
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = tabText,
                        fontFamily = AppFonts().montserrat,
                        fontWeight = if (selectedTab == tabText) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (selectedTab == tabText) AppColors().darkGreen else AppColors().grey,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .width(indicatorWidth)
                .height(2.dp)
                .offset(x = indicatorOffset)
                .background(AppColors().darkGreen)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventDetailsScreenPreview() {
    var event = Event(
        1,
        "INF 311 - Programação Dispositivos móveis",
        "Aula prática de Kotlin",
        "Online",
        EventVerificationMethod.QR_CODE,
        "ABC123",
        true,
        "XYZ789",
        AppDateHelper().getDate(2025, 2, 25),
        AppDateHelper().getDate(2025, 2, 25),
        null,
        null,
        null,
        null,
        Event.EventStage.CURRENT,
        listOf()
    )

    EventDetailsScreen(navController = rememberNavController(), event = event)
}