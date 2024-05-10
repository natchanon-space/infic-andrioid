package com.natch.app.infic.player.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.readAllFictionsFromDir
import com.natch.app.infic.writer.component.FictionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaySelectionScreen(
    viewModel: FictionViewModel,
    onSelectFiction: () -> Unit = { }
) {
    val context = LocalContext.current

    val fictions = readAllFictionsFromDir(context)

    Scaffold(
        topBar = { TopAppBar(title = { Text("INFIC Player") }) }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            userScrollEnabled = true
        ) {
            itemsIndexed(
                fictions,
                key = { index, _ -> "item-${index}" }
            ) { _, fiction ->
                FictionCard(
                    fiction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.currentFiction.value = fiction
                            onSelectFiction()
                        }
                )
            }
        }
    }
}