package com.example.inf311_projeto09.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.example.inf311_projeto09.ui.utils.AppColors
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableDatePickerDialog(
    showDialog: Boolean,
    initialDate: Calendar = Calendar.getInstance(),
    onDismiss: () -> Unit,
    onDateSelected: (Calendar) -> Unit
) {
    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate.timeInMillis,
            initialDisplayMode = DisplayMode.Picker
        )
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                                timeInMillis = millis
                            }

                            val finalDate = Calendar.getInstance().apply {
                                set(Calendar.YEAR, newDate[Calendar.YEAR])
                                set(Calendar.MONTH, newDate[Calendar.MONTH])
                                set(Calendar.DAY_OF_MONTH, newDate[Calendar.DAY_OF_MONTH])
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }

                            onDateSelected(finalDate)
                        }
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
            colors = DatePickerDefaults.colors(
                containerColor = AppColors().white
            )
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = AppColors().white,
                    titleContentColor = AppColors().darkGreen,
                    headlineContentColor = AppColors().darkGreen,
                    weekdayContentColor = AppColors().grey,
                    navigationContentColor = AppColors().darkGreen,
                    yearContentColor = AppColors().darkGreen,
                    currentYearContentColor = AppColors().darkGreen,
                    selectedYearContentColor = AppColors().black,
                    selectedYearContainerColor = AppColors().lightGreen,
                    dayContentColor = AppColors().darkGreen,
                    selectedDayContentColor = AppColors().black,
                    selectedDayContainerColor = AppColors().lightGreen,
                    todayContentColor = AppColors().darkGreen,
                    todayDateBorderColor = AppColors().lightGreen,
                    dividerColor = AppColors().darkGreen,
                )
            )
        }
    }
}