package com.natch.app.infic.writer.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.natch.app.infic.model.Fiction
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.conditional
import com.natch.app.infic.utils.deleteFictionFromDir
import com.natch.app.infic.utils.readAllFictionsFromDir
import com.natch.app.infic.utils.writeFictionToJsonFile
import com.natch.app.infic.writer.component.FictionCard
import com.natch.app.infic.writer.component.MultiSelectionList
import com.natch.app.infic.writer.component.rememberMultiSelectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFictionScreen(
    viewModel: FictionViewModel,
    selectFictionCallback: () -> Unit = { }
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    val fictions = remember { mutableStateListOf<Fiction>() }
    fictions.clear()
    fictions.addAll(readAllFictionsFromDir(context))

    val selectedItems = remember { mutableStateListOf<Fiction>() }
    val multiSelectionState = rememberMultiSelectionState()

    var addFictionDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (multiSelectionState.isMultiSelectionModeEnabled) {
                // delete fictions
                // TODO: add dialog before confirming delete
                TopAppBar(
                    title = { Text("Select") },
                    navigationIcon = {
                        IconButton(onClick = {
                            selectedItems.clear()
                            multiSelectionState.isMultiSelectionModeEnabled = false
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Cancel")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            for (item in selectedItems) {
                                deleteFictionFromDir(item, context)
                            }
                            selectedItems.clear()
                            fictions.clear()
                            fictions.addAll(readAllFictionsFromDir(context))
                            multiSelectionState.isMultiSelectionModeEnabled = false
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Fictions")
                        }
                    }
                )
            } else {
                // add fiction
                TopAppBar(
                    title = { Text("INFIC Writer") },
                    actions = {
                        IconButton(onClick = { addFictionDialog = true }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Fiction")
                        }
                        // TODO: (Optional) add search bar
                        // TODO: (Optional) add fiction filter (e.g. by name, by author)
                    }
                )
            }
        }
    ) {
        MultiSelectionList(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            state = multiSelectionState,
            items = fictions,
            selectedItems = selectedItems,
            itemContent = { fiction ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    FictionCard(
                        fiction,
                        modifier = Modifier
                            .conditional(!multiSelectionState.isMultiSelectionModeEnabled) {
                                fillMaxWidth()
                            }
                            .conditional(multiSelectionState.isMultiSelectionModeEnabled) {
                                // TODO: this is static number, please change it
                                width((configuration.screenWidthDp - 50).dp)
                            }
                    )
                }
            },
            key = { fiction ->
                fictions.indexOf(fiction)
            },
            onClick = { fiction ->
                if (multiSelectionState.isMultiSelectionModeEnabled) {
                    if (fiction in selectedItems) {
                        selectedItems.remove(fiction)
                    } else {
                        selectedItems.add(fiction)
                    }
                } else {
                    // update view model with selected fiction
                    viewModel.currentFiction.value = fiction
                    // navigate back
                    selectFictionCallback()
                }
            }
        )
    }

    // add fiction dialog card
    if (addFictionDialog) {
        var title by rememberSaveable { mutableStateOf("") }
        var author by rememberSaveable { mutableStateOf("") }

        Dialog(onDismissRequest = { addFictionDialog = false }) {
            Card(modifier = Modifier.wrapContentSize()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("New Fiction")

                    // TODO: handle invalid name and empty field
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("title") },
                        maxLines = 1,
                    )
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author") },
                        maxLines = 1,
                    )

                    Row(
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Button(
                            modifier = Modifier.padding(5.dp, 0.dp),
                            onClick = { addFictionDialog = false }
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                writeFictionToJsonFile(Fiction(title, author), context)
                                fictions.clear()
                                fictions.addAll(readAllFictionsFromDir(context))
                                addFictionDialog = false
                            }
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}