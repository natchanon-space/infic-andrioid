package com.natch.app.infic.player.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natch.app.infic.model.FictionViewModel

@Composable
fun PlayTitleScreen(
    viewModel: FictionViewModel,
    onStartFiction: () -> Unit = {}
) {
    val fiction = viewModel.currentFiction.value!!

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fiction.title,
            fontSize = 32.sp,
            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
            textAlign = TextAlign.Center
        )
        Text(
            "By ${fiction.author}",
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = { onStartFiction() }) {
            Text("Play")
        }
    }
}