package com.example.inf311_projeto09.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inf311_projeto09.ui.screens.WelcomeScreen

enum class ScreenType {
    WELCOME,
    CLASS_INFOS,
    HOME,
    CHECK_IN,
    PROFILE
}

object MyComposeLauncher {
    @JvmStatic
    fun launch(activity: ComponentActivity, screenType: ScreenType) {
        activity.setContent {
            when (screenType) {
                ScreenType.WELCOME -> WelcomeScreen()
                ScreenType.CLASS_INFOS -> TODO()
                ScreenType.HOME -> TODO()
                ScreenType.CHECK_IN -> TODO()
                ScreenType.PROFILE -> TODO()
            }
        }
    }
}
