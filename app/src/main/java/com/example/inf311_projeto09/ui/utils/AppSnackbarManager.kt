package com.example.inf311_projeto09.ui.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppSnackBarManager {
    val snackBarHostState = SnackbarHostState()

    fun showMessage(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            snackBarHostState.showSnackbar(message)
        }
    }
}
