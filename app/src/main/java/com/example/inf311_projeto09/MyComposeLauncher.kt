package com.example.inf311_projeto09

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inf311_projeto09.ui.ClassInfosScreen

enum class ScreenType {
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
                ScreenType.CLASS_INFOS -> ClassInfosScreen()
                ScreenType.HOME -> TODO()
                ScreenType.CHECK_IN -> TODO()
                ScreenType.PROFILE -> TODO()
            }
        }
    }
}
