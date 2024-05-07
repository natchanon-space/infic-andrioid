package com.natch.app.infic.writer.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.writeFictionToJsonFile
import com.natch.app.infic.writer.component.MultiSelectionList
import com.natch.app.infic.writer.component.rememberMultiSelectionState

@Composable
fun EditParameterScreen(
    viewModel: FictionViewModel
) {
    val context = LocalContext.current

    val selectedItems = remember { mutableStateListOf<String>() }
    val multiSelectionState = rememberMultiSelectionState()

    val parameterNames = viewModel.currentFiction.value!!.parameters.keys.toList()

    var addParameterDialog by rememberSaveable { mutableStateOf(false) }
    var editParameterDialog by rememberSaveable { mutableStateOf(false) }
    var editParameterName by rememberSaveable { mutableStateOf("") }

    val onFictionUpdate = {
        writeFictionToJsonFile(viewModel.currentFiction.value!!, context)
    }

    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Parameter Setting")
            Spacer(modifier = Modifier
                .weight(1f)
                .fillMaxWidth())
            if (!multiSelectionState.isMultiSelectionModeEnabled) {
                // add parameter button
                IconButton(onClick = { addParameterDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Icon")
                }
            }
            else {
                // cancel button
                IconButton(onClick = {
                    selectedItems.clear()
                    multiSelectionState.isMultiSelectionModeEnabled = false
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Cancel Icon")
                }
                // delete button
                IconButton(onClick = {
                    for (parameterName in selectedItems) {
                        viewModel.currentFiction.value!!.parameters.remove(parameterName)
                    }
                    selectedItems.clear()
                    onFictionUpdate()
                    multiSelectionState.isMultiSelectionModeEnabled = false
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Icon")

                }
            }
        }
        MultiSelectionList(
            modifier = Modifier,
            state = multiSelectionState,
            items = parameterNames,
            selectedItems = selectedItems,
            itemContent = { parameterName ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(parameterName + ": " + viewModel.currentFiction.value!!.parameters[parameterName])
                }
            },
            key = { parameterNames ->
                parameterNames
            },
            onClick = { parameterNames ->
                if (multiSelectionState.isMultiSelectionModeEnabled) {
                   if (parameterNames in selectedItems) {
                       selectedItems.remove(parameterNames)
                   } else {
                       selectedItems.add(parameterNames)
                   }
                } else {
                    // open edit dialog
                    editParameterName = parameterNames
                    editParameterDialog = true
                }
            }
        )
    }

    // TODO: for all dialog, handle invalid parameter name and empty field and already exists parameter name as error

    if (addParameterDialog) {
        var parameterName by rememberSaveable { mutableStateOf("") }
        var defaultValue by rememberSaveable { mutableStateOf("") }

        Dialog(onDismissRequest = { addParameterDialog = false }) {
            Card(modifier = Modifier.wrapContentSize()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Add Parameter")

                    OutlinedTextField(
                        value = parameterName,
                        onValueChange = { parameterName = it },
                        label = { Text("name") },
                        maxLines = 1,
                    )
                    OutlinedTextField(
                        value = defaultValue,
                        onValueChange = { defaultValue = it },
                        label = { Text("default value" )},
                        maxLines = 1,
                    )

                    Row(
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Button(
                            modifier = Modifier.padding(5.dp, 0.dp),
                            onClick = { addParameterDialog = false }
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                viewModel.currentFiction.value!!.parameters[parameterName] = defaultValue
                                onFictionUpdate()
                                addParameterDialog = false
                            }
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }

    if (editParameterDialog) {
        var parameterName by rememberSaveable { mutableStateOf(editParameterName) }
        var defaultValue by rememberSaveable { mutableStateOf("") }

        Dialog(onDismissRequest = { editParameterDialog = false }) {
            Card(modifier = Modifier.wrapContentSize()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Update Parameter")

                    OutlinedTextField(
                        value = parameterName,
                        onValueChange = { parameterName = it },
                        label = { Text("name") },
                        maxLines = 1,
                    )
                    OutlinedTextField(
                        value = defaultValue,
                        onValueChange = { defaultValue = it },
                        label = { Text("default value" )},
                        maxLines = 1,
                    )

                    Row(
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Button(
                            modifier = Modifier.padding(5.dp, 0.dp),
                            onClick = { editParameterDialog = false }
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                // remove previous parameter name
                                viewModel.currentFiction.value!!.parameters.remove(editParameterName)
                                // update new one
                                viewModel.currentFiction.value!!.parameters[parameterName] = defaultValue
                                onFictionUpdate()
                                editParameterDialog = false
                            }
                        ) {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}