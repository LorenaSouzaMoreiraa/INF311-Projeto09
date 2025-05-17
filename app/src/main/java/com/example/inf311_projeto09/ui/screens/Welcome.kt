package com.example.inf311_projeto09.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.ui.utils.AppFonts

@Composable
fun WelcomeScreen(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF003612)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF003612))
                .padding(40.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.extended_logo),
                contentDescription = "Logotipo extendida",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .height(600.dp)
                    .width(400.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                // TODO: Mudar "Erick" para "Usuário"
                Image(
                    painter = painterResource(id = R.drawable.welcome_screen),
                    contentDescription = "Imagem na tela inicial",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(304.dp)
                        .height(621.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(325.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF003612),
                                    Color(0xFF003612),
                                    Color(0xFF003612),
                                    Color(0xFF003612)
                                )
                            )
                        )
                )

                Text(
                    text = "Sua\nparticipação\ncomeça aqui",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 40.sp,
                    lineHeight = 35.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 320.dp)
                        .width(280.dp)
                )

                Text(
                    text = "Gerencie e registre presenças em eventos de forma simples e rápida, sem papelada ou complicações.",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 450.dp)
                        .width(280.dp)
                )

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4FB51),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(60),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "Pronto para começar?",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}
