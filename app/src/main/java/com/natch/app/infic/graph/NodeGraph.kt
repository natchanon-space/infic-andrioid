package com.natch.app.infic.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.sqrt

class Edge<T>(val start: T, val end: T)

class Graph<T>(val edges: List<Edge<T>>, options: GraphOptions = GraphOptions()) {
    private val nodePositions: MutableMap<T, IntRect> = mutableMapOf()
    private val _positions = MutableStateFlow<Map<T, IntOffset>>(emptyMap())
    var positions: StateFlow<Map<T, IntOffset>> = _positions

    private val horizontalPadding = options.horizontalPadding
    private val verticalPadding = options.verticalPadding

    private var needLayout = true
    fun placeNodes(measured: Map<T, Placeable>) {

        if (!needLayout) {
            return
        } else {
            needLayout = false
        }

        val layers = splitIntoLayers(measured.keys)

        val widestLayer = layers.map { layerWidth(it, measured) }.max()

        var y = 30
        layers.forEach { layer ->

            val layerWidth = layerWidth(layer, measured)

            var x = 30 + (widestLayer - layerWidth) / 2
            layer.forEach { node ->
                measured[node]?.let { item ->
                    nodePositions[node] =
                        IntRect(IntOffset(x, y), IntOffset(x + item.width, y + item.height))
                    x += item.width + horizontalPadding
                }
            }
            y += verticalPadding + layer.map { measured[it]?.height ?: 0 }.max()
        }
        _positions.value = nodePositions.mapValues { it.value.topLeft }
    }

    private fun layerWidth(layer: List<T>, measured: Map<T, Placeable>): Int {
        return layer.map { measured[it]?.width ?: 0 }.sum() + (layer.size - 1) * horizontalPadding
    }

    private fun splitIntoLayers(nodes: Set<T>): List<List<T>> {
        val collectedLayers = mutableListOf<List<T>>()
        var currentLayer = nodes.toMutableSet()
        do {
            val nextLayer = edges
                .filter { currentLayer.contains(it.end) }
                .map { it.start }
                .toSet()

            // TODO: handle cycle in graph!

            currentLayer.removeAll(nextLayer)
            collectedLayers.add(currentLayer.toList())
            currentLayer = nextLayer.toMutableSet()
        } while (nextLayer.isNotEmpty())

        return collectedLayers
    }

    fun arrows(): List<Arrow> {
        val arrows = mutableListOf<Arrow>()
        edges.forEach { edge ->
            nodePositions[edge.start]?.let { startRect ->
                nodePositions[edge.end]?.let { endRect ->
                    val end = intersectRectangle(startRect, endRect.center.toOffset())
                    val start = intersectRectangle(endRect, startRect.center.toOffset())

                    arrows.add(Arrow(start, end))
                }
            }
        }
        return arrows
    }

    private var dragNode: T? = null
    fun dragStart(position: Offset) {
        dragNode =
            nodePositions.filter { it.value.contains(position.toIntOffSet()) }.keys.firstOrNull()
    }

    fun onDrag(dragAmount: Offset) {
        dragNode?.let { node ->
            nodePositions[node]?.let { r ->
                nodePositions[node] = r.translate(dragAmount.toIntOffSet())
                _positions.value = nodePositions.mapValues { v -> v.value.topLeft }
            }
        }
    }
}

class GraphOptions(val horizontalPadding: Int = 30, val verticalPadding: Int = 100)

private fun Offset.toIntOffSet(): IntOffset = IntOffset(x.toInt(), y.toInt())

data class Arrow(val start: Offset, val end: Offset)

@Composable
fun <T> NodeGraph(graph: Graph<T>, modifier: Modifier = Modifier, content: @Composable () -> Unit) {

    val placed = graph.positions.collectAsState().value

    Layout(content, modifier
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                graph
                    .arrows()
                    .forEach { drawArrow(Color.Blue, it.start, it.end) }
            }
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { position -> graph.dragStart(position) }
            ) { _, dragAmount ->
                graph.onDrag(dragAmount)
            }
        }
    ) { modifiable, constraints ->
        val measured = modifiable.associate {
            it.layoutId as T to it.measure(Constraints())
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            graph.placeNodes(measured)

            placed.forEach {
                measured[it.key]?.placeRelative(it.value)
            }
        }
    }
}

private fun DrawScope.drawArrow(color: Color, start: Offset, end: Offset) {
    val headStart = moveAlongLine(end, start, 10f)
    drawLine(color, start, end)
    rotate(20f, end) {
        drawLine(color, headStart, end)
    }
    rotate(-20f, end) {
        drawLine(color, headStart, end)
    }
}

fun intersectRectangle(startRect: IntRect, end: Offset): Offset {
    val start = startRect.center.toOffset()

    val dy = end.y - start.y
    val dx = end.x - start.x
    val m = dy / dx

    var startY = if (start.y > end.y) {
        startRect.top.toFloat()
    } else {
        startRect.bottom.toFloat()
    }

    var startX = (startY - start.y) / m + start.x

    if (startX < startRect.left) {
        startX = startRect.left.toFloat()
        startY = (startX - start.x) * m + start.y
    }

    if (startX > startRect.right) {
        startX = startRect.right.toFloat()
        startY = (startX - start.x) * m + start.y
    }

    return Offset(startX, startY)
}

fun moveAlongLine(p0: Offset, p1: Offset, distance: Float): Offset {
    val dx = p1.x - p0.x
    val dy = p1.y - p0.y
    val length = sqrt(dx * dx + dy * dy)
    if (length <= 0) {
        return p0
    }
    val scale = distance / length
    return Offset((dx * scale) + p0.x, (dy * scale) + p0.y)
}

val nodes = listOf("Hello", "Kotlin", "New", "World")

val edges = listOf(
    Edge(nodes[1], nodes[0]),
    Edge(nodes[2], nodes[0]),
    Edge(nodes[3], nodes[1]),
    Edge(nodes[3], nodes[2])
)

val graph = Graph(edges)

@Composable
@Preview(showBackground = false)
fun NodeGraphPreview() {
    NodeGraph(
        graph = graph,
        modifier = Modifier.size(500.dp)
    ) {
        nodes.forEach {
            Text(
                it, modifier = Modifier
                    .background(Color.LightGray)
                    .border(width = 1.dp, color = Color.Black)
                    .padding(4.dp)
                    .layoutId(it)
            )
        }
    }
}