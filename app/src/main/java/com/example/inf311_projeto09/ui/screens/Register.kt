package com.example.inf311_projeto09.ui.screens

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.helper.PasswordHelper
import com.example.inf311_projeto09.model.UniversitiesMock
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit = {},
    onSignUpSuccess: (Boolean) -> Unit = {},
    onLoginClick: () -> Unit = {},
    universitiesMock: UniversitiesMock = UniversitiesMock(),
    userRole: User.UserRole
) {
    var cpf by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val universities = remember { universitiesMock.getUniversitiesList() }

    val focusManager: FocusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val maxVisibleItems = 5
    val itemHeight = 50.dp
    val dropdownMaxHeight = itemHeight * maxVisibleItems
    val customDropdownShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )

    val annotatedTermsAndPrivacy = buildAnnotatedString {
        append("Ao prosseguir, você declara estar de acordo com os ")

        // TODO: mudar link dos termos de uso
        val terms = LinkAnnotation.Url(
            "https://github.com/LorenaSouzaMoreiraa/INF311-Projeto09",
            TextLinkStyles(
                style = SpanStyle(textDecoration = TextDecoration.Underline),
            )
        )
        withLink(terms) { append("Termos de Uso") }

        append(" e a ")

        val privacy = LinkAnnotation.Url(
            "https://github.com/LorenaSouzaMoreiraa/INF311-Projeto09",
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
                .padding(top = 110.dp)
                .fillMaxWidth()
                .background(AppColors().darkGreen)
        ) {
            Text(
                text = "Faça parte",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().white,
                fontSize = 30.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Realize o cadastro para ter acesso a todos os recursos e obter o máximo dessa experiência",
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
                        AppColors().white,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(25.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = cpf,
                        onValueChange = { newValue ->
                            cpf = newValue.filter { it.isDigit() }.take(11)
                        },
                        label = {
                            Text(
                                "CPF",
                                fontFamily = AppFonts().montserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = AppColors().grey
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AppColors().black,
                            unfocusedTextColor = AppColors().black,
                            focusedBorderColor = AppColors().lightGrey,
                            unfocusedBorderColor = AppColors().lightGrey,
                            cursorColor = AppColors().black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = VisualTransformation { text ->
                            text.text.toCpfMasked()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = {
                            Text(
                                "Nome completo",
                                fontFamily = AppFonts().montserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = AppColors().grey
                            )
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

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = school,
                            onValueChange = { school = it },
                            readOnly = true,
                            label = {
                                Text(
                                    "Instituição de Ensino",
                                    fontFamily = AppFonts().montserrat,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = AppColors().grey
                                )
                            },
                            trailingIcon = {
                                AppIcons.Outline.ArrowDown(
                                    boxSize = 24.dp,
                                    colorIcon = AppColors().grey,
                                    modifier = Modifier.graphicsLayer(rotationZ = if (expanded) 180f else 0f)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AppColors().black,
                                unfocusedTextColor = AppColors().black,
                                focusedBorderColor = AppColors().lightGrey,
                                unfocusedBorderColor = AppColors().lightGrey,
                                cursorColor = AppColors().black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(AppColors().white)
                                .heightIn(max = dropdownMaxHeight),
                            shape = customDropdownShape
                        ) {
                            universities.forEachIndexed { index, selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        school = selectionOption
                                        expanded = false
                                    }
                                )

                                if (index < universities.size - 1) {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(0.8.dp)
                                            .background(AppColors().lightGrey)
                                    )
                                }
                            }
                        }
                    }

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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = {
                            Text(
                                "Confirme a senha",
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
                            IconButton(onClick = {
                                confirmPasswordVisibility = !confirmPasswordVisibility
                            }) {
                                if (confirmPasswordVisibility)
                                    AppIcons.Outline.Eye(24.dp, AppColors().grey)
                                else
                                    AppIcons.Outline.EyeClosed(24.dp, AppColors().grey)
                            }
                        },
                        visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
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

                    Text(
                        text = annotatedTermsAndPrivacy,
                        fontSize = 10.sp,
                        color = AppColors().grey,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            onSignUpSuccess(
                                validateRegister(
                                    name,
                                    email,
                                    school,
                                    password,
                                    confirmPassword,
                                    cpf,
                                    userRole
                                )
                            )
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
                            text = "Realizar cadastro",
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
                            text = "Já tem uma conta?",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Medium,
                            color = AppColors().darkGreen,
                            fontSize = 12.sp
                        )
                        Text(
                            text = " Acesse sua conta",
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors().darkGreen,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onLoginClick() }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

fun validateRegister(
    name: String,
    email: String,
    school: String,
    password: String,
    confirmPassword: String,
    cpf: String,
    userRole: User.UserRole
): Boolean {
    // TODO: validar os dados antes
    if (password.isNotEmpty() && password == confirmPassword) {
        return RubeusApi.registerUser(
            name,
            email,
            school,
            PasswordHelper.hashPassword(password),
            cpf,
            userRole
        )
    }
    return false
}

fun formatCpf(cpf: String): String {
    val trimmed = if (cpf.length >= 11) cpf.substring(0..10) else cpf
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i == 2 || i == 5) out += "."
        if (i == 8) out += "-"
    }
    return out
}

fun createCpfOffsetMapping(): OffsetMapping {
    return object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 2) return offset
            if (offset <= 5) return offset + 1
            if (offset <= 8) return offset + 2
            return offset + 3
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset - 1
            if (offset <= 11) return offset - 2
            return offset - 3
        }
    }
}

fun String.toCpfMasked(): TransformedText {
    val originalCpfDigits = this.filter { it.isDigit() }.take(11)
    val maskedCpf = formatCpf(originalCpfDigits)
    val offsetMapping = createCpfOffsetMapping()
    return TransformedText(AnnotatedString(maskedCpf), offsetMapping)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(userRole = User.UserRole.USER)
}