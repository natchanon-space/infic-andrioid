package com.natch.app.infic.writer.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.natch.app.infic.model.Fiction
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.conditional
import com.natch.app.infic.writer.component.FictionCard
import com.natch.app.infic.writer.component.MultiSelectionList
import com.natch.app.infic.writer.component.rememberMultiSelectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFictionScreen(viewModel: FictionViewModel) {

    // val fictions = (1..100).toMutableList()
    val fictions = remember { mutableStateListOf(Fiction("A", "Auth-A"), Fiction("B", "Auth-B")) }
    val selectedItems = remember { mutableStateListOf<Fiction>() }
    val multiSelectionState = rememberMultiSelectionState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

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
                            for (item in selectedItems) {
                                fictions.remove(item)
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
            items = fictions,
            selectedItems = selectedItems,
            itemContent = {fiction ->
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
            onClick = {
                if (multiSelectionState.isMultiSelectionModeEnabled) {
                    selectedItems.add(it)
                } else {
                    // TODO: go to edit screen
                    Toast.makeText(context, "Click ${it.title}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}