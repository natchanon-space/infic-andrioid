package com.natch.app.infic.player.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.readAllFictionsFromDir
import com.natch.app.infic.writer.component.FictionCard

@Composable
fun PlaySelectionScreen(
    viewModel: FictionViewModel,
    onSelectFiction: () -> Unit = { }
) {
    val context = LocalContext.current

    val fictions = readAllFictionsFromDir(context)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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