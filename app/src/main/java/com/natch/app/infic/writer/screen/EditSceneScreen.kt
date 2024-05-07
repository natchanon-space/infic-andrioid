package com.natch.app.infic.writer.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.natch.app.infic.writer.component.SceneCard
import com.natch.app.infic.writer.component.rememberMultiSelectionState

@Composable
fun EditSceneScreen(
    viewModel: FictionViewModel
) {
    val context = LocalContext.current

    var addSceneDialog by rememberSaveable { mutableStateOf(false) }

    val scenes = viewModel.currentFiction.value!!.scenes

    val onFictionUpdate = {
        writeFictionToJsonFile(viewModel.currentFiction.value!!, context)
    }

    Column(modifier = Modifier
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
            IconButton(onClick = { addSceneDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Icon")
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = true
        ) {
            items(
                scenes,
                key = { scene -> scene.uuid.toString() }
            ) { scene ->
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // TODO: implement onClick here (nav to edit scene uuid)
                    }) {
                    SceneCard(scene, modifier = Modifier.fillMaxWidth())
                }
            }
        }
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