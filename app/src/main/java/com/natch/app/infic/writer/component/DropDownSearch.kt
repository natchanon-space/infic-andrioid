package com.natch.app.infic.writer.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.lang.Integer.min
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownSearch(
    items: List<T>,
    defaultSelectedItem: T? = null,
    mapper: (T) -> String,
    onSelectedCallBack: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    val options = items.map(mapper)
    var expanded by remember { mutableStateOf(false) }
    var defaultOptionIndex = max(options.indexOf(defaultSelectedItem?.let { mapper(it) } ?: 0), 0)
    var selectedOptionText by remember {
        mutableStateOf(options[defaultOptionIndex])
    }

    // return default option
    onSelectedCallBack(selectedOptionText)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            modifier = modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            label = { Text("Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption)
                    },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onSelectedCallBack(selectionOption)
                    }
                )
            }
        }
    }
}