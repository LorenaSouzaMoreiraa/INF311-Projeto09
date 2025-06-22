package com.example.inf311_projeto09.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.model.NotificationsMock
import com.example.inf311_projeto09.ui.screens.LoginScreen
import com.example.inf311_projeto09.ui.screens.RecoverPasswordScreen
import com.example.inf311_projeto09.ui.screens.RegisterScreen
import com.example.inf311_projeto09.ui.screens.UserRoleScreen
import com.example.inf311_projeto09.ui.screens.WelcomeScreen
import com.example.inf311_projeto09.ui.screens.admin.RegisterEventScreen
import com.example.inf311_projeto09.ui.screens.user.CalendarScreen
import com.example.inf311_projeto09.ui.screens.user.EditProfileScreen
import com.example.inf311_projeto09.ui.screens.user.HomeScreen
import com.example.inf311_projeto09.ui.screens.user.NotificationsScreen
import com.example.inf311_projeto09.ui.screens.user.ProfileScreen
import com.example.inf311_projeto09.ui.screens.user.QrScannerScreen
import com.example.inf311_projeto09.ui.screens.user.VerificationCodeScreen
import com.example.inf311_projeto09.ui.utils.AppDateHelper
import java.util.Calendar

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

object MyComposeLauncher {
    @JvmStatic
    fun launch(activity: ComponentActivity) {
        activity.setContent {
            val navController = rememberNavController()
            AppNavHost(navController)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val initialScreen = ScreenType.WELCOME.route

    // TODO: atualizar de tempos em tempos?
    val userEvents = RubeusApi.listUserEvents(22)
    val today = remember { Calendar.getInstance() }
    val todayEvents = AppDateHelper().getEventsForDate(userEvents, today.time)

    NavHost(
        navController = navController,
        startDestination = initialScreen
    ) {
        composable(ScreenType.WELCOME.route) {
            WelcomeScreen(
                onContinue = {
                    navController.navigate(ScreenType.LOGIN.route)
                }
            )
        }

        composable(ScreenType.LOGIN.route) {
            // TODO: fazer essa verificação de lembrar login
            // TODO: precisa ter os dados preenchidos e verificar
            // TODO: fazer a tela de login e esqueci senha?
            LoginScreen(
                onLoginSuccess = { rememberUser ->
                    navController.navigate(ScreenType.HOME.route) {
                        popUpTo(ScreenType.WELCOME.route) { inclusive = true }
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
                onRoleSelected = {
                    navController.navigate(ScreenType.REGISTER.route)
                }
            )
        }

        composable(ScreenType.REGISTER.route) {
            // TODO: apagar mock
            RegisterScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSignUpSuccess = { signUpSuccess ->
                    if (signUpSuccess)
                        navController.navigate(ScreenType.LOGIN.route) {
                            popUpTo(ScreenType.LOGIN.route) { inclusive = false }
                            launchSingleTop = true
                        }
                },
                onLoginClick = {
                    navController.navigate(ScreenType.LOGIN.route) {
                        popUpTo(ScreenType.LOGIN.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(ScreenType.HOME.route) {
            HomeScreen(
                todayEvents = todayEvents,
                navController = navController
            )
        }

        composable(ScreenType.CALENDAR.route) {
            CalendarScreen(
                navController = navController,
                allEvents = userEvents
            )
        }

        composable(ScreenType.PROFILE.route) {
            // TODO: integrar
            ProfileScreen(
                navController = navController
            )
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
            EditProfileScreen(
                navController = navController,
                choosePhoto = { /* TODO: Lógica para escolher foto */ }
            )
        }

        composable(ScreenType.REGISTER_EVENT.route) {
            RegisterEventScreen(
                navController = navController,
            )
        }
    }
}
