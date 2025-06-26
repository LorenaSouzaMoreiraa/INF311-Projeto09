package com.example.inf311_projeto09.ui.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppSnackBarManager {
    val snackBarHostState = SnackbarHostState()
    var lastMessage: SnackBarMessage? = null

    fun showMessage(message: String, color: SnackBarColor = SnackBarColor.DARK_GREEN) {
        lastMessage = SnackBarMessage(message, color)

        CoroutineScope(Dispatchers.Main).launch {
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }
}

data class SnackBarMessage(
    val text: String,
    val backgroundColor: SnackBarColor = SnackBarColor.DARK_GREEN
)

enum class SnackBarColor {
    DARK_GREEN,
    LIGHT_GREEN,
    WHITE
}
