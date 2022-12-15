package day15

import Day
import Lines
import kotlin.math.abs

class Day15 : Day() {
    override fun part1(input: Lines): Any {
        val targetY = 2000000
        val sensorsAtTargetY = mutableSetOf<Int>()
        val beaconsAtTargetY = mutableSetOf<Int>()
        val infeasibleAtTargetY = mutableSetOf<Int>()
        val regex = """Sensor at x=([-]?\d+), y=([-]?\d+): closest beacon is at x=([-]?\d+), y=([-]?\d+)""".toRegex()
        for (line in input) {
            regex.findAll(line)
                .map { result -> result.groups.drop(1).map { it!!.value.toInt() } }
                .forEach { (sx, sy, bx, by) ->
                    if (sy == targetY) sensorsAtTargetY.add(sx)
                    if (by == targetY) beaconsAtTargetY.add(bx)
                    val mDistance = abs(sy - by) + abs(sx - bx)
                    val mDistanceAtTargetY = mDistance - abs(sy - targetY)
                    if (mDistanceAtTargetY >= 0) {
                        for (step in 0..mDistanceAtTargetY) {
                            infeasibleAtTargetY.add(sx - step)
                            infeasibleAtTargetY.add(sx + step)
                        }
                    }
                }
        }
        infeasibleAtTargetY.removeAll(sensorsAtTargetY + beaconsAtTargetY)
        return infeasibleAtTargetY.size
    }

    override fun part2(input: Lines): Any {
        val env = Environment(input)
        return env.findBeacon().let { it.x.toLong() * 4000000 + it.y.toLong() }
    }

    class Environment(input: Lines) {
        private val sensors = mutableListOf<Cell>()
        private val regex =
            """Sensor at x=([-]?\d+), y=([-]?\d+): closest beacon is at x=([-]?\d+), y=([-]?\d+)""".toRegex()
        private val min = 0
        private val max = 4000000

        init {
            for (line in input) {
                regex.findAll(line)
                    .map { result -> result.groups.drop(1).map { it!!.value.toInt() } }
                    .forEach { (sx, sy, bx, by) ->
                        val mDistance = abs(sy - by) + abs(sx - bx)
                        sensors.add(Cell(sx, sy, mDistance))
                    }
            }
        }

        fun findBeacon(): Cell {
            for (y in 0..max) {
                findBeaconAtY(y)?.let { return it }
            }
            error("failed to find")
        }

        private fun getCellAt(x: Int, y: Int): Cell {
            val step = sensors.map { sensor ->
                val mDistanceTraveled = abs(x - sensor.x) + abs(y - sensor.y)
                sensor.steps - mDistanceTraveled // remaining steps at x,y
            }.max()
            return Cell(x, y, maxOf(-1, step))
        }

        private fun findBeaconAtY(y: Int): Cell? {
            var cell = getCellAt(min, y)
            while (cell.x <= max) {
                if (cell.steps == -1) {
                    return cell
                }
                cell = getCellAt(cell.x + cell.steps + 1, y)
            }
            return null
        }
    }

    /**
     * @param steps guaranteed no beacon can be found within number of steps(mDistance)
     */
    data class Cell(val x: Int, val y: Int, val steps: Int)
}