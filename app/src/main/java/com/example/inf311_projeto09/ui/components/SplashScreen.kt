package com.example.inf311_projeto09.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.inf311_projeto09.ui.utils.AppColors

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AppColors().lightGreen)
    }
}