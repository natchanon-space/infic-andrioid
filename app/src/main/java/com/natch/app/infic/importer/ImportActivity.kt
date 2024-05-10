package com.natch.app.infic.importer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NavUtils
import com.natch.app.infic.ui.theme.InficTheme
import com.natch.app.infic.utils.jsonToFiction
import com.natch.app.infic.utils.writeFictionToJsonFile

@OptIn(ExperimentalMaterial3Api::class)
class ImportActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InficTheme {

                var jsonString by rememberSaveable { mutableStateOf("") }

                val clipboardManager: ClipboardManager = LocalClipboardManager.current
                val context = LocalContext.current

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = { TopAppBar(title = { Text("INFIC") })}
                    ) {paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedTextField(
                                value = jsonString,
                                onValueChange = { jsonString = it },
                                label = { Text("Json String") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.7f)
                            )
                            Button(onClick = {
                                try {
                                    val fiction = jsonToFiction(jsonString)
                                    writeFictionToJsonFile(fiction, context)
                                    Toast.makeText(context, "Imported ${fiction.title}", Toast.LENGTH_LONG).show()
                                } catch (exception: Exception) {
                                    exception.printStackTrace()
                                    Toast.makeText(context, "Unable to import, please check your file format", Toast.LENGTH_LONG).show()
                                }
                            }) {
                                Text("Import")
                            }
                        }
                    }
                }
            }
        }
    }
}