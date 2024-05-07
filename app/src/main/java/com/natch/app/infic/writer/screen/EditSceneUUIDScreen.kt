package com.natch.app.infic.writer.screen

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.writeFictionToJsonFile
import com.natch.app.infic.writer.component.DropDownSearch
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun EditSceneUUIDScreen(
    viewModel: FictionViewModel,
    uuidString: String,
    saveCallback: () -> Unit = { }
) {
    val context = LocalContext.current

    val currentScene = viewModel.currentFiction.value!!.getSceneByUUID(UUID.fromString(uuidString))

    var title by rememberSaveable { mutableStateOf(currentScene.title) }
    var story by rememberSaveable { mutableStateOf(currentScene.story) }
    val choices by rememberSaveable { mutableStateOf(currentScene.choices) }
    val inputParameters = remember { mutableStateListOf<String>() }
    inputParameters.clear()
    inputParameters.addAll(currentScene.inputParameters)
    var isEndingScene by rememberSaveable { mutableStateOf(currentScene.isEndingScene) }
    var isFirstScene by rememberSaveable { mutableStateOf(currentScene.isFirstScene) }

    var enableInputs by rememberSaveable { mutableStateOf(inputParameters.size > 0) }

    val lazyState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val onFictionUpdate = {
        writeFictionToJsonFile(viewModel.currentFiction.value!!, context)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = -available.y
                coroutineScope.launch {
                    if (scrollState.isScrollInProgress.not()) {
                        scrollState.scrollBy(delta)
                    }
                }
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .nestedScroll(nestedScrollConnection)
            .verticalScroll(scrollState)
    ) {
        Text("Scene Editor")

        // scene title
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // first scene settings
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isFirstScene,
                onCheckedChange = {
                    isFirstScene = !isFirstScene
                }
            )
            Text("First Scene")
        }

        // ending scene settings
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isEndingScene,
                onCheckedChange = {
                    isEndingScene = !isEndingScene
                }
            )
            Text("Ending Scene")
        }

        // story
        TextField(
            value = story,
            onValueChange = { story = it },
            label = { Text("story") },
            modifier = Modifier.fillMaxWidth()
        )

        // input parameters
        Column(
            modifier = Modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = enableInputs,
                    onCheckedChange = {
                        enableInputs = !enableInputs

                        // remove selected option when unable section
                        if (!enableInputs) {
                            inputParameters.clear()
                        }
                    },
                )
                Text("Enable Input Parameters")
            }
            if (enableInputs) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Input Parameters")
                    IconButton(onClick = { inputParameters.add("") }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Parameter Icon")
                    }
                }
                // list of input parameters
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier.heightIn(max = 1000.dp)
                ) {
                    itemsIndexed(
                        inputParameters,
                        key = { index, _ -> "item-${index}"}
                    ) { index, item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DropDownSearch(
                                items = viewModel.currentFiction.value!!.parameters.keys.toList(),
                                mapper = { s -> s },
                                defaultSelectedItem = item,
                                onSelectedCallBack = {
                                    inputParameters[index] = it
                                }
                            )
                            IconButton(onClick = {
                                inputParameters.removeAt(index)
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Option")
                            }
                        }
                    }
                }
            }
        }

        // choices

        // save button
        Button(onClick = {
            viewModel.currentFiction.value!!.updateScene(
                currentScene,
                title,
                story,
                choices,
                inputParameters.toMutableList(),
                isEndingScene,
                isFirstScene
            )
            onFictionUpdate()
            saveCallback()
        }) {
            Text("Save")
        }
    }
}