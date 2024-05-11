package com.natch.app.infic.writer.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.natch.app.infic.graph.Edge
import com.natch.app.infic.graph.Graph
import com.natch.app.infic.graph.GraphOptions
import com.natch.app.infic.graph.NodeGraph
import com.natch.app.infic.model.Fiction
import com.natch.app.infic.model.FictionViewModel

@Composable
fun EditTreeView(viewModel: FictionViewModel) {
    val graph = fictionToGraph(viewModel.currentFiction.value!!, graphOption = GraphOptions(100, 100))
    val scenes = viewModel.currentFiction.value!!.scenes.toList()

    NodeGraph(
        graph = graph,
        modifier = Modifier
            .fillMaxSize()
    ) {
        scenes.forEach { scene ->
            Text(
                scene.title,
                modifier = Modifier
                    .background(Color.LightGray)
                    .border(width = 1.dp, color = Color.Black)
                    .padding(4.dp)
                    .layoutId(scene.title)
            )
        }
    }

//    NodeGraph(
//        graph = com.natch.app.infic.graph.graph,
//        modifier = Modifier.size(500.dp)
//    ) {
//        nodes.forEach {
//            Text(
//                it, modifier = Modifier
//                    .background(Color.LightGray)
//                    .border(width = 1.dp, color = Color.Black)
//                    .padding(4.dp)
//                    .layoutId(it)
//            )
//        }
//    }
}

private fun fictionToGraph(fiction: Fiction, graphOption: GraphOptions): Graph<String> {
    val edges = mutableListOf<Edge<String>>()
    // add all edges
    fiction.scenes.forEach { scene ->
        scene.choices.forEach { choice ->
            val nextScene = fiction.getSceneByUUID(choice.nextSceneUUID)
            nextScene?.let {
                edges.add(Edge(scene.title, nextScene.title))
            }
        }
    }
    // filter out bidirectional edges
    val edgesToRemove = mutableListOf<Edge<String>>()
    for (i in 0 until edges.size-1) {
        for (j in i+1 until edges.size) {
            val edge1 = edges[i]
            val edge2 = edges[j]
            if (edge1.start == edge2.end && edge1.end == edge2.start) {
                edgesToRemove.add(edge2)
                edges[i].bidirectional = true
            }
        }
    }
    edges.removeAll(edgesToRemove)
    Log.d("DEBUG", edges.size.toString())
    return Graph(edges.toList(), graphOption)
}