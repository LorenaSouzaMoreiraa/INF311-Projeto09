package com.example.inf311_projeto09.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.components.NavBar
import com.example.inf311_projeto09.ui.components.NavBarOption
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons

@Composable
fun EditProfileScreen(
    userId: String = "idUsuario",
    navController: NavHostController,
    choosePhoto: () -> Unit = {},
) {
    var isEditingMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 130.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = AppColors().offWhite,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 30.dp, vertical = 40.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                EditProfileFields(userId, isEditingMode)
            }

            NavBar(navController, NavBarOption.PROFILE)
        }

        TopBarEditProfile(
            isEditingMode = isEditingMode,
            onToggleEditMode = { isEditingMode = !isEditingMode },
            onBack = { navController.popBackStack() }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier.size(130.dp),
                contentAlignment = Alignment.Center
            ) {
                val profileImage = 1
                if (profileImage == null) {
                    AppIcons.Outline.CircleUserRound(120.dp)
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Foto do usuário",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(118.dp)
                            .clip(CircleShape)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(35.dp)
                        .background(color = AppColors().darkGreen, shape = CircleShape)
                        .clickable(onClick = choosePhoto),
                    contentAlignment = Alignment.Center
                ) {
                    AppIcons.Filled.Camera(
                        boxSize = 25.dp,
                        colorIcon = AppColors().darkGreen,
                        backgroundColorIcon = AppColors().white
                    )
                }
            }
        }
    }
}

@Composable
fun TopBarEditProfile(
    onBack: () -> Unit = {},
    isEditingMode: Boolean,
    onToggleEditMode: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(30.dp)
        ) {
            AppIcons.Outline.CircleArrowLeft(30.dp, AppColors().white)
        }

        IconButton(
            onClick = onToggleEditMode,
            modifier = Modifier.size(30.dp)
        ) {
            AppIcons.Outline.EditIcon(
                30.dp,
                if (isEditingMode) AppColors().lightGreen
                else AppColors().white
            )
        }
    }
}

fun getUserData(userId: String): User {
    return User(
        Integer.valueOf(userId),
        "Nome completo",
        User.UserRole.USER,
        "rubeus@rubeus.com",
        "000.000.000-00",
        "Nome universidade",
        "testeSenha"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileFields(
    userId: String,
    isEditingMode: Boolean
) {
    val user = getUserData(userId)

    val cpf by remember { mutableStateOf(user.cpf) }
    var name by remember { mutableStateOf(user.name) }
    var university by remember { mutableStateOf(user.school) }
    val email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf(user.password) }
    var passwordVisible by remember { mutableStateOf(false) }
    var receiveNotifications by remember { mutableStateOf(true) }

    val universities = remember { RubeusApi.listSchools().toList() }

    CpfField(cpf = cpf)

    Spacer(modifier = Modifier.height(18.dp))

    NameField(name = name, onNameChange = { name = it }, isEditingMode = isEditingMode)

    Spacer(modifier = Modifier.height(18.dp))

    UniversityField(
        university = university,
        onUniversityChange = { university = it },
        isEditingMode = isEditingMode,
        universities = universities
    )

    Spacer(modifier = Modifier.height(18.dp))

    EmailField(email = email)

    Spacer(modifier = Modifier.height(18.dp))

    PasswordField(
        password = password,
        onPasswordChange = { password = it },
        isEditingMode = isEditingMode,
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible }
    )

    Spacer(modifier = Modifier.height(10.dp))

    NotificationsSwitch(
        receiveNotifications = receiveNotifications,
        onNotificationsChange = { receiveNotifications = it },
        isEditingMode = isEditingMode
    )

    Spacer(modifier = Modifier.height(10.dp))

    ActionButton(
        isEditingMode = isEditingMode,
        onSaveClick = { }, // TODO: Lógica para "Salvar alterações"
        onDeactivateClick = { } // TODO: Lógica para "Desativar conta"
    )
}

@Composable
fun CpfField(cpf: String) {
    OutlinedTextField(
        value = cpf,
        onValueChange = {},
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
        modifier = Modifier.fillMaxWidth(),
        readOnly = true
    )
}

@Composable
fun NameField(name: String, onNameChange: (String) -> Unit, isEditingMode: Boolean) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
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
        modifier = Modifier.fillMaxWidth(),
        readOnly = !isEditingMode
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversityField(
    university: String,
    onUniversityChange: (String) -> Unit,
    isEditingMode: Boolean,
    universities: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val maxVisibleItems = 5
    val itemHeight = 50.dp
    val dropdownMaxHeight = itemHeight * maxVisibleItems
    val customDropdownShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )

    if (isEditingMode) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = university,
                onValueChange = {},
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppColors().black,
                    unfocusedTextColor = AppColors().black,
                    focusedBorderColor = AppColors().lightGrey,
                    unfocusedBorderColor = AppColors().lightGrey,
                    cursorColor = AppColors().black
                ),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    AppIcons.Outline.ArrowDown(
                        boxSize = 24.dp,
                        colorIcon = AppColors().grey
                    )
                },
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
                            onUniversityChange(selectionOption)
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
    } else {
        OutlinedTextField(
            value = university,
            onValueChange = {},
            label = {
                Text(
                    "Instituição de Ensino",
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
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
    }
}

@Composable
fun EmailField(email: String) {
    OutlinedTextField(
        value = email,
        onValueChange = {},
        label = {
            Text(
                "Email",
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
        leadingIcon = {
            AppIcons.Outline.Mail(24.dp, AppColors().black)
        },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true
    )
}

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    isEditingMode: Boolean,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(
                "Senha",
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
        visualTransformation = if (isEditingMode && passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = {
            AppIcons.Outline.KeyRound(24.dp, AppColors().black)
        },
        trailingIcon = if (isEditingMode) {
            {
                IconButton(onClick = onTogglePasswordVisibility) {
                    if (passwordVisible)
                        AppIcons.Outline.Eye(24.dp, AppColors().grey)
                    else
                        AppIcons.Outline.EyeClosed(24.dp, AppColors().grey)
                }
            }
        } else null,
        modifier = Modifier.fillMaxWidth(),
        readOnly = !isEditingMode
    )
}

@Composable
fun NotificationsSwitch(
    receiveNotifications: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    isEditingMode: Boolean
) {
    val checkedTrackColor = AppColors().green
    val checkedThumbColor = AppColors().white
    val uncheckedTrackColor = AppColors().lightGrey
    val uncheckedThumbColor = AppColors().white

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Receber notificações",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = AppColors().black,
            fontSize = 17.sp
        )
        Switch(
            checked = receiveNotifications,
            onCheckedChange = onNotificationsChange,
            enabled = isEditingMode,
            thumbContent = {
                Box(
                    modifier = Modifier
                        .size(SwitchDefaults.IconSize)
                        .background(AppColors().white, CircleShape)
                )
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = checkedTrackColor,
                checkedThumbColor = checkedThumbColor,
                uncheckedTrackColor = uncheckedTrackColor,
                uncheckedThumbColor = uncheckedThumbColor,
                uncheckedBorderColor = AppColors().transparent,
                disabledCheckedTrackColor = checkedTrackColor,
                disabledCheckedThumbColor = checkedThumbColor,
                disabledUncheckedTrackColor = uncheckedTrackColor,
                disabledUncheckedThumbColor = uncheckedThumbColor
            ),
            modifier = Modifier.graphicsLayer {
                scaleY = 0.9f
            }
        )
    }
}

@Composable
fun ActionButton(isEditingMode: Boolean, onSaveClick: () -> Unit, onDeactivateClick: () -> Unit) {
    Button(
        onClick = {
            if (isEditingMode) {
                onSaveClick()
            } else {
                onDeactivateClick()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEditingMode) AppColors().green
            else AppColors().lightRed
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = if (isEditingMode) "Salvar alterações" else "Desativar conta",
            fontFamily = AppFonts().montserrat,
            fontWeight = FontWeight.SemiBold,
            color = if (isEditingMode) AppColors().black else AppColors().red,
            fontSize = 16.sp
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(navController = rememberNavController())
}