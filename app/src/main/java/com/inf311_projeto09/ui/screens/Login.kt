package com.inf311_projeto09.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inf311_projeto09.R
import com.inf311_projeto09.ui.utils.AppColors
import com.inf311_projeto09.ui.utils.AppFonts
import com.inf311_projeto09.ui.utils.AppIcons

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String, Boolean) -> Unit = { _, _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val focusManager: FocusManager = LocalFocusManager.current

    val annotated = buildAnnotatedString {
        append("Ao prosseguir, você declara estar de acordo com os ")

        val terms = LinkAnnotation.Url(
            "https://github.com/LorenaSouzaMoreiraa/INF311-Projeto09/blob/main/app/docs/Termos%20de%20Uso.pdf",
            TextLinkStyles(
                style = SpanStyle(textDecoration = TextDecoration.Underline),
            )
        )
        withLink(terms) { append("Termos de Uso") }

        append(" e a ")

        val privacy = LinkAnnotation.Url(
            "https://github.com/LorenaSouzaMoreiraa/INF311-Projeto09/blob/main/app/docs/Pol%C3%ADtica%20de%20Privacidade.pdf",
            TextLinkStyles(
                style = SpanStyle(textDecoration = TextDecoration.Underline),
            )
        )
        withLink(privacy) { append("Política de Privacidade") }

        append(" da plataforma. O uso do aplicativo implica na aceitação dessas condições, e o não cumprimento poderá resultar em restrições de acesso ou suspensão da conta.")
    }

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
            Image(
                painter = painterResource(id = R.drawable.extended_logo),
                contentDescription = "Logotipo extendida",
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 180.dp)
                .fillMaxWidth()
                .background(AppColors().darkGreen)
        ) {
            Text(
                text = "Acesse sua conta",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().white,
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Faça login para ter acesso a todos os recursos e obter o máximo dessa experiência",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().lightGrey,
                fontSize = 12.sp,
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
                    .padding(30.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

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
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                "Senha",
                                fontFamily = AppFonts().montserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = AppColors().grey
                            )
                        },
                        leadingIcon = {
                            AppIcons.Outline.KeyRound(24.dp, AppColors().black)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                if (passwordVisibility)
                                    AppIcons.Outline.Eye(24.dp, AppColors().grey)
                                else
                                    AppIcons.Outline.EyeClosed(24.dp, AppColors().grey)
                            }
                        },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Password
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AppColors().black,
                            unfocusedTextColor = AppColors().black,
                            focusedBorderColor = AppColors().lightGrey,
                            unfocusedBorderColor = AppColors().lightGrey,
                            cursorColor = AppColors().black,
                            focusedLeadingIconColor = AppColors().grey,
                            unfocusedLeadingIconColor = AppColors().black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AppColors().lightGreen
                            ),
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = "Lembre-se de mim",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Medium,
                            color = AppColors().black,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Esqueci minha senha?",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors().darkGreen,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onForgotPasswordClick() }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = annotated,
                        fontSize = 10.sp,
                        color = AppColors().grey,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = { onLoginSuccess(email, password, rememberMe) },
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
                            text = "Fazer parte!",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Não tem uma conta?",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Medium,
                            color = AppColors().darkGreen,
                            fontSize = 12.sp
                        )
                        Text(
                            text = " Faça o seu cadastro",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors().darkGreen,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onSignUpClick() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}