package com.inf311_projeto09.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inf311_projeto09.R
import com.inf311_projeto09.ui.utils.AppColors
import com.inf311_projeto09.ui.utils.AppFonts
import com.inf311_projeto09.ui.utils.AppIcons

@Composable
fun RecoverPasswordScreen(
    onSendRecoveryLinkClick: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val focusManager: FocusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            )
            .focusable(false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 30.dp, end = 30.dp)
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
                    .align(Alignment.TopCenter)
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 150.dp)
                .fillMaxWidth()
                .background(AppColors().darkGreen)
        ) {
            Text(
                text = "Recuperar senha",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().white,
                fontSize = 30.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Insira o endereço de email associado a sua conta da plataforma",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().lightGrey,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        AppColors().offWhite,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(25.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                "Email",
                                fontFamily = AppFonts().montserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = AppColors().grey
                            )
                        },
                        leadingIcon = {
                            AppIcons.Outline.Mail(24.dp, AppColors().black)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AppColors().black,
                            unfocusedTextColor = AppColors().black,
                            focusedBorderColor = AppColors().lightGrey,
                            unfocusedBorderColor = AppColors().lightGrey,
                            cursorColor = AppColors().black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Caso o email informado esteja cadastrado na plataforma, você receberá um link para redefinição de senha.",
                        fontFamily = AppFonts().montserrat,
                        fontWeight = FontWeight.Medium,
                        color = AppColors().lightBlack,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = { onSendRecoveryLinkClick(email) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors().lightGreen,
                            contentColor = AppColors().black
                        ),
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier
                            .padding(bottom = 60.dp)
                            .width(278.dp)
                            .height(51.dp)
                    ) {
                        Text(
                            text = "Enviar link de recuperação",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecoverPasswordScreenPreview() {
    RecoverPasswordScreen()
}