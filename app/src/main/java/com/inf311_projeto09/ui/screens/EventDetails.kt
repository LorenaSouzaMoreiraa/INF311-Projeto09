package com.inf311_projeto09.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.inf311_projeto09.R
import com.inf311_projeto09.api.RubeusApi
import com.inf311_projeto09.model.Event
import com.inf311_projeto09.model.Event.EventVerificationMethod
import com.inf311_projeto09.model.User
import com.inf311_projeto09.ui.components.FilterDialog
import com.inf311_projeto09.ui.components.ReusableDatePickerDialog
import com.inf311_projeto09.ui.components.ReusableTimePickerDialog
import com.inf311_projeto09.ui.utils.AppColors
import com.inf311_projeto09.ui.utils.AppDateHelper
import com.inf311_projeto09.ui.utils.AppFonts
import com.inf311_projeto09.ui.utils.AppIcons
import com.inf311_projeto09.ui.utils.AppSnackBarManager
import com.inf311_projeto09.ui.utils.rememberLocationHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class ParticipantsFilter(val label: String) {
    ABSENT("Ausentes"),
    PRESENT("Presentes"),
    LATE("Atrasados"),
    AT_TIME("No horário")
}

@Composable
fun EventDetailsScreen(
    navController: NavHostController,
    event: Event,
    user: User,
    onDelete: () -> Unit
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
    var eventDescription by remember { mutableStateOf(event.description) }
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
    val participants by remember { mutableStateOf(event.participants.toList()) }
    val participantsWithEventStatus = remember { mutableStateListOf<Pair<User, Event>>() }

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedLocationText by remember { mutableStateOf(event.location) }
    var showMapDialog by remember { mutableStateOf(false) }

    val locationHelper = rememberLocationHelper()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {}
    )

    LaunchedEffect(Unit) {
        locationHelper.getCurrentLocation(permissionLauncher)
    }

    LaunchedEffect(participants) {
        val tempParticipantsStatus = mutableListOf<Pair<User, Event>>()
        participants.forEach { email ->
            val participantUser = RubeusApi.searchUserByEmail(email)
            participantUser?.let { user ->
                val participantEvent = RubeusApi.getUserEvent(user.id, event.course)
                if (participantEvent != null) {
                    tempParticipantsStatus.add(Pair(user, participantEvent))
                }
            }
        }
        participantsWithEventStatus.clear()
        participantsWithEventStatus.addAll(tempParticipantsStatus)
    }

    var isEditingMode by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Dados") }

    val (filteredParticipants, selectedParticipantsFilters, showParticipantFilterDialog) = rememberParticipantFilterState(
        event = event
    )

    if (showParticipantFilterDialog.value) {
        FilterDialog(
            title = "Filtrar Participantes",
            filtersToShow = ParticipantsFilter.entries.map { it to it.label },
            selected = selectedParticipantsFilters.value,
            onDismiss = { showParticipantFilterDialog.value = false },
            onConfirm = { newFilters ->
                selectedParticipantsFilters.value = newFilters
                showParticipantFilterDialog.value = false
            }
        )
    }

    val currentTime = remember { Date() }
    val isBeforeStart = currentTime.before(event.beginTime)

    LaunchedEffect(isEditingMode) {
        if (!isEditingMode) {
            eventName = event.title
            startDateText = dateFormatter.format(event.beginTime)
            startTimeText = timeFormatter.format(event.beginTime)
            endDateText = dateFormatter.format(event.endTime)
            endTimeText = timeFormatter.format(event.endTime)
            selectedEventType = event.type
            eventDescription = event.description
            selectedAuthMethod = event.verificationMethod
            autoCheck = event.autoCheck
        }
    }

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
            ScreenTitleAndSubtitle(eventName = eventName, eventDescription = eventDescription)

            Spacer(modifier = Modifier.height(25.dp))

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
                eventDescription = eventDescription,
                onDescriptionChange = { eventDescription = it },
                selectedLocationText = selectedLocationText,
                onOpenMapDialog = { showMapDialog = true },
                selectedAuthMethod = selectedAuthMethod,
                onSelectedAuthMethodChange = { selectedAuthMethod = it },
                autoCheckInOut = autoCheck,
                onAutoCheckInOutChange = { autoCheck = it },
                scrollState = scrollState,
                isEditingMode = isEditingMode,
                isBeforeStart = isBeforeStart,
                onToggleEditing = { isEditingMode = !isEditingMode },
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                selectedFilters = selectedParticipantsFilters,
                showFilterDialog = showParticipantFilterDialog,
                user = user,
                participants = filteredParticipants,
                event = event,
                onDelete = onDelete
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

    if (showMapDialog) {
        LaunchedEffect(Unit) {
            val location = locationHelper.getCurrentLocation(permissionLauncher)
            if (location != null) {
                selectedLocation = location
                selectedLocationText =
                    String.format(
                        Locale.US,
                        "Lat: %.5f, Lng: %.5f",
                        location.latitude,
                        location.longitude
                    )
            } else {
                AppSnackBarManager.showMessage("Não foi possível obter a localização atual")
            }
        }

        AlertDialog(
            onDismissRequest = { showMapDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showMapDialog = false
                }) {
                    Text(
                        text = "Selecionar",
                        color = AppColors().darkGreen,
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    selectedLocationText = ""
                    showMapDialog = false
                }) {
                    Text(
                        text = "Limpar",
                        color = AppColors().darkGreen,
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            },
            text = {
                com.inf311_projeto09.ui.screens.admin.LocationSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    initialLocation = selectedLocation,
                    onLocationSelected = { latLng ->
                        selectedLocation = latLng
                        selectedLocationText =
                            String.format(
                                Locale.US,
                                "Lat: %.5f, Lng: %.5f",
                                latLng.latitude,
                                latLng.longitude
                            )
                    }
                )
            },
            containerColor = AppColors().offWhite
        )
    }
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
fun ScreenTitleAndSubtitle(eventName: String, eventDescription: String) {
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
        text = eventDescription,
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.Medium,
        color = AppColors().lightGrey,
        fontSize = 14.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 30.dp)
    )
}

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
    eventDescription: String,
    onDescriptionChange: (String) -> Unit,
    selectedLocationText: String,
    onOpenMapDialog: () -> Unit,
    selectedAuthMethod: EventVerificationMethod,
    onSelectedAuthMethodChange: (EventVerificationMethod) -> Unit,
    autoCheckInOut: Boolean,
    onAutoCheckInOutChange: (Boolean) -> Unit,
    scrollState: androidx.compose.foundation.ScrollState,
    isEditingMode: Boolean,
    isBeforeStart: Boolean,
    onToggleEditing: () -> Unit,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    selectedFilters: MutableState<Set<ParticipantsFilter>>,
    showFilterDialog: MutableState<Boolean>,
    user: User,
    participants: List<User>,
    event: Event,
    onDelete: () -> Unit
) {
    val customDropdownShape =
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp)

    var showConfirmDialog by remember { mutableStateOf(false) }

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
                when (selectedTab) {
                    "Dados" -> {
                        if (user.type == User.UserRole.ADMIN && isBeforeStart) {
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
                        } else {
                            Spacer(modifier = Modifier.height(20.dp))
                        }

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

                        DescriptionField(
                            description = eventDescription,
                            onDescriptionChange = onDescriptionChange,
                            isEditingMode = isEditingMode
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LocationField(
                            isEditingMode = isEditingMode,
                            locationText = selectedLocationText,
                            onClickSelectLocation = onOpenMapDialog
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

                        Spacer(modifier = Modifier.height(10.dp))

                        if (user.type == User.UserRole.ADMIN && isBeforeStart) {
                            ActionButton(
                                isEditingMode = isEditingMode,
                                onSaveClick = {
                                    val now = Date()

                                    when {
                                        eventName.isEmpty() -> {
                                            AppSnackBarManager.showMessage("O campo 'Nome do evento' é obrigatório")
                                            return@ActionButton
                                        }

                                        startDateText.isEmpty() -> {
                                            AppSnackBarManager.showMessage("O campo 'Data início' é obrigatório")
                                            return@ActionButton
                                        }

                                        startTimeText.isEmpty() -> {
                                            AppSnackBarManager.showMessage("O campo 'Hora início' é obrigatório")
                                            return@ActionButton
                                        }
                                    }

                                    val beginTime =
                                        AppDateHelper().getDateByDateStringAndTimeString(
                                            startDateText,
                                            startTimeText
                                        )
                                    if (beginTime.before(now)) {
                                        AppSnackBarManager.showMessage("A data e hora de início devem ser futuras")
                                        return@ActionButton
                                    }

                                    when {
                                        endDateText.isEmpty() -> {
                                            AppSnackBarManager.showMessage("O campo 'Data fim' é obrigatório")
                                            return@ActionButton
                                        }

                                        endTimeText.isEmpty() -> {
                                            AppSnackBarManager.showMessage("O campo 'Hora fim' é obrigatório")
                                            return@ActionButton
                                        }
                                    }

                                    val endTime =
                                        AppDateHelper().getDateByDateStringAndTimeString(
                                            endDateText,
                                            endTimeText
                                        )
                                    if (endTime.before(now)) {
                                        AppSnackBarManager.showMessage("A data e hora de término devem ser futuras")
                                        return@ActionButton
                                    }

                                    when {
                                        !endTime.after(beginTime) -> {
                                            AppSnackBarManager.showMessage("A data e hora de término devem ser posteriores à data e hora de início")
                                            return@ActionButton
                                        }

                                        selectedEventType.isEmpty() -> {
                                            AppSnackBarManager.showMessage("O campo 'Tipo de evento' é obrigatório")
                                            return@ActionButton
                                        }

                                        selectedEventType == "Presencial" && selectedLocationText.isEmpty() -> {
                                            AppSnackBarManager.showMessage("O campo 'Local do evento' é obrigatório")
                                            return@ActionButton
                                        }

                                        selectedAuthMethod == EventVerificationMethod.NONE -> {
                                            AppSnackBarManager.showMessage("O campo 'Método de autenticação' é obrigatório")
                                            return@ActionButton
                                        }
                                    }

                                    if (event.title == eventName &&
                                        event.beginTime == beginTime &&
                                        event.endTime == endTime &&
                                        event.type == selectedEventType &&
                                        event.description == eventDescription &&
                                        event.location == selectedLocationText &&
                                        event.verificationMethod == selectedAuthMethod &&
                                        event.autoCheck == autoCheckInOut
                                    ) {
                                        onToggleEditing()
                                        return@ActionButton
                                    }

                                    event.title = eventName
                                    event.beginTime = beginTime
                                    event.endTime = endTime
                                    event.type = selectedEventType
                                    event.description = eventDescription
                                    event.location = selectedLocationText
                                    event.verificationMethod = selectedAuthMethod
                                    event.autoCheck = autoCheckInOut
                                    RubeusApi.updateEvent(user.id, event)
                                    onToggleEditing()
                                },
                                onDeleteClick = {
                                    showConfirmDialog = true
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    "Participantes" -> {

                        if (user.type == User.UserRole.ADMIN && !isBeforeStart) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .padding(top = 0.dp)
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
                                        AppIcons.Outline.Filter(24.dp, AppColors().black)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clickable { }
                                    ) {
                                        AppIcons.Outline.FileGenerator(24.dp, AppColors().black)
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        participants
                            .forEach { participant ->
                                ParticipantItem(participant)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                    }
                }
            }
        }
    }

    if (showConfirmDialog) {
        ConfirmEnableCheckDialog(
            onConfirm = {
                showConfirmDialog = false
                RubeusApi.deleteEvent(user.id, event)
                onDelete()
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }
}

@Composable
fun ConfirmEnableCheckDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Sim",
                    color = AppColors().darkGreen,
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Não",
                    color = AppColors().darkGreen,
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        text = {
            Text(
                text = "Realmente deseja excluir o evento? Essa ação é irreversível.",
                color = AppColors().darkGreen,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        },
        containerColor = AppColors().white
    )
}

@Composable
fun ParticipantItem(participant: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(participant.imageUrl)
                .crossfade(true)
                .crossfade(300)
                .build(),
            contentDescription = "Foto do usuário",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.profile_image),
            error = painterResource(R.drawable.profile_image),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp),
        ) {
            Text(
                text = participant.name,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = participant.email,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().lightBlack,
                fontSize = 12.sp
            )
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
fun DescriptionField(
    description: String,
    onDescriptionChange: (String) -> Unit,
    isEditingMode: Boolean
) {
    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = {
            Text(
                "Descrição do evento",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = AppColors().grey
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        textStyle = LocalTextStyle.current.copy(
            fontFamily = AppFonts().montserrat,
            fontSize = 14.sp
        ),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AppColors().black,
            unfocusedTextColor = AppColors().black,
            focusedBorderColor = AppColors().lightGrey,
            unfocusedBorderColor = AppColors().lightGrey,
            cursorColor = AppColors().black,
            disabledTextColor = AppColors().black,
            disabledBorderColor = AppColors().lightGrey,
            disabledLabelColor = AppColors().grey
        ),
        readOnly = !isEditingMode,
        maxLines = Int.MAX_VALUE
    )
}

@Composable
fun LocationField(
    isEditingMode: Boolean,
    locationText: String,
    onClickSelectLocation: () -> Unit
) {
    OutlinedTextField(
        value = locationText,
        onValueChange = {},
        label = {
            Text(
                "Local do evento",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = AppColors().grey
            )
        },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = {
                if (isEditingMode) {
                    onClickSelectLocation()
                }
            }) {
                AppIcons.Outline.Map(30.dp, AppColors().black)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AppColors().black,
            unfocusedTextColor = AppColors().black,
            focusedBorderColor = AppColors().lightGrey,
            unfocusedBorderColor = AppColors().lightGrey,
            cursorColor = AppColors().black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LocationSelector(
    modifier: Modifier = Modifier,
    initialLocation: LatLng? = null,
    onLocationSelected: (LatLng) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val markerRef = remember { mutableStateOf<Marker?>(null) }
    val mapInitialized = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        mapView.onCreate(null)
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDestroy()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { mv ->
            mv.getMapAsync { googleMap ->
                if (!mapInitialized.value) {
                    googleMap.uiSettings.isZoomControlsEnabled = true

                    initialLocation?.let {
                        val pos = LatLng(it.latitude, it.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
                        markerRef.value = googleMap.addMarker(
                            MarkerOptions().position(pos).title("Local selecionado")
                        )
                    }

                    googleMap.setOnMapClickListener { latLng ->
                        markerRef.value?.remove()
                        val newMarker = googleMap.addMarker(
                            MarkerOptions().position(latLng).title("Local selecionado")
                        )
                        markerRef.value = newMarker
                        onLocationSelected(latLng)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    }

                    mapInitialized.value = true
                }
            }
        }
    )
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
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-5).dp),
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
fun ActionButton(isEditingMode: Boolean, onSaveClick: () -> Unit, onDeleteClick: () -> Unit) {
    Button(
        onClick = {
            if (isEditingMode) {
                onSaveClick()
            } else {
                onDeleteClick()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEditingMode) AppColors().green
            else AppColors().lightRed
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = if (isEditingMode) "Salvar alterações" else "Deletar evento",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = if (isEditingMode) AppColors().black else AppColors().red,
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

@Composable
fun rememberParticipantFilterState(event: Event): Triple<List<User>, MutableState<Set<ParticipantsFilter>>, MutableState<Boolean>> {
    val eventParticipantsEmails = remember(event.participants) { event.participants.toList() }
    val participantsWithEventStatus = remember { mutableStateOf(emptyList<Pair<User, Event>>()) }

    LaunchedEffect(eventParticipantsEmails) {
        val tempParticipantsStatus = mutableListOf<Pair<User, Event>>()
        eventParticipantsEmails.forEach { email ->
            val participantUser = RubeusApi.searchUserByEmail(email)
            participantUser?.let { user ->
                val participantEvent = RubeusApi.getUserEvent(user.id, event.course)
                if (participantEvent != null) {
                    tempParticipantsStatus.add(Pair(user, participantEvent))
                }
            }
        }
        participantsWithEventStatus.value = tempParticipantsStatus
    }

    val selectedFilters = remember { mutableStateOf(setOf<ParticipantsFilter>()) }
    val showFilterDialog = remember { mutableStateOf(false) }

    val filteredParticipants = remember(participantsWithEventStatus.value, selectedFilters.value) {
        if (selectedFilters.value.isEmpty()) {
            participantsWithEventStatus.value.map { it.first } // Use .value aqui
        } else {
            participantsWithEventStatus.value.filter { (_, participantEvent) ->
                selectedFilters.value.all { filter ->
                    when (filter) {
                        ParticipantsFilter.ABSENT -> isUserMissed(participantEvent, event.endTime)
                        ParticipantsFilter.PRESENT -> isUserCheckedIn(participantEvent)
                        ParticipantsFilter.LATE -> isUserLate(participantEvent)
                        ParticipantsFilter.AT_TIME -> isUserOnTime(participantEvent)
                    }
                }
            }.map { it.first }
        }
    }

    return Triple(filteredParticipants, selectedFilters, showFilterDialog)
}

fun isUserCheckedIn(userEvent: Event): Boolean {
    return userEvent.checkInTime != null
}

fun isUserLate(userEvent: Event): Boolean {
    val checkIn = userEvent.checkInTime
    val scheduledCheckInStart = userEvent.checkInEnabled

    if (checkIn == null || scheduledCheckInStart == null) {
        return false
    }

    val lateThreshold = Calendar.getInstance().apply {
        time = scheduledCheckInStart
        add(Calendar.MINUTE, 10)
    }.time

    return checkIn.after(lateThreshold)
}

fun isUserOnTime(userEvent: Event): Boolean {
    val checkIn = userEvent.checkInTime
    val scheduledCheckInStart = userEvent.checkInEnabled

    if (checkIn == null || scheduledCheckInStart == null) {
        return false
    }

    val lateThreshold = Calendar.getInstance().apply {
        time = scheduledCheckInStart
        add(Calendar.MINUTE, 10)
    }.time

    return (checkIn.before(lateThreshold) || checkIn == lateThreshold)
}

fun isUserMissed(userEvent: Event, eventEndTime: Date): Boolean {
    return userEvent.checkInTime == null && Date().after(eventEndTime)
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
        AppDateHelper().getFullDate(2025, 5, 25, 23, 56, 0),
        AppDateHelper().getFullDate(2025, 5, 25, 23, 56, 0),
        null,
        null,
        null,
        null,
        Event.EventStage.CURRENT,
        listOf()
    )

    var user = User(
        0,
        "Erick Soares",
        User.UserRole.ADMIN,
        "teste@teste.com",
        "12345678900",
        "Universidade Federal de Viçosa (UFV)",
        "****",
        true,
        null,
        listOf()
    )

    EventDetailsScreen(
        navController = rememberNavController(),
        event = event,
        user = user,
        onDelete = {})
}