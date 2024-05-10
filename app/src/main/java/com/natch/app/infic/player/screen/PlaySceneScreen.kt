package com.natch.app.infic.player.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.replaceParameters
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaySceneScreen(
    viewModel: FictionViewModel,
    uuidString: String,
    onSelectChoice: (UUID) -> Unit = { _ -> },
    onGameOver: () -> Unit = { }
) {
    val currentScene = viewModel.currentFiction.value?.getSceneByUUID(UUID.fromString(uuidString))!!

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(currentScene.title) })
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // render story
            Text(
                replaceParameters(currentScene.story, viewModel.currentFiction.value!!.parameters),
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp)
            )
            // render input parameters
            if (currentScene.inputParameters.isNotEmpty()) {
                currentScene.inputParameters.forEach { key ->
                    var inputState by rememberSaveable {
                        mutableStateOf(
                            viewModel.currentFiction.value!!.parameters[key] ?: ""
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(key, modifier = Modifier.fillMaxWidth(0.2f))
                        TextField(
                            value = inputState,
                            onValueChange = {
                                inputState = it
                                viewModel.currentFiction.value!!.parameters[key] = it
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }
            }

            // render choices
            if (currentScene.choices.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    currentScene.choices.forEach { choice ->
                        Button(onClick = {
                            if (choice.nextSceneUUID != null) {
                                onSelectChoice(choice.nextSceneUUID!!)
                            } else {
                                // TODO: handle null next screen!
                            }
                        }) {
                            Text(choice.text)
                        }
                    }
                }
            }

            // case of game over
            if (currentScene.isEndingScene) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("The End")
                    Button(onClick = { onGameOver() }) {
                        Text("Start Over")
                    }
                }
            }
        }
    }
}
