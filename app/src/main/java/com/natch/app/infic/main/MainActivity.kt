package com.natch.app.infic.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natch.app.infic.graph.NodeGraph
import com.natch.app.infic.graph.graph
import com.natch.app.infic.graph.nodes
import com.natch.app.infic.importer.ImportActivity
import com.natch.app.infic.player.PlayerActivity
import com.natch.app.infic.ui.theme.InficTheme
import com.natch.app.infic.writer.WriterActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InficTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val btnModifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text("INFIC", fontSize = 64.sp)

                        NewActivityButton("Writer", context, WriterActivity::class.java, btnModifier)
                        NewActivityButton("Player", context, PlayerActivity::class.java, btnModifier)
                        NewActivityButton("Import", context, ImportActivity::class.java, btnModifier)
                    }
                }
            }
        }
    }
}

@Composable
fun NewActivityButton(btnText: String, context: Context, newActivityClass: Class<*>, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier,
        onClick = {
            context.startActivity(Intent(context, newActivityClass))
        }
    ) {
        Text(btnText)
    }
}
