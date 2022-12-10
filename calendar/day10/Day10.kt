package day10

import Day
import Lines

class Day10 : Day() {
    override fun part1(input: Lines): Any {
        val cpu = Cpu()
        return input.flatMap { cpu.process(it) }
            .filter { it.cycle % 40 == 20 && it.cycle <= 220 }
            .sumOf { it.signalStrength }
    }

    override fun part2(input: Lines): Any {
        val cpu = Cpu()
        input.flatMap { cpu.process(it) }
            .windowed(40, 40, true)
            .forEach {
                println(it.mapIndexed { pos, cycleX -> cycleX.getPixel(pos) }.joinToString(""))
            }
        return 0
    }

    class Cpu {
        private var cycle = 0
        private var x = 1

        fun process(line: String): List<CycleX> {
            if (line == "noop") {
                return listOf(CycleX(++cycle, x))
            }
            val addX = line.split(" ").last().toInt()
            return listOf(CycleX(++cycle, x), CycleX(++cycle, x)).also { x += addX }
        }

        data class CycleX(val cycle: Int, val x: Int) {
            val signalStrength = cycle * x

            fun getPixel(pixelPos: Int): String {
                return if (pixelPos in x - 1..x + 1) "#" else "."
            }
        }
    }
}