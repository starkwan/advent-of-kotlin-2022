package day12

import Day
import Lines
import java.util.Comparator.comparingInt

class Day12 : Day() {
    override fun part1(input: Lines): Any {
        val dijkstra = Dijkstra(
            input,
            cost = { node, other -> if (node.elevation - other.elevation >= -1) 1 else -10000 }
        )
        val processedNodes = dijkstra.findShortestPath(dijkstra.nodes.single { it.symbol == 'S' })
        return processedNodes.single { it.symbol == 'E' }.costFromStart
    }

    override fun part2(input: Lines): Any {
        // Find the shortest path starting from the end
        // Since we are going in reversed way, we provide another the cost function
        val dijkstraStartFromEnd = Dijkstra(
            input,
            cost = { node, other -> if (other.elevation - node.elevation >= -1) 1 else -10000 }
        )
        val originalEnd = dijkstraStartFromEnd.nodes.single { it.symbol == 'E' }
        val originalStart = dijkstraStartFromEnd.nodes.single { it.symbol == 'S' }
        val possibleStarts = findPossibleStartNodes(dijkstraStartFromEnd.graph, originalStart)
        val processedNodes = dijkstraStartFromEnd.findShortestPath(originalEnd)
        return processedNodes.filter { possibleStarts.contains(it) }.minOf { it.costFromStart }
    }

    private fun findPossibleStartNodes(graph: List<List<Node>>, startNode: Node): List<Node> {
        val candidates = mutableSetOf<Node>()
        val nodes = mutableSetOf<Node>()
        val visitedNotes = mutableSetOf<Node>()
        candidates.add(startNode)
        while (candidates.isNotEmpty()) {
            val candidate = candidates.first()
            candidates.remove(candidate)
            visitedNotes.add(candidate)
            if (candidate.elevation != startNode.elevation) {
                continue
            }
            // if the candidate has the same elevation as the startNode, add it to return set
            nodes.add(candidate)
            val cIndex = candidate.pos.first
            val rIndex = candidate.pos.second
            val row = graph[rIndex]

            // Also, add the valid unvisited surrounding nodes to the candidate list to process in next interation
            val rules = listOf(
                (cIndex > 0) to { row[cIndex - 1] },
                (cIndex < row.size - 1) to { row[cIndex + 1] },
                (rIndex > 0) to { graph[rIndex - 1][cIndex] },
                (rIndex < graph.size - 1) to { graph[rIndex + 1][cIndex] },
            )
            for ((condition, nodeSupplier) in rules) {
                if (condition) {
                    val node = nodeSupplier()
                    if (!visitedNotes.contains(node)) {
                        candidates.add(node)
                    }
                }
            }
        }
        return nodes.toList()
    }

    // Dijkstra's algorithm
    // https://isaaccomputerscience.org/concepts/dsa_search_dijkstra
    class Dijkstra(
        input: Lines,
        val cost: (node: Node, other: Node) -> Int
    ) {
        private var visited = mutableListOf<Node>()
        private var unvisited = mutableListOf<Node>()
        val graph: List<List<Node>>
        val nodes: List<Node>
            get() = visited + unvisited

        init {
            graph = input.map { row -> row.toList().map { Node(it) } }
            graph.forEachIndexed { rIndex, row ->
                row.forEachIndexed { cIndex, node ->
                    node.pos = cIndex to rIndex
                    val neighbours = mutableListOf<Node>()
                    if (cIndex > 0) neighbours.add(row[cIndex - 1])
                    if (cIndex < row.size - 1) neighbours.add(row[cIndex + 1])
                    if (rIndex > 0) neighbours.add(graph[rIndex - 1][cIndex])
                    if (rIndex < graph.size - 1) neighbours.add(graph[rIndex + 1][cIndex])
                    node.neighbours = neighbours.filter { cost(node, it) > 0 }
                }
            }
            unvisited = graph.flatten().toMutableList()
        }

        fun findShortestPath(startNode: Node): List<Node> {
            unvisited.single { it == startNode }.costFromStart = 0
            // Repeat until there are no more nodes in the unvisited list
            while (unvisited.isNotEmpty()) {
                // Get the unvisited node with the lowest cost
                val currentNode = unvisited.minWith(comparingInt { it.costFromStart })
                if (currentNode.costFromStart != Int.MAX_VALUE) {
                    // A node has MAX_VALUE if it is an orphan node.
                    for (neighbour in currentNode.neighbours) {
                        if (visited.contains(neighbour)) {
                            continue
                        }
                        val costFromStart = currentNode.costFromStart + cost(currentNode, neighbour)
                        // Check if the new cost is less
                        if (costFromStart < neighbour.costFromStart) {
                            // Update costFromStart and previous node reference
                            neighbour.costFromStart = costFromStart
                            neighbour.previousNode = currentNode
                        }
                    }
                }
                // Add the current node to the visited list
                visited.add(currentNode)
                unvisited.remove(currentNode)
            }
            return visited
        }
    }

    class Node(var symbol: Char) {
        var pos: Pair<Int, Int> = -1 to -1
        var previousNode: Node? = null
        var costFromStart = Int.MAX_VALUE
        var neighbours = emptyList<Node>()
        val elevation
            get() = when (symbol) {
                'S' -> 'a'
                'E' -> 'z'
                else -> symbol
            }

        override fun equals(other: Any?) = if (other is Node) pos == other.pos else false
    }
}