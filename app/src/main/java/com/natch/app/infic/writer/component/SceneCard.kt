package com.natch.app.infic.writer.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.natch.app.infic.model.Scene

@Composable
fun SceneCard(
    scene: Scene,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier,
    ) {
        Column {
            Text(
                scene.title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(10.dp)
            )
            if (scene.isEndingScene) {
                Text(
                    "Ending Scene",
                    modifier = Modifier
                        .padding(10.dp, 0.dp, 10.dp, 10.dp)
                )
            }
            if (scene.isFirstScene) {
                Text(
                    "First Scene",
                    modifier = Modifier
                        .padding(10.dp, 0.dp, 10.dp, 10.dp)
                )
            }
            if (scene.choices.isNotEmpty()) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        "Choices",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                    scene.choices.forEach { choice ->
                        Text(choice.text, modifier = Modifier.padding(10.dp, 0.dp))
                    }
                }
            }
        }
    }
}
