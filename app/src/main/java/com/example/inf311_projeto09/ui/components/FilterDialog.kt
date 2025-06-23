package com.example.inf311_projeto09.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inf311_projeto09.ui.screens.admin.EventFilter
import com.example.inf311_projeto09.ui.utils.AppColors
import com.example.inf311_projeto09.ui.utils.AppFonts

@Composable
fun FilterDialog(
    selected: Set<EventFilter>,
    onDismiss: () -> Unit,
    onConfirm: (Set<EventFilter>) -> Unit
) {
    val tempSelected = remember { mutableStateOf(selected) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Selecionar Filtros",
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = AppColors().darkGreen
            )
        },
        text = {
            Column {
                EventFilter.values().forEach { filter ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                tempSelected.value = if (tempSelected.value.contains(filter)) {
                                    tempSelected.value - filter
                                } else {
                                    tempSelected.value + filter
                                }
                            }
                            .padding(vertical = 6.dp)
                    ) {
                        Checkbox(
                            checked = tempSelected.value.contains(filter),
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = AppColors().darkGreen,
                                checkmarkColor = AppColors().white
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(filter.label)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempSelected.value) }) {
                Text(
                    "Aplicar",
                    color = AppColors().darkGreen
                )

            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancelar",
                    color = AppColors().darkGreen
                )
            }
        }
    )
}