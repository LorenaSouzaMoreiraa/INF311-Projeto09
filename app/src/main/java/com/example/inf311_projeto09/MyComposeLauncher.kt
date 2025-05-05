package com.example.inf311_projeto09

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inf311_projeto09.ui.MyScreen

object MyComposeLauncher {
    @JvmStatic
    fun launch(activity: ComponentActivity) {
        activity.setContent {
            MyScreen()
        }
    }
}
