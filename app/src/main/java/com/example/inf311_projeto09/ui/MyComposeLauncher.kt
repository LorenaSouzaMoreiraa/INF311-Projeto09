package com.example.inf311_projeto09.ui

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.helper.PasswordHelper
import com.example.inf311_projeto09.model.NotificationsMock
import com.example.inf311_projeto09.model.User
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
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import java.util.Calendar

// TODO: trocar cores de branco para offWhite

enum class ScreenType(val route: String) {
    WELCOME("welcome"),
    LOGIN("login"),
    USER_ROLE("user_role"),
    REGISTER("register"),
    HOME("home"),
    PROFILE("profile"),
    CALENDAR("calendar"),
    NOTIFICATIONS("notifications"),
    QR_SCANNER("qr_scanner"),
    VERIFICATION_CODE("verification_code"),
    RECOVER_PASSWORD("recover_password"),
    EDIT_PROFILE("edit_profile"),
    REGISTER_EVENT("register_event")
}

val notificationsMock = NotificationsMock()

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
    var user =
        if (rememberedEmail.isNotEmpty()) RubeusApi.searchUserByEmail(rememberedEmail) else null
    var userEvents = user?.let { RubeusApi.listUserEvents(it.id) } ?: emptyList()
    var todayEvents = AppDateHelper().getEventsForDate(userEvents, today.time)
    // TODO: atualizar os eventos de tempos em tempos?

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
            // TODO: precisa ter os dados preenchidos e verificar
            LoginScreen(
                onLoginSuccess = { email, password, rememberUser ->
                    user = RubeusApi.searchUserByEmail(email)
                    val isValidPassword = PasswordHelper.verifyPassword(password, user?.password)

                    if (isValidPassword) {
                        if (rememberUser) {
                            setRememberedEmail(activity, email)
                        }

                        userEvents = user?.let { RubeusApi.listUserEvents(it.id) } ?: emptyList()
                        todayEvents = AppDateHelper().getEventsForDate(userEvents, today.time)

                        navController.navigate(ScreenType.HOME.route) {
                            popUpTo(ScreenType.WELCOME.route) { inclusive = true }
                        }
                    } else {
                        Log.e("LOGIN", "Login inválido.")
                        // TODO: mensagem de login não válido
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
            // TODO: precisa ter uma opção selecionada
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
                backStackEntry.arguments?.getString("userRole")?.let { User.UserRole.valueOf(it) }

            // TODO: exibir mensagem falando que precisa ter selecionado pelo menos um
            if (userRole != null) {
                RegisterScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSignUpSuccess = { signUpSuccess ->
                        if (signUpSuccess) {
                            navController.navigate(ScreenType.LOGIN.route) {
                                popUpTo(ScreenType.LOGIN.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        } else {
                            Log.e("REGISTER", "Registro inválido.")
                            // TODO: mensagem de erro
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
            user?.let { nonNullUser ->
                HomeScreen(
                    user = nonNullUser,
                    todayEvents = todayEvents,
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
            user?.let { nonNullUser ->
                CalendarScreen(
                    user = nonNullUser,
                    allEvents = userEvents,
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

        composable(ScreenType.PROFILE.route) {
            user?.let { nonNullUser ->
                ProfileScreen(
                    user = nonNullUser,
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
            // TODO: apagar mock
            NotificationsScreen(
                onBack = {
                    navController.popBackStack()
                },
                notificationsMock = notificationsMock
            )
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
            user?.let { nonNullUser ->
                EditProfileScreen(
                    user = nonNullUser,
                    navController = navController,
                    choosePhoto = { /* TODO: Lógica para escolher foto */ },
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
            RegisterEventScreen(
                navController = navController,
            )
        }
    }
}
