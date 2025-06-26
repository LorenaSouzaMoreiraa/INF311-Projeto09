package com.example.inf311_projeto09.ui.screens.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.inf311_projeto09.BuildConfig
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.api.RubeusApi
import com.example.inf311_projeto09.helper.PasswordHelper
import com.example.inf311_projeto09.model.User
import com.example.inf311_projeto09.ui.components.NavBar
import com.example.inf311_projeto09.ui.components.NavBarOption
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

// TODO: pedir permissão para acessar galeria?

@Composable
fun EditProfileScreen(
    user: User,
    navController: NavHostController,
    onDeactivateAccount: () -> Unit = {}
) {
    var isEditingMode by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uploadImageToImgBB(context, it) { url ->
                if (url != null) {
                    RubeusApi.updateUser(
                        user,
                        user.name,
                        user.school,
                        user.password,
                        user.enableNotifications,
                        url
                    )
                }
            }
        }
    }

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

                EditProfileFields(
                    user = user,
                    isEditingMode = isEditingMode,
                    onSave = {
                        isEditingMode = false
                    },
                    onDeactivateAccount = onDeactivateAccount
                )
            }

            NavBar(navController, NavBarOption.PROFILE, user)
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
                modifier = Modifier
                    .size(130.dp),
                contentAlignment = Alignment.Center
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
                        .size(130.dp)
                        .clip(CircleShape)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(35.dp)
                        .background(color = AppColors().darkGreen, shape = CircleShape)
                        .clickable(onClick = {
                            launcher.launch("image/*")
                        }),
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

fun uploadImageToImgBB(
    context: Context,
    imageUri: Uri,
    onResult: (String?) -> Unit
) {
    val imageBase64 = compressImage(context, imageUri, quality = 70)

    if (imageBase64 == null) {
        onResult(null)
        return
    }

    val client = OkHttpClient()

    val requestBody = FormBody.Builder()
        .add("key", BuildConfig.IMGBB_API_KEY)
        .add("image", imageBase64)
        .build()

    val request = Request.Builder()
        .url("https://api.imgbb.com/1/upload")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            onResult(null)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val bodyString = response.body?.string()
                val json = JSONObject(bodyString ?: "")
                val imageUrl = json.getJSONObject("data").getString("url")
                onResult(imageUrl)
            } else {
                onResult(null)
            }
        }
    })
}

fun compressImage(context: Context, imageUri: Uri, quality: Int = 70): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val outputStream = ByteArrayOutputStream()
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val imageBytes = outputStream.toByteArray()

        Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
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
            modifier = Modifier.size(33.dp)
        ) {
            AppIcons.Outline.EditIcon(
                30.dp,
                if (isEditingMode) AppColors().lightGreen
                else AppColors().white
            )
        }
    }
}

@Composable
fun EditProfileFields(
    user: User,
    isEditingMode: Boolean,
    onSave: () -> Unit,
    onDeactivateAccount: () -> Unit,
) {
    val cpf by remember { mutableStateOf(user.cpf) }
    var name by remember { mutableStateOf(user.name) }
    var university by remember { mutableStateOf(user.school) }
    val email by remember { mutableStateOf(user.email) }
    var receiveNotifications by remember { mutableStateOf(user.enableNotifications) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val universities = remember { RubeusApi.listSchools().toList() }

    CpfField(cpf = cpf)

    Spacer(modifier = Modifier.height(18.dp))

    NameField(
        originalName = user.name,
        name = name,
        onNameChange = { name = it },
        isEditingMode = isEditingMode
    )

    Spacer(modifier = Modifier.height(18.dp))

    UniversityField(
        originalUniversity = user.school,
        university = university,
        onUniversityChange = { university = it },
        isEditingMode = isEditingMode,
        universities = universities
    )

    Spacer(modifier = Modifier.height(18.dp))

    EmailField(email = email)

    Spacer(modifier = Modifier.height(18.dp))

    PasswordField(
        isEditingMode = isEditingMode,
        oldPassword = oldPassword,
        onOldPasswordChange = { oldPassword = it },
        newPassword = newPassword,
        onNewPasswordChange = { newPassword = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        oldPasswordVisible = oldPasswordVisible,
        onToggleOldPasswordVisibility = { oldPasswordVisible = !oldPasswordVisible },
        newPasswordVisible = newPasswordVisible,
        onToggleNewPasswordVisibility = { newPasswordVisible = !newPasswordVisible },
        confirmPasswordVisible = confirmPasswordVisible,
        onToggleConfirmPasswordVisibility = { confirmPasswordVisible = !confirmPasswordVisible }
    )

    Spacer(modifier = Modifier.height(10.dp))

    // TODO: voltar depois que implementar a "Notificação"
    NotificationsSwitch(
        originalReceiveNotifications = user.enableNotifications,
        receiveNotifications = receiveNotifications,
        onNotificationsChange = { receiveNotifications = it },
        isEditingMode = isEditingMode
    )

    Spacer(modifier = Modifier.height(10.dp))

    ActionButton(
        isEditingMode = isEditingMode,
        onSaveClick = {
            if (name.isEmpty() || university.isEmpty()) {
                // TODO: não pode opções vazias
            } else if (oldPassword.isEmpty() && newPassword.isEmpty() && confirmPassword.isEmpty()) {
                if (name != user.name || university != user.school || receiveNotifications != user.enableNotifications) {
                    RubeusApi.updateUser(
                        user,
                        name,
                        university,
                        user.password,
                        receiveNotifications,
                        user.imageUrl
                    )
                    onSave()
                    oldPassword = ""
                    newPassword = ""
                    confirmPassword = ""
                }
            } else {
                if (oldPassword.isEmpty()) {
                    // TODO: não pode vazia
                } else if (newPassword.isEmpty()) {
                    // TODO: não pode vazia
                } else if (confirmPassword.isEmpty()) {
                    // TODO: não pode vazia
                } else if (!PasswordHelper.verifyPassword(oldPassword, user.password)) {
                    // TODO: senha antiga incorreta
                } else if (newPassword != confirmPassword) {
                    // TODO: senhas não batem
                } else {
                    RubeusApi.updateUser(
                        user,
                        name,
                        university,
                        PasswordHelper.hashPassword(newPassword),
                        receiveNotifications,
                        user.imageUrl
                    )
                    onSave()
                    onSave()
                    oldPassword = ""
                    newPassword = ""
                    confirmPassword = ""
                }
            }
        },
        onDeactivateClick = {
            RubeusApi.deleteUser(user.id)
            onDeactivateAccount()
            // TODO: Perguntar se realmente deseja excluir
        }
    )
}

@Composable
fun CpfField(cpf: String) {
    OutlinedTextField(
        value = "${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.${
            cpf.substring(
                6,
                9
            )
        }-${cpf.substring(9, 11)}",
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
            cursorColor = AppColors().black,
            focusedContainerColor = AppColors().darkGrey.copy(alpha = 0.4f),
            unfocusedContainerColor = AppColors().darkGrey.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        readOnly = true
    )
}

@Composable
fun NameField(
    originalName: String,
    name: String,
    onNameChange: (String) -> Unit,
    isEditingMode: Boolean
) {
    OutlinedTextField(
        value = if (isEditingMode) name else originalName,
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
    originalUniversity: String,
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
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
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
            value = originalUniversity,
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
            cursorColor = AppColors().black,
            focusedContainerColor = AppColors().darkGrey.copy(alpha = 0.4f),
            unfocusedContainerColor = AppColors().darkGrey.copy(alpha = 0.4f)
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
    isEditingMode: Boolean,
    oldPassword: String,
    onOldPasswordChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    oldPasswordVisible: Boolean,
    onToggleOldPasswordVisibility: () -> Unit,
    newPasswordVisible: Boolean,
    onToggleNewPasswordVisibility: () -> Unit,
    confirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit
) {
    if (!isEditingMode) {
        OutlinedTextField(
            value = "********",
            onValueChange = {},
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
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            visualTransformation = VisualTransformation.None,
            leadingIcon = {
                AppIcons.Outline.KeyRound(24.dp, AppColors().black)
            }
        )
    } else {
        Column {
            PasswordFieldItem(
                label = "Senha Antiga",
                password = oldPassword,
                onPasswordChange = onOldPasswordChange,
                passwordVisible = oldPasswordVisible,
                onTogglePasswordVisibility = onToggleOldPasswordVisibility
            )
            Spacer(modifier = Modifier.height(12.dp))
            PasswordFieldItem(
                label = "Nova Senha",
                password = newPassword,
                onPasswordChange = onNewPasswordChange,
                passwordVisible = newPasswordVisible,
                onTogglePasswordVisibility = onToggleNewPasswordVisibility
            )
            Spacer(modifier = Modifier.height(12.dp))
            PasswordFieldItem(
                label = "Confirmar Senha",
                password = confirmPassword,
                onPasswordChange = onConfirmPasswordChange,
                passwordVisible = confirmPasswordVisible,
                onTogglePasswordVisibility = onToggleConfirmPasswordVisibility
            )
        }
    }
}

@Composable
fun PasswordFieldItem(
    label: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password
        ),
        label = {
            Text(
                label,
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
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = {
            AppIcons.Outline.KeyRound(24.dp, AppColors().black)
        },
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisibility) {
                if (passwordVisible)
                    AppIcons.Outline.Eye(24.dp, AppColors().grey)
                else
                    AppIcons.Outline.EyeClosed(24.dp, AppColors().grey)
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun NotificationsSwitch(
    originalReceiveNotifications: Boolean,
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
            checked = if (isEditingMode) receiveNotifications else originalReceiveNotifications,
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
    EditProfileScreen(
        user = User(
            0,
            "Erick Soares",
            User.UserRole.USER,
            "teste@teste.com",
            "12345678900",
            "Universidade Federal de Viçosa (UFV)",
            "****",
            true,
            null
        ), navController = rememberNavController()
    )
}