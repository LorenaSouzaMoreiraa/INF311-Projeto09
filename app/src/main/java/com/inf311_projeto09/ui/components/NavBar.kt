package com.inf311_projeto09.ui.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.inf311_projeto09.R
import com.inf311_projeto09.model.User
import com.inf311_projeto09.ui.ScreenType
import com.inf311_projeto09.ui.utils.AppColors
import com.inf311_projeto09.ui.utils.AppIcons

enum class NavBarOption {
    PROFILE,
    HOME,
    CALENDAR
}

@Composable
fun NavBar(
    navController: NavHostController,
    navBarOption: NavBarOption,
    user: User
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(AppColors().darkGreen),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileIcon(navController, user, navBarOption == NavBarOption.PROFILE)

        HomeIcon(navController, navBarOption == NavBarOption.HOME)

        CalendarIcon(
            navController,
            user.type == User.UserRole.ADMIN,
            navBarOption == NavBarOption.CALENDAR
        )
    }
}

@Composable
private fun ProfileIcon(
    navController: NavHostController,
    user: User,
    isProfileSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clickable {
                navController.navigate(ScreenType.PROFILE.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.imageUrl)
                    .crossfade(true)
                    .crossfade(300)
                    .build(),
                contentDescription = "Foto do usuário",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.profile_image),
                error = painterResource(R.drawable.profile_image),
                modifier = Modifier
                    .size(28.dp)
            )
        }
    }
}

@Composable
private fun HomeIcon(
    navController: NavHostController,
    isHomeSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clickable {
                navController.navigate(ScreenType.HOME.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            },
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
    navController: NavHostController,
    isAdminHome: Boolean,
    isCalendarSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clickable {
                navController.navigate(if (isAdminHome) ScreenType.EVENTS.route else ScreenType.CALENDAR.route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AppIcons.Outline.Calendar(
            30.dp,
            if (isCalendarSelected) AppColors().lightGreen else AppColors().white
        )
    }
}
