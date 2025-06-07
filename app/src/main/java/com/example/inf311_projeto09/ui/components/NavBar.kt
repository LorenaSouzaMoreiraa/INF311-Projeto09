package com.example.inf311_projeto09.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppIcons

enum class NavBarOption {
    PROFILE,
    HOME,
    CALENDAR
}

@Composable
fun NavBar(
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCalendarClick: () -> Unit,
    navBarOption: NavBarOption
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(AppColors().darkGreen),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileIcon(onProfileClick, navBarOption == NavBarOption.PROFILE)

        HomeIcon(onHomeClick, navBarOption == NavBarOption.HOME)

        CalendarIcon(onCalendarClick, navBarOption == NavBarOption.CALENDAR)
    }
}

@Composable
private fun ProfileIcon(
    onProfileClick: () -> Unit,
    isProfileSelected: Boolean
) {
    // TODO: colocar foto do usuário
    val profileImage = null

    // TODO: mudar as rotas do clique
    Box(
        modifier = Modifier
            .size(30.dp)
            .clickable { onProfileClick() },
        contentAlignment = Alignment.Center
    ) {
        if (profileImage == null) {
            AppIcons.Outline.CircleUserRound(
                30.dp,
                if (isProfileSelected) AppColors().lightGreen else AppColors().white
            )
        } else {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .then(
                        if (isProfileSelected) Modifier
                            .border(
                                width = 2.dp,
                                color = AppColors().lightGreen,
                                shape = CircleShape
                            )
                        else Modifier
                    )
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Foto do usuário",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeIcon(
    onHomeClick: () -> Unit,
    isHomeSelected: Boolean
) {
    // TODO: mudar as rotas do clique
    Box(
        modifier = Modifier
            .size(30.dp)
            .clickable { onHomeClick() },
        contentAlignment = Alignment.Center
    ) {
        AppIcons.Outline.House(
            30.dp,
            if (isHomeSelected) AppColors().lightGreen else AppColors().white
        )
    }
}

@Composable
private fun CalendarIcon(
    onCalendarClick: () -> Unit,
    isCalendarSelected: Boolean
) {
// TODO: mudar as rotas do clique
    Box(
        modifier = Modifier
            .size(30.dp)
            .clickable { onCalendarClick() },
        contentAlignment = Alignment.Center
    ) {
        AppIcons.Outline.Calendar(
            30.dp,
            if (isCalendarSelected) AppColors().lightGreen else AppColors().white
        )
    }
}
