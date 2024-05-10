package com.natch.app.infic.writer.component

import android.util.Log
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
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownSearch(
    items: List<T>,
    mapper: (T) -> String,
    modifier: Modifier = Modifier,
    defaultSelectedItem: T? = null,
    advancedMapper: (String) -> String = { s -> s },
    onSelectedCallBack: (String) -> Unit = { },
) {
    val mixMapper = { item: T -> advancedMapper(mapper(item)) }
    val originalOptions = items.map(mapper)
    val options = items.map(mixMapper)
    var expanded by remember { mutableStateOf(false) }
    val defaultOptionIndex =
        max(options.indexOf(defaultSelectedItem?.let { mixMapper(it) } ?: 0), 0)
    var selectedOptionText by remember {
        mutableStateOf(options[defaultOptionIndex])
    }

    // return default option
//    Log.d("DEBUG", "initial callback -> default value")
//    onSelectedCallBack(originalOptions[defaultOptionIndex])

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
            options.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption)
                    },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        Log.d("DEBUG", "onSelectCallback -> set value")
                        Log.d("DEBUG", originalOptions[index])
                        onSelectedCallBack(originalOptions[index])
                    }
                )
            }
        }
    }
}