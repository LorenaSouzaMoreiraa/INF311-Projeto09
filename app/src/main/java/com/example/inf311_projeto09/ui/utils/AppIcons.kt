package com.example.inf311_projeto09.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.jihan.lucide_icons.lucide

class AppIcons {
    @Composable
    fun OutlineCircleCheck(boxSize: Dp, colorIcon: Color) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .background(color = AppColors().transparent, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = lucide.circle_check),
                contentDescription = "Selecionado",
                modifier = Modifier.fillMaxSize()
                    .scale(1f),
                tint = colorIcon
            )
        }
    }

    @Composable
    fun FilledCircleCheck(boxSize: Dp, colorIcon: Color, backgroundColorIcon: Color) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .background(color = colorIcon, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = lucide.circle_check),
                contentDescription = "Selecionado",
                modifier = Modifier.fillMaxSize()
                    .scale(1.2f),
                tint = backgroundColorIcon
            )
        }
    }
}