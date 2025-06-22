package com.example.inf311_projeto09.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.inf311_projeto09.ui.utils.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableTimePickerDialog(
    showDialog: Boolean,
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Selecione a Hora", color = AppColors().darkGreen) },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        containerColor = AppColors().white,
                        clockDialColor = AppColors().transparent,
                        selectorColor = AppColors().darkGreen,
                        periodSelectorSelectedContainerColor = AppColors().lightGreen,
                        periodSelectorUnselectedContainerColor = AppColors().transparent,
                        periodSelectorSelectedContentColor = AppColors().black,
                        periodSelectorUnselectedContentColor = AppColors().white,
                        timeSelectorSelectedContainerColor = AppColors().lightGreen,
                        timeSelectorUnselectedContainerColor = AppColors().transparent,
                        timeSelectorSelectedContentColor = AppColors().black,
                        timeSelectorUnselectedContentColor = AppColors().black,
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(timePickerState.hour, timePickerState.minute)
                        onDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppColors().darkGreen
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppColors().darkGreen
                    )
                ) {
                    Text("Cancelar")
                }
            },
            containerColor = AppColors().white,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReusableTimePickerDialog() {
    ReusableTimePickerDialog(
        showDialog = true,
        initialHour = 13,
        initialMinute = 30,
        onDismiss = {},
        onTimeSelected = { _, _ -> }
    )
}