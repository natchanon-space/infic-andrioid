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
import com.natch.app.infic.model.Scene
import com.natch.app.infic.utils.writeFictionToJsonFile
import com.natch.app.infic.writer.component.MultiSelectionList
import com.natch.app.infic.writer.component.SceneCard
import com.natch.app.infic.writer.component.rememberMultiSelectionState
import java.util.UUID

@Composable
fun EditSceneScreen(
    viewModel: FictionViewModel,
    selectSceneCallback: (UUID) -> Unit = { _ -> }
) {
    val context = LocalContext.current

    var addSceneDialog by rememberSaveable { mutableStateOf(false) }

    val scenes = viewModel.currentFiction.value!!.scenes

    val selectedItems = remember { mutableStateListOf<Scene>() }
    val multiSelectionState = rememberMultiSelectionState()

    val onFictionUpdate = {
        writeFictionToJsonFile(viewModel.currentFiction.value!!, context)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Fiction Scenes")
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            if (!multiSelectionState.isMultiSelectionModeEnabled) {
                IconButton(onClick = { addSceneDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Icon")
                }
            } else {
                IconButton(onClick = {
                    multiSelectionState.isMultiSelectionModeEnabled = false
                    selectedItems.clear()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Cancel Button")
                }
                IconButton(onClick = {
                    scenes.removeAll(selectedItems)
                    selectedItems.clear()
                    multiSelectionState.isMultiSelectionModeEnabled = false
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Button")
                }
            }
        }
        MultiSelectionList(
            state = multiSelectionState,
            items = scenes,
            selectedItems = selectedItems,
            itemContent = { scene ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    SceneCard(scene, modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp))
                }
            },
            onClick = { scene ->
                if (multiSelectionState.isMultiSelectionModeEnabled) {
                    if (scene in selectedItems) {
                        selectedItems.remove(scene)
                    } else {
                        selectedItems.add(scene)
                    }
                } else {
                    selectSceneCallback(scene.uuid!!)
                }
            }
        )
    }

    if (addSceneDialog) {
        var sceneTitle by rememberSaveable { mutableStateOf("") }

        Dialog(onDismissRequest = { addSceneDialog = false }) {
            Card(modifier = Modifier.wrapContentSize()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Add Scene")

                    OutlinedTextField(
                        value = sceneTitle,
                        onValueChange = { sceneTitle = it },
                        label = { Text("title") },
                        maxLines = 1,
                    )

                    Row(
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Button(
                            modifier = Modifier.padding(5.dp, 0.dp),
                            onClick = { addSceneDialog = false }
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                viewModel.currentFiction.value!!.scenes.add(Scene(sceneTitle))
                                onFictionUpdate()
                                addSceneDialog = false
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