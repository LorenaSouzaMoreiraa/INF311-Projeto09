package com.inf311_projeto09.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inf311_projeto09.ui.utils.AppColors
import com.inf311_projeto09.ui.utils.AppFonts

@Composable
fun <T> FilterDialog(
    title: String,
    filtersToShow: List<Pair<T, String>>,
    selected: Set<T>,
    onDismiss: () -> Unit,
    onConfirm: (Set<T>) -> Unit
) {
    var tempSelectedFilters by remember { mutableStateOf(selected.toMutableSet()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontFamily = AppFonts().montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = AppColors().darkGreen
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                filtersToShow.forEach { (filter, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                tempSelectedFilters = tempSelectedFilters.toMutableSet().apply {
                                    if (contains(filter)) remove(filter) else add(filter)
                                }
                            }
                            .padding(vertical = 6.dp)
                    ) {
                        Checkbox(
                            checked = tempSelectedFilters.contains(filter),
                            onCheckedChange = { isChecked ->
                                tempSelectedFilters = tempSelectedFilters.toMutableSet().apply {
                                    if (isChecked) add(filter) else remove(filter)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AppColors().darkGreen,
                                uncheckedColor = AppColors().lightGrey,
                                checkmarkColor = AppColors().white
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = label,
                            fontFamily = AppFonts().montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = AppColors().black
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempSelectedFilters) }) {
                Text(
                    "Aplicar",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors().darkGreen
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancelar",
                    fontFamily = AppFonts().montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors().darkGreen
                )
            }
        },
    )
}