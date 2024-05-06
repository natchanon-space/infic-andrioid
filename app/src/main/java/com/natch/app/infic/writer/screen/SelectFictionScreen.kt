package com.natch.app.infic.writer.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.writer.component.MultiSelectionList
import com.natch.app.infic.writer.component.rememberMultiSelectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFictionScreen(viewModel: FictionViewModel) {

    val numbers = (1..100).toMutableList()
    val selectedItems = remember { mutableStateListOf<Int>() }
    val multiSelectionState = rememberMultiSelectionState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            if (multiSelectionState.isMultiSelectionModeEnabled) {
                TopAppBar(
                    title = { },
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
                            selectedItems.sort()
                            selectedItems.reverse()
                            for (item in selectedItems) {
                                numbers.remove(item)
                            }
                            selectedItems.clear()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Fictions")
                        }
                    }
                )
            } else {
                TopAppBar(title = { Text("Writer") })
            }
        }
    ) {
        MultiSelectionList(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            state = multiSelectionState,
            items = numbers,
            selectedItems = selectedItems,
            itemContent = {
                Text(
                    text = it.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            },
            key = { num ->
                num
            },
            onClick = {
                if (multiSelectionState.isMultiSelectionModeEnabled) {
                    selectedItems.add(it)
                } else {
                    Toast.makeText(context, "Click $it", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}