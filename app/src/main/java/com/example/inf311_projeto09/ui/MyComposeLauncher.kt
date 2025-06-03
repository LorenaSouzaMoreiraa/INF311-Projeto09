package com.example.inf311_projeto09.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inf311_projeto09.ui.screens.HomeScreen
import com.example.inf311_projeto09.ui.screens.LoginScreen
import com.example.inf311_projeto09.ui.screens.UserRoleScreen
import com.example.inf311_projeto09.ui.screens.WelcomeScreen

enum class ScreenType {
    WELCOME,
    USER_ROLE,
    LOGIN,
    HOME,
    CLASS_INFOS,
    CHECK_IN,
    PROFILE
}

object MyComposeLauncher {
    @JvmStatic
    fun launch(activity: ComponentActivity, screenType: ScreenType) {
        activity.setContent {
            when (screenType) {
                ScreenType.WELCOME -> WelcomeScreen()
                ScreenType.USER_ROLE -> UserRoleScreen()
                ScreenType.LOGIN -> LoginScreen()
                ScreenType.HOME -> HomeScreen(currentEvent = null, nextEvents = listOf())
                ScreenType.CHECK_IN -> TODO()
                ScreenType.PROFILE -> TODO()
                ScreenType.CLASS_INFOS -> TODO()
            }
        }
    }
}
