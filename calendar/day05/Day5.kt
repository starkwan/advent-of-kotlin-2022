package day05

import Day
import Lines

class Day5 : Day() {
    override fun part1(input: Lines): Any {
        val crates = mutableMapOf<Int, ArrayDeque<Char>>()
        lateinit var columns: List<Int>
        input.forEach { line ->
            if (line.contains("[")) {
                line.chunked(4).forEachIndexed { i, crate ->
                    val crateText = crate.trim()
                    if (crateText.isNotEmpty()) {
                        val stack = crates.getOrPut(i + 1) { ArrayDeque() }
                        stack.addFirst(crateText[1])
                    }
                }
            } else if (line.trimStart().startsWith("1")) {
                columns = line.trim().split("\\s+".toRegex()).map(String::toInt)
            } else if (line.contains("move")) {
                val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
                val (times, from, to) = regex.matchEntire(line)!!.groupValues.takeLast(3).map(String::toInt)
                repeat(times) {
                    crates[to]!!.add(crates[from]!!.removeLast())
                }
            }
        }
        return columns.map { crates[it]!!.removeLast() }.joinToString("")
    }

    override fun part2(input: Lines): Any {
        val crates = mutableMapOf<Int, ArrayDeque<Char>>()
        lateinit var columns: List<Int>
        input.forEach { line ->
            if (line.contains("[")) {
                line.chunked(4).forEachIndexed { i, crate ->
                    val crateText = crate.trim()
                    if (crateText.isNotEmpty()) {
                        val stack = crates.getOrPut(i + 1) { ArrayDeque() }
                        stack.addFirst(crateText[1])
                    }
                }
            } else if (line.trimStart().startsWith("1")) {
                columns = line.trim().split("\\s+".toRegex()).map(String::toInt)
            } else if (line.contains("move")) {
                val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
                val (numberOfCrates, from, to) = regex.matchEntire(line)!!.groupValues.takeLast(3).map(String::toInt)
                val movingCrates = mutableListOf<Char>()
                repeat(numberOfCrates) {
                    movingCrates.add(0, crates[from]!!.removeLast())
                }
                crates[to]!!.addAll(movingCrates)
            }
        }
        return columns.map { crates.get(it)!!.removeLast() }.joinToString("")
    }
}