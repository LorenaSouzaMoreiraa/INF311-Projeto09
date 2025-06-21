package com.example.inf311_projeto09.ui.utils

import android.graphics.Camera
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.inf311_projeto09.R
import com.jihan.lucide_icons.lucide

class AppIcons {

    object Outline {
        @Composable
        fun CircleCheck(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.circle_check),
                    contentDescription = "Círculo de verificação",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun CircleArrowLeft(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.circle_arrow_left),
                    contentDescription = "Seta para esquerda",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun CircleArrowRight(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.circle_arrow_right),
                    contentDescription = "Seta para direita",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Mail(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.mail),
                    contentDescription = "Email",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun KeyRound(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.key_round),
                    contentDescription = "Chave",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun EyeClosed(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.eye_closed),
                    contentDescription = "Olho fechado",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Eye(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.eye),
                    contentDescription = "Olho aberto",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Bell(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.bell),
                    contentDescription = "Sino",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun House(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.house),
                    contentDescription = "Casa",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Calendar(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendário",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun CalendarDays(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.calendar_days),
                    contentDescription = "Dias do calendário",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun CircleUserRound(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.circle_user_round),
                    contentDescription = "Círculo de usuário",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Pin(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.pin),
                    contentDescription = "Pino",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Clock(boxSize: Dp, colorIcon: Color = AppColors().darkGreen) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.clock),
                    contentDescription = "Relógio",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun ArrowDown(
            boxSize: Dp,
            colorIcon: Color = AppColors().grey,
            modifier: Modifier = Modifier
        ) {
            Box(
                modifier = modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.chevron_down),
                    contentDescription = "Expandir",
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun LogOut(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.log),
                    contentDescription = "Deslogar",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Settings(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.settings),
                    contentDescription = "Configurações",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun Target(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.target),
                    contentDescription = "Alvo",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }

        @Composable
        fun EditIcon(boxSize: Dp, colorIcon: Color = AppColors().white) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = AppColors().transparent, shape = RectangleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.square_arrow_out_up_left),
                    contentDescription = "Editar",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    tint = colorIcon
                )
            }
        }
    }

    object Filled {
        @Composable
        fun CircleCheck(boxSize: Dp, colorIcon: Color, backgroundColorIcon: Color) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = colorIcon, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.circle_check),
                    contentDescription = "Círculo de verificação",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.2f),
                    tint = backgroundColorIcon
                )
            }
        }

        @Composable
        fun CircleClose(
            boxSize: Dp,
            colorIcon: Color,
            backgroundColorIcon: Color
        ) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = colorIcon, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.circle_x),
                    contentDescription = "X para sair da tela",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.2f),
                    tint = backgroundColorIcon
                )
            }
        }

        @Composable
        fun Camera(boxSize: Dp, colorIcon: Color, backgroundColorIcon: Color) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = colorIcon, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = lucide.camera),
                    contentDescription = "Câmera",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.0f),
                    tint = backgroundColorIcon
                )
            }
        }
    }
}