package com.example.inf311_projeto09.ui

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.helper.PasswordHelper
import com.example.inf311_projeto09.model.Event
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.screens.CheckRoomScreen
import com.example.inf311_projeto09.ui.screens.EventDetailsScreen
import com.example.inf311_projeto09.ui.screens.EventsScreen
import com.example.inf311_projeto09.ui.screens.HomeScreen
import com.example.inf311_projeto09.ui.screens.LoginScreen
import com.example.inf311_projeto09.ui.screens.RecoverPasswordScreen
import com.example.inf311_projeto09.ui.screens.RegisterScreen
import com.example.inf311_projeto09.ui.screens.UserRoleScreen
import com.example.inf311_projeto09.ui.screens.WelcomeScreen
import com.example.inf311_projeto09.ui.screens.admin.RegisterEventScreen
import com.example.inf311_projeto09.ui.screens.user.CalendarScreen
import com.example.inf311_projeto09.ui.screens.user.EditProfileScreen
import com.example.inf311_projeto09.ui.screens.user.NotificationsScreen
import com.example.inf311_projeto09.ui.screens.user.ProfileScreen
import com.example.inf311_projeto09.ui.screens.user.QrScannerScreen
import com.example.inf311_projeto09.ui.screens.user.VerificationCodeScreen
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppSnackBarManager
import com.example.inf311_projeto09.ui.utils.SnackBarColor
import java.util.Calendar

enum class ScreenType(val route: String) {
    WELCOME("welcome"),
    LOGIN("login"),
    USER_ROLE("user_role"),
    REGISTER("register"),
    HOME("home"),
    PROFILE("profile"),
    CALENDAR("calendar"),
    EVENTS("events"),
    NOTIFICATIONS("notifications"),
    QR_SCANNER("qr_scanner"),
    VERIFICATION_CODE("verification_code"),
    RECOVER_PASSWORD("recover_password"),
    EDIT_PROFILE("edit_profile"),
    REGISTER_EVENT("register_event"),
    CHECK_OUT("check_out"),
    EVENT_DETAILS("event_details")
}

fun setHasSeenWelcome(activity: ComponentActivity) {
    val prefs = activity.getPreferences(Context.MODE_PRIVATE)
    prefs.edit {
        putBoolean("hasSeenWelcome", true)
        apply()
    }
}

fun hasSeenWelcome(activity: ComponentActivity): Boolean {
    val prefs = activity.getPreferences(Context.MODE_PRIVATE)
    return prefs.getBoolean("hasSeenWelcome", false)
}

fun setRememberedEmail(activity: ComponentActivity, email: String) {
    val prefs = activity.getPreferences(Context.MODE_PRIVATE)
    prefs.edit {
        putString("rememberedEmail", email)
        apply()
    }
}

fun hasRememberedEmail(activity: ComponentActivity): String {
    val prefs = activity.getPreferences(Context.MODE_PRIVATE)
    return prefs.getString("rememberedEmail", "") ?: ""
}

object MyComposeLauncher {
    @JvmStatic
    fun launch(activity: ComponentActivity) {
        val startDestination = when {
            hasRememberedEmail(activity).isNotEmpty() -> ScreenType.HOME.route
            hasSeenWelcome(activity) -> ScreenType.LOGIN.route
            else -> ScreenType.WELCOME.route
        }

        activity.setContent {
            val navController = rememberNavController()
            AppNavHost(navController, startDestination, activity)
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    activity: ComponentActivity
) {
    val today = remember { Calendar.getInstance() }
    val rememberedEmail = hasRememberedEmail(activity)
    val userState = remember {
        mutableStateOf(
            if (rememberedEmail.isNotEmpty()) RubeusApi.searchUserByEmail(rememberedEmail) else null
        )
    }
    val userEventsState = remember {
        mutableStateOf(userState.value?.let { RubeusApi.listUserEvents(it.id) } ?: emptyList())
    }
    val todayEventsState = remember {
        mutableStateOf(AppDateHelper().getEventsForDate(userEventsState.value, today.time))
    }

    fun deleteEvent(event: Event) {
        userEventsState.value = userEventsState.value.filterNot { it.course == event.course }
        todayEventsState.value = AppDateHelper().getEventsForDate(userEventsState.value, today.time)
    }
    // TODO: atualizar os eventos de tempos em tempos
    LaunchedEffect(Unit) {
        while (true) {
            delay(120000L)
            Log.d("AppNavHost", "Atualizando eventos...")
            userState.value?.let { user ->
                val updatedEvents = RubeusApi.listUserEvents(user.id)
                userEventsState.value = updatedEvents
                todayEventsState.value = AppDateHelper().getEventsForDate(updatedEvents, today.time)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = AppSnackBarManager.snackBarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp)
            ) { data ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 40.dp)
                ) {
                    val backgroundColor = when (AppSnackBarManager.lastMessage?.backgroundColor) {
                        SnackBarColor.DARK_GREEN -> AppColors().darkGreen.copy(alpha = 0.9f)
                        SnackBarColor.LIGHT_GREEN -> AppColors().lightGreen.copy(alpha = 0.9f)
                        SnackBarColor.WHITE -> AppColors().white.copy(alpha = 0.9f)
                        else -> AppColors().darkGreen.copy(alpha = 0.9f)
                    }

                    val contentColor = when (AppSnackBarManager.lastMessage?.backgroundColor) {
                        SnackBarColor.DARK_GREEN -> AppColors().white
                        SnackBarColor.LIGHT_GREEN -> AppColors().black
                        SnackBarColor.WHITE -> AppColors().black
                        else -> AppColors().darkGreen
                    }

                    Snackbar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        containerColor = backgroundColor,
                        contentColor = contentColor,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = data.visuals.message,
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = contentColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(ScreenType.WELCOME.route) {
                WelcomeScreen(
                    onContinue = {
                        setHasSeenWelcome(activity)
                        navController.navigate(ScreenType.LOGIN.route) {
                            popUpTo(ScreenType.WELCOME.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(ScreenType.LOGIN.route) {
                LoginScreen(
                    onLoginSuccess = { email, password, rememberUser ->
                        if (email.isEmpty()) {
                            AppSnackBarManager.showMessage("O campo 'Email' é obrigatório")
                        } else if (password.isEmpty()) {
                            AppSnackBarManager.showMessage("O campo 'Senha' é obrigatório")
                        } else {
                            userState.value = RubeusApi.searchUserByEmail(email)
                            if (userState.value == null) {
                                AppSnackBarManager.showMessage("Dados inválidos")
                            } else {
                                val isValidPassword =
                                    PasswordHelper.verifyPassword(
                                        password,
                                        userState.value?.password
                                    )

                                if (isValidPassword) {
                                    if (rememberUser) {
                                        setRememberedEmail(activity, email)
                                    }

                                    userEventsState.value =
                                        userState.value?.let { RubeusApi.listUserEvents(it.id) }
                                            ?: emptyList()
                                    todayEventsState.value =
                                        AppDateHelper().getEventsForDate(
                                            userEventsState.value,
                                            today.time
                                        )

                                    navController.navigate(ScreenType.HOME.route) {
                                        popUpTo(ScreenType.WELCOME.route) { inclusive = true }
                                    }
                                } else {
                                    Log.e("LOGIN", "Login inválido.")
                                    AppSnackBarManager.showMessage("Dados inválidos")
                                }
                            }
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(ScreenType.USER_ROLE.route)
                    },
                    onForgotPasswordClick = {
                        navController.navigate(ScreenType.RECOVER_PASSWORD.route)
                    }
                )
            }

            composable(ScreenType.RECOVER_PASSWORD.route) {
                RecoverPasswordScreen(
                    onSendRecoveryLinkClick = { email ->
                        // TODO: Talvez fazer o envio real de um link para esse email
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(ScreenType.USER_ROLE.route) {
                UserRoleScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onRoleSelected = { selectedRole ->
                        navController.navigate("${ScreenType.REGISTER.route}/$selectedRole")
                    }
                )
            }

            composable("${ScreenType.REGISTER.route}/{userRole}") { backStackEntry ->
                val userRole =
                    backStackEntry.arguments?.getString("userRole")
                        ?.let { User.UserRole.valueOf(it) }

                if (userRole != null) {
                    RegisterScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        onSignUpSuccess = { signUpSuccess ->
                            if (signUpSuccess != null) {
                                if (signUpSuccess) {
                                    navController.navigate(ScreenType.LOGIN.route) {
                                        popUpTo(ScreenType.LOGIN.route) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                } else {
                                    Log.e("REGISTER", "Registro inválido.")
                                    AppSnackBarManager.showMessage("Aconteceu um erro ao registrar sua conta")
                                }
                            }
                        },
                        onLoginClick = {
                            navController.navigate(ScreenType.LOGIN.route) {
                                popUpTo(ScreenType.LOGIN.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        userRole = userRole
                    )
                }
            }

            composable(ScreenType.HOME.route) {
                userState.value?.let { nonNullUser ->
                    HomeScreen(
                        user = nonNullUser,
                        todayEvents = todayEventsState.value,
                        navController = navController
                    )
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(ScreenType.CALENDAR.route) {
                userState.value?.let { nonNullUser ->
                    CalendarScreen(
                        user = nonNullUser,
                        allEvents = userEventsState.value,
                        navController = navController
                    )
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(ScreenType.EVENTS.route) {
                userState.value?.let { nonNullUser ->
                    EventsScreen(
                        user = nonNullUser,
                        allEvents = userEventsState.value,
                        navController = navController,
                        onEventDetails = { event ->
                            navController.navigate("${ScreenType.EVENT_DETAILS.route}/${event.course}")
                        }
                    )
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable("${ScreenType.EVENT_DETAILS.route}/{event}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("event")?.toIntOrNull()
                val selectedEvent = userEventsState.value.find { it.course == courseId }

                userState.value?.let { nonNullUser ->
                    if (selectedEvent != null) {
                        EventDetailsScreen(
                            user = nonNullUser,
                            event = selectedEvent,
                            navController = navController,
                            onDelete = {
                                deleteEvent(selectedEvent)
                                navController.popBackStack()
                            }
                        )
                    }
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(ScreenType.PROFILE.route) {
                userState.value?.let { nonNullUser ->
                    ProfileScreen(
                        user = nonNullUser,
                        allEvents = userEventsState.value,
                        navController = navController,
                        onLogout = {
                            setRememberedEmail(activity, "")
                            navController.navigate(ScreenType.LOGIN.route)
                        }
                    )
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(ScreenType.NOTIFICATIONS.route) {
                userState.value?.let { nonNullUser ->
                    NotificationsScreen(
                        user = nonNullUser,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(ScreenType.QR_SCANNER.route) {
                QrScannerScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(ScreenType.VERIFICATION_CODE.route) {
                VerificationCodeScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }

            composable(ScreenType.EDIT_PROFILE.route) {
                userState.value?.let { nonNullUser ->
                    EditProfileScreen(
                        user = nonNullUser,
                        navController = navController,
                        onDeactivateAccount = {
                            setRememberedEmail(activity, "")
                            navController.navigate(ScreenType.LOGIN.route)
                        }
                    )
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(ScreenType.REGISTER_EVENT.route) {
                userState.value?.let { nonNullUser ->
                    RegisterEventScreen(
                        user = nonNullUser,
                        navController = navController
                    )
                } ?: run {
                    setRememberedEmail(activity, "")
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(ScreenType.CHECK_OUT.route) {
                val currentEvent =
                    todayEventsState.value.find { it.eventStage == Event.EventStage.CURRENT }

                currentEvent?.let { nonNullCurrentEvent ->
                    CheckRoomScreen(
                        event = nonNullCurrentEvent,
                        onBack = {
                            navController.popBackStack()
                        },
                        navController = navController
                    )
                }
            }
        }
    }
}
