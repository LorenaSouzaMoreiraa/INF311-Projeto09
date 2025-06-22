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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons

@Composable
fun UserRoleScreen(
    onBack: () -> Unit = {},
    onRoleSelected: (User.UserRole) -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf<User.UserRole?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp, horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(30.dp)
                ) {
                    AppIcons.Outline.CircleArrowLeft(30.dp, AppColors().white)
                }

                Image(
                    painter = painterResource(id = R.drawable.extended_logo),
                    contentDescription = "Logotipo extendida",
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Quem é\nvocê?",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors().white,
                    fontSize = 40.sp,
                    lineHeight = 35.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )

                Text(
                    text = "Selecione o perfil que melhor descreve sua função na plataforma. Isso nos ajudará a personalizar sua experiência com os recursos mais adequados ao seu uso.",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.Medium,
                    color = AppColors().lightGrey,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                )

                RoleSelectionCard(
                    title = "Organizador(a)",
                    description = "Professores, coordenadores, instituições de ensino e organizadores de eventos",
                    isSelected = selectedRole == User.UserRole.ADMIN,
                    onClick = {
                        selectedRole = User.UserRole.ADMIN
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                RoleSelectionCard(
                    title = "Participante",
                    description = "Estudantes, colaboradores, convidados e participantes de eventos",
                    isSelected = selectedRole == User.UserRole.USER,
                    onClick = {
                        selectedRole = User.UserRole.USER
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    selectedRole?.let { onRoleSelected(it) }
                },
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
                    fontSize = 16.sp
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentBorderColor = if (isSelected) AppColors().lightGreen else AppColors().white
    val borderStroke = if (isSelected) 3.5.dp else 1.dp

    Column(
        modifier = modifier
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
                AppIcons.Filled.CircleCheck(20.dp, AppColors().green, AppColors().darkGreen)
            } else {
                AppIcons.Outline.CircleCheck(20.dp)
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