package com.example.inf311_projeto09.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.model.NotificationsMock
import com.example.inf311_projeto09.model.getCurrentEvent
import com.example.inf311_projeto09.model.getNextEvents
import com.example.inf311_projeto09.ui.screens.CalendarScreen
import com.example.inf311_projeto09.ui.screens.HomeScreen
import com.example.inf311_projeto09.ui.screens.LoginScreen
import com.example.inf311_projeto09.ui.screens.NotificationsScreen
import com.example.inf311_projeto09.ui.screens.QrScannerScreen
import com.example.inf311_projeto09.ui.screens.RegisterScreen
import com.example.inf311_projeto09.ui.screens.ProfileScreen
import com.example.inf311_projeto09.ui.screens.UserRoleScreen
import com.example.inf311_projeto09.ui.screens.WelcomeScreen

enum class ScreenType(val route: String) {
    WELCOME("welcome"),
    USER_ROLE("user_role"),
    LOGIN("login"),
    HOME("home"),
    PROFILE("profile"),
    CALENDAR("calendar"),
    NOTIFICATIONS("notifications"),
    QR_SCANNER("qr_scanner"),
    REGISTER("register")
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

    NavHost(
        navController = navController,
        startDestination = initialScreen
    ) {
        composable(ScreenType.WELCOME.route) {
            WelcomeScreen(
                onContinue = {
                    navController.navigate(ScreenType.USER_ROLE.route)
                }
            )
        }

        composable(ScreenType.USER_ROLE.route) {
            // TODO: precisa ter uma opção selecionada
            UserRoleScreen(
                onRoleSelected = {
                    navController.navigate(ScreenType.LOGIN.route)
                }
            )
        }

        composable(ScreenType.LOGIN.route) {
            // TODO: fazer essa verificação de lembrar login
            // TODO: precisa ter os dados preenchidos e verificar
            // TODO: fazer a tela de login e esqueci senha?
            LoginScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLoginSuccess = { rememberUser ->
                    navController.navigate(ScreenType.HOME.route) {
                        popUpTo(ScreenType.WELCOME.route) { inclusive = true }
                    }
                }
            )
        }

        composable(ScreenType.HOME.route) {
            // TODO: apagar mock
            HomeScreen(
                currentEvent = getCurrentEvent(),
                nextEvents = getNextEvents(),
                navController = navController
            )
        }

        composable(ScreenType.CALENDAR.route) {
            CalendarScreen(
                navController = navController
            )
        }

        composable(ScreenType.PROFILE.route) {
            ProfileScreen(navController = navController)
        }

        composable(ScreenType.NOTIFICATIONS.route) {
            // TODO: apagar mock
            NotificationsScreen(
                notificationsMock = notificationsMock,
                navController = navController
            )
        }

        composable(ScreenType.QR_SCANNER.route) {
            // TODO: apagar mock
            QrScannerScreen(
                navController = navController
            )
        }

        composable(ScreenType.REGISTER.route) {
            // TODO: apagar mock
            RegisterScreen()
        }
    }
}
