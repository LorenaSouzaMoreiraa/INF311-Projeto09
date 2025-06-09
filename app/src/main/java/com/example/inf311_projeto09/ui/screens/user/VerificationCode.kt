package com.example.inf311_projeto09.ui.screens.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.R
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts
import com.example.inf311_projeto09.ui.utils.AppIcons

@Composable
fun VerificationCodeScreen(
    onBack: () -> Unit = {}
) {
    val code = remember { mutableStateListOf("", "", "", "") }
    val focusRequesters = remember { List(4) { FocusRequester() } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors().darkGreen)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 24.dp, end = 24.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(30.dp)
            ) {
                AppIcons.Filled.CircleClose(
                    boxSize = 30.dp,
                    colorIcon = AppColors().lightGreen,
                    backgroundColorIcon = AppColors().darkGreen
                )
            }

            Text(
                text = "Check-in",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                color = AppColors().white,
                fontSize = 26.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(85.dp)
                            .border(
                                BorderStroke(2.5.dp, AppColors().lightGreen),
                                RoundedCornerShape(25.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = TextFieldValue(
                                text = code[index],
                                selection = TextRange(code[index].length)
                            ),
                            onValueChange = { newValue ->
                                if (newValue.text.length <= 1 && newValue.text.all { it.isDigit() }) {
                                    code[index] = newValue.text

                                    if (newValue.text.isNotEmpty() && index < code.size - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            cursorBrush = SolidColor(AppColors().lightGreen),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = AppColors().white,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontFamily = AppFonts().montserrat
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 17.dp)
                                .focusRequester(focusRequesters[index])
                                .onKeyEvent { event ->
                                    if (event.key == Key.Backspace && code[index].isEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                        true
                                    } else {
                                        false
                                    }
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Para prosseguir com a confirmação de sua presença, solicitamos que insira o código de verificação correspondente",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.Medium,
                color = AppColors().white,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(1f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerificationCodeScreenPreview() {
    VerificationCodeScreen()
}