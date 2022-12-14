package day14

import Day
import Lines

class Day14 : Day() {
    override fun part1(input: Lines): Any {
        val wall = Wall(input)
        val firstSandToFlowOver = { sand: Point -> sand.y >= wall.globalMaxY }
        return wall.simulate(firstSandToFlowOver) - 1
    }

    override fun part2(input: Lines): Any {
        val wall = Wall(input)
        val firstSandToBlockStart = { sand: Point -> sand == wall.startingPoint }
        return wall.simulate(firstSandToBlockStart)
    }

    class Wall(input: Lines) {
        private val points = mutableSetOf<Point>()
        val startingPoint = Point(500, 0)
        var globalMaxY = 0
        var floorY = 0

        init {
            input.forEach {
                it.split("( -> )|,".toRegex())
                    .map(String::toInt)
                    .windowed(4, 2)
                    .forEach { (x1, y1, x2, y2) ->
                        val minX = minOf(x1, x2)
                        val maxX = maxOf(x1, x2)
                        val minY = minOf(y1, y2)
                        val maxY = maxOf(y1, y2)
                        globalMaxY = maxOf(globalMaxY, maxY)
                        when {
                            x1 == x2 -> for (y in minY..maxY) points.add(Point(x1, y))
                            y1 == y2 -> for (x in minX..maxX) points.add(Point(x, y1))
                            else -> error("diagonal rocks!")
                        }
                    }
            }
            floorY = globalMaxY + 2
        }

        fun simulate(condition: (Point) -> Boolean): Int {
            var unit = 1
            while (!dropSand(condition)) {
                unit++
            }
            return unit
        }

        private fun dropSand(condition: (Point) -> Boolean): Boolean {
            var sand = startingPoint
            var movedSand = fall(sand)
            while (
                sand != movedSand && // the sand is moved
                movedSand.y < floorY // the moved sand has not fallen over (part1: maxY+1)
            ) {
                sand = movedSand
                movedSand = fall(sand)
            }
            points.add(sand)
            return condition(sand)
        }

        private fun fall(p: Point): Point {
            val nextPoints = listOf(
                p.copy(y = p.y + 1),
                p.copy(x = p.x - 1, y = p.y + 1),
                p.copy(x = p.x + 1, y = p.y + 1)
            )
            nextPoints.forEach {
                if (
                    !points.contains(it) &&  // part 1: not having rock or sand
                    it.y < floorY // part 2: cannot overlap with the floor
                ) {
                    return it
                }
            }
            return p
        }
    }

    data class Point(val x: Int, val y: Int)
}