package com.natch.app.infic.writer.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.natch.app.infic.model.Fiction
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.utils.deleteFictionFromDir
import com.natch.app.infic.utils.fictionToJson
import com.natch.app.infic.utils.writeFictionToJsonFile

@Composable
fun EditProfileScreen(
    viewModel: FictionViewModel,
    onUpdateCallback: () -> Unit = { },
) {
    var title by rememberSaveable { mutableStateOf(viewModel.currentFiction.value!!.title) }
    val previousTitle by rememberSaveable { mutableStateOf(viewModel.currentFiction.value!!.title) }
    var author by rememberSaveable { mutableStateOf(viewModel.currentFiction.value!!.author) }
    val previousAuthor by rememberSaveable { mutableStateOf(viewModel.currentFiction.value!!.author) }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Text("Fiction Profile")

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("title" )},
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp)
        )
        TextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("author" )},
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            // update button
            Button(onClick = {
                // delete previous fiction
                deleteFictionFromDir(Fiction(previousTitle, previousAuthor), context)
                // update values by creating new file
                viewModel.currentFiction.value!!.title = title
                viewModel.currentFiction.value!!.author = author
                writeFictionToJsonFile(viewModel.currentFiction.value!!, context)
                // callback
                onUpdateCallback()
            }) {
                Text("Update")
            }

            // export button
            // TODO: (Optional) move `export button` to TopAppBar
            Button(onClick = {
                clipboardManager.setText(AnnotatedString(fictionToJson(viewModel.currentFiction.value!!)))
                Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
            }) {
                Text("Export")
            }
        }
    }

    // TODO: Add dialog card before update is applied
}