package com.example.inf311_projeto09.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons

@Composable
fun UserRoleScreen(
    onClickButton: () -> Unit = {},
    onClickCard: (String) -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(AppColors().darkGreen)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.extended_logo),
                contentDescription = "Logotipo extendida",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Quem é\nvocê?",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().white,
                fontSize = 40.sp,
                lineHeight = 35.sp,
                modifier = Modifier
                    .width(280.dp)
            )

            Text(
                text = "Selecione o perfil que melhor descreve sua função na plataforma. Isso nos ajudará a personalizar sua experiência com os recursos mais adequados ao seu uso.",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().white,
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .width(280.dp)
            )

            RoleSelectionCard(
                title = "Organizador(a)",
                description = "Professores, coordenadores, instituições de ensino e organizadores de eventos",
                isSelected = selectedRole == "organizer",
                onClick = {
                    selectedRole = "organizer"
                    onClickCard("organizer")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoleSelectionCard(
                title = "Participante",
                description = "Estudantes, colaboradores, convidados e participantes de eventos",
                isSelected = selectedRole == "participant",
                onClick = {
                    selectedRole = "participant"
                    onClickCard("participant")
                }
            )

            Button(
                onClick = onClickButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors().lightGreen,
                    contentColor = AppColors().black
                ),
                shape = RoundedCornerShape(60),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Vamos começar",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun RoleSelectionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val currentBorderColor = if (isSelected) AppColors().lightGreen else AppColors().white

    val borderStroke = if (isSelected) 3.5.dp else 1.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(borderStroke, currentBorderColor),
                RoundedCornerShape(15.dp)
            )
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().white,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                AppIcons().filledCircleCheck(20.dp, AppColors().green, AppColors().darkGreen)
            } else {
                AppIcons().outlineCircleCheck(20.dp, AppColors().white)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.Medium,
            color = AppColors().lightGrey,
            fontSize = 12.sp,
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserRoleScreenPreview() {
    UserRoleScreen()
}
