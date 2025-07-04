package com.inf311_projeto09.ui.screens.admin

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.inf311_projeto09.api.RubeusApi
import com.inf311_projeto09.helper.EventAuthenticationHelper
import com.inf311_projeto09.model.Event
import com.inf311_projeto09.model.User
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

@Composable
fun RegisterEventScreen(
    user: User,
    navController: NavHostController,
) {
    var eventName by remember { mutableStateOf("") }
    var startDateCalendar by remember { mutableStateOf<Calendar?>(null) }
    var startTimeCalendar by remember { mutableStateOf<Calendar?>(null) }
    var endDateCalendar by remember { mutableStateOf<Calendar?>(null) }
    var endTimeCalendar by remember { mutableStateOf<Calendar?>(null) }
    var eventTypeExpanded by remember { mutableStateOf(false) }
    val eventTypes = listOf("Online", "Presencial", "Híbrido")
    var selectedEventType by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var selectedAuthMethod by remember { mutableStateOf(Event.EventVerificationMethod.NONE) }
    var autoCheckInOut by remember { mutableStateOf(false) }
    var emailsImported by remember { mutableStateOf(listOf<String>()) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale("pt", "BR")) }
    var startDateText by remember { mutableStateOf("") }
    var startTimeText by remember { mutableStateOf("") }
    var endDateText by remember { mutableStateOf("") }
    var endTimeText by remember { mutableStateOf("") }

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedLocationText by remember { mutableStateOf("") }
    var showMapDialog by remember { mutableStateOf(false) }

    val locationHelper = rememberLocationHelper()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {}
    )

    LaunchedEffect(Unit) {
        locationHelper.getCurrentLocation(permissionLauncher)
    }

    val focusManager: FocusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

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
            ScreenTitleAndSubtitle()

            Spacer(modifier = Modifier.height(30.dp))

            MainContent(
                user = user,
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
                eventTypes = eventTypes,
                selectedEventType = selectedEventType,
                onSelectedEventTypeChange = { selectedEventType = it },
                eventDescription = eventDescription,
                onDescriptionChange = { eventDescription = it },
                selectedLocationText = selectedLocationText,
                onOpenMapDialog = { showMapDialog = true },
                selectedAuthMethod = selectedAuthMethod,
                onSelectedAuthMethodChange = { selectedAuthMethod = it },
                autoCheckInOut = autoCheckInOut,
                onAutoCheckInOutChange = { autoCheckInOut = it },
                emailsImported = emailsImported,
                onEmailsImported = { emailsImported = it },
                scrollState = scrollState,
                navController = navController
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
                LocationSelector(
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
fun ScreenTitleAndSubtitle() {
    Text(
        text = "Cadastre seu evento",
        fontFamily = AppFonts().montserrat,
        fontWeight = FontWeight.SemiBold,
        color = AppColors().white,
        fontSize = 30.sp,
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

@Composable
fun MainContent(
    user: User,
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
    selectedAuthMethod: Event.EventVerificationMethod,
    onSelectedAuthMethodChange: (Event.EventVerificationMethod) -> Unit,
    autoCheckInOut: Boolean,
    onAutoCheckInOutChange: (Boolean) -> Unit,
    emailsImported: List<String>,
    onEmailsImported: (List<String>) -> Unit,
    scrollState: androidx.compose.foundation.ScrollState,
    navController: NavHostController
) {
    val customDropdownShape =
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AppColors().offWhite,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
            .padding(25.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            EventNameField(eventName = eventName, onEventNameChange = onEventNameChange)

            Spacer(modifier = Modifier.height(10.dp))

            DateTimeFields(
                isStartDateTime = true,
                startDateText = startDateText,
                onShowStartDatePicker = onShowStartDatePicker,
                startTimeText = startTimeText,
                onShowStartTimePicker = onShowStartTimePicker
            )

            Spacer(modifier = Modifier.height(10.dp))

            DateTimeFields(
                isStartDateTime = false,
                startDateText = endDateText,
                onShowStartDatePicker = onShowEndDatePicker,
                startTimeText = endTimeText,
                onShowStartTimePicker = onShowEndTimePicker
            )

            Spacer(modifier = Modifier.height(10.dp))

            EventTypeDropdown(
                eventTypeExpanded = eventTypeExpanded,
                onEventTypeExpandedChange = onEventTypeExpandedChange,
                eventTypes = eventTypes,
                selectedEventType = selectedEventType,
                onSelectedEventTypeChange = onSelectedEventTypeChange,
                customDropdownShape = customDropdownShape
            )

            Spacer(modifier = Modifier.height(10.dp))

            DescriptionField(
                description = eventDescription,
                onDescriptionChange = onDescriptionChange
            )

            Spacer(modifier = Modifier.height(10.dp))

            LocationField(
                locationText = selectedLocationText,
                onClickSelectLocation = onOpenMapDialog
            )

            Spacer(modifier = Modifier.height(10.dp))

            AuthMethodSelection(
                selectedAuthMethod = selectedAuthMethod,
                onSelectedAuthMethodChange = onSelectedAuthMethodChange
            )

            Spacer(modifier = Modifier.height(10.dp))

            ImportParticipantsSection(onEmailsImported = onEmailsImported)

            Spacer(modifier = Modifier.height(10.dp))

            AutoCheckInOutCheckbox(
                autoCheckInOut = autoCheckInOut,
                onAutoCheckInOutChange = onAutoCheckInOutChange
            )

            Spacer(modifier = Modifier.height(15.dp))

            CreateEventButton(onClick = {
                val now = Date()

                when {
                    eventName.isEmpty() -> {
                        AppSnackBarManager.showMessage("O campo 'Nome do evento' é obrigatório")
                        return@CreateEventButton
                    }

                    startDateText.isEmpty() -> {
                        AppSnackBarManager.showMessage("O campo 'Data início' é obrigatório")
                        return@CreateEventButton
                    }

                    startTimeText.isEmpty() -> {
                        AppSnackBarManager.showMessage("O campo 'Hora início' é obrigatório")
                        return@CreateEventButton
                    }
                }

                val beginTime =
                    AppDateHelper().getDateByDateStringAndTimeString(startDateText, startTimeText)
                if (beginTime.before(now)) {
                    AppSnackBarManager.showMessage("A data e hora de início devem ser futuras")
                    return@CreateEventButton
                }

                when {
                    endDateText.isEmpty() -> {
                        AppSnackBarManager.showMessage("O campo 'Data fim' é obrigatório")
                        return@CreateEventButton
                    }

                    endTimeText.isEmpty() -> {
                        AppSnackBarManager.showMessage("O campo 'Hora fim' é obrigatório")
                        return@CreateEventButton
                    }
                }

                val endTime =
                    AppDateHelper().getDateByDateStringAndTimeString(endDateText, endTimeText)
                if (endTime.before(now)) {
                    AppSnackBarManager.showMessage("A data e hora de término devem ser futuras")
                    return@CreateEventButton
                }

                when {
                    !endTime.after(beginTime) -> {
                        AppSnackBarManager.showMessage("A data e hora de término devem ser posteriores à data e hora de início")
                        return@CreateEventButton
                    }

                    selectedEventType.isEmpty() -> {
                        AppSnackBarManager.showMessage("O campo 'Tipo de evento' é obrigatório")
                        return@CreateEventButton
                    }

                    selectedEventType == "Presencial" && selectedLocationText.isEmpty() -> {
                        AppSnackBarManager.showMessage("O campo 'Local do evento' é obrigatório")
                        return@CreateEventButton
                    }

                    selectedAuthMethod == Event.EventVerificationMethod.NONE -> {
                        AppSnackBarManager.showMessage("O campo 'Método de autenticação' é obrigatório")
                        return@CreateEventButton
                    }

                    emailsImported.isEmpty() -> {
                        AppSnackBarManager.showMessage("É necessário importar um arquivo .csv com os emails dos participantes")
                        return@CreateEventButton
                    }
                }

                val event = Event(
                    0,
                    eventName,
                    eventDescription,
                    selectedEventType,
                    selectedAuthMethod,
                    EventAuthenticationHelper.generateCheckCode(selectedAuthMethod),
                    autoCheckInOut,
                    selectedLocationText,
                    beginTime,
                    endTime,
                    if (autoCheckInOut) AppDateHelper().getDateByDateStringAndTimeString(
                        startDateText,
                        startTimeText
                    ) else null,
                    if (autoCheckInOut) AppDateHelper().getDateByDateStringAndTimeString(
                        endDateText,
                        endTimeText
                    ) else null,
                    null,
                    null,
                    Event.EventStage.NEXT,
                    emailsImported
                )

                RubeusApi.registerEvent(event, user)
                navController.popBackStack()
            })

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun EventNameField(eventName: String, onEventNameChange: (String) -> Unit) {
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
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun DateTimeFields(
    isStartDateTime: Boolean,
    startDateText: String,
    onShowStartDatePicker: () -> Unit,
    startTimeText: String,
    onShowStartTimePicker: () -> Unit
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
                leadingIcon = {
                    AppIcons.Outline.CalendarField(24.dp, AppColors().black)
                },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = AppColors().black,
                    disabledBorderColor = AppColors().lightGrey,
                    disabledLeadingIconColor = AppColors().black,
                    disabledLabelColor = AppColors().grey
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
                leadingIcon = {
                    AppIcons.Outline.Clock(24.dp, AppColors().black)
                },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = AppColors().black,
                    disabledBorderColor = AppColors().lightGrey,
                    disabledLeadingIconColor = AppColors().black,
                    disabledLabelColor = AppColors().grey
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
    customDropdownShape: RoundedCornerShape
) {
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
}

@Composable
fun DescriptionField(description: String, onDescriptionChange: (String) -> Unit) {
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
        maxLines = Int.MAX_VALUE
    )
}

@Composable
fun LocationField(
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
            IconButton(onClick = onClickSelectLocation) {
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
    selectedAuthMethod: Event.EventVerificationMethod,
    onSelectedAuthMethodChange: (Event.EventVerificationMethod) -> Unit
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
            checked = (selectedAuthMethod == Event.EventVerificationMethod.QR_CODE),
            onCheckedChange = { isChecked ->
                if (isChecked) onSelectedAuthMethodChange(Event.EventVerificationMethod.QR_CODE)
                else if (selectedAuthMethod == Event.EventVerificationMethod.QR_CODE) onSelectedAuthMethodChange(
                    Event.EventVerificationMethod.NONE
                )
            },
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors().darkGreen,
                uncheckedColor = AppColors().lightGrey,
                checkmarkColor = AppColors().darkGreen
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
                .clickable { onSelectedAuthMethodChange(if (selectedAuthMethod == Event.EventVerificationMethod.QR_CODE) Event.EventVerificationMethod.NONE else Event.EventVerificationMethod.QR_CODE) }
                .offset(x = (-13).dp)
        )

        Spacer(modifier = Modifier.width(70.dp))

        Checkbox(
            checked = (selectedAuthMethod == Event.EventVerificationMethod.VERIFICATION_CODE),
            onCheckedChange = { isChecked ->
                if (isChecked) onSelectedAuthMethodChange(Event.EventVerificationMethod.VERIFICATION_CODE)
                else if (selectedAuthMethod == Event.EventVerificationMethod.VERIFICATION_CODE) onSelectedAuthMethodChange(
                    Event.EventVerificationMethod.NONE
                )
            },
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors().darkGreen,
                uncheckedColor = AppColors().lightGrey,
                checkmarkColor = AppColors().darkGreen
            )
        )

        Text(
            text = "Código único",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = AppColors().black,
            modifier = Modifier.clickable {
                onSelectedAuthMethodChange(if (selectedAuthMethod == Event.EventVerificationMethod.VERIFICATION_CODE) Event.EventVerificationMethod.NONE else Event.EventVerificationMethod.VERIFICATION_CODE)
            }
        )
    }
}

@Composable
fun ImportParticipantsSection(
    modifier: Modifier = Modifier,
    onEmailsImported: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val selectedFileName = remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val emails = readEmailsFromCsv(context, it)
            selectedFileName.value = getFileNameFromUri(context, it)

            if (emails.isNotEmpty()) {
                onEmailsImported(emails)
                AppSnackBarManager.showMessage("Participantes importados com sucesso.")
            } else {
                AppSnackBarManager.showMessage("Nenhum e-mail válido encontrado.")
            }
        }
    }

    Column(modifier = modifier) {
        Text(
            text = "Importar participantes",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().black,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        Button(
            onClick = { launcher.launch("text/csv") },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors().white,
                contentColor = AppColors().grey
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, AppColors().lightGrey), RoundedCornerShape(8.dp))
        ) {
            Text(
                text = selectedFileName.value ?: "Clique aqui para selecionar o arquivo",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = AppColors().grey
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun readEmailsFromCsv(context: Context, uri: Uri): List<String> {
    val emails = mutableListOf<String>()
    try {
        context.contentResolver.openInputStream(uri)?.bufferedReader()?.useLines { lines ->
            lines.forEach { line ->
                val trimmedLine = line.trim()
                if (trimmedLine.contains("@")) {
                    emails.add(trimmedLine)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return emails
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var name = "arquivo.csv"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex >= 0) {
            name = it.getString(nameIndex)
        }
    }
    return name
}

@Composable
fun AutoCheckInOutCheckbox(autoCheckInOut: Boolean, onAutoCheckInOutChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = autoCheckInOut,
            onCheckedChange = onAutoCheckInOutChange,
            colors = CheckboxDefaults.colors(
                checkedColor = AppColors().darkGreen,
                uncheckedColor = AppColors().lightGrey,
                checkmarkColor = AppColors().darkGreen
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
fun CreateEventButton(
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors().lightGreen,
            contentColor = AppColors().black
        ),
        shape = RoundedCornerShape(60),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp)
    ) {
        Text(
            text = "Criar evento",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterEventScreenPreview() {
    RegisterEventScreen(
        user = User(
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
        ), navController = rememberNavController()
    )
}