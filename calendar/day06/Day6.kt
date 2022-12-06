package day06

import Day
import Lines

class Day6 : Day() {
    override fun part1(input: Lines): Any {
        val buffer = mutableListOf(' ', ' ', ' ', ' ')
        input.single().toList().forEachIndexed { i, char ->
            buffer[i % buffer.size] = char
            if (i >= buffer.size - 1 && buffer.toSet().size == buffer.size) {
                return i + 1
            }
        }
        error("Not found")
    }

    override fun part2(input: Lines): Any {
        val buffer = mutableListOf<Char>().apply { repeat(14) { this.add(' ') } }
        input.single().toList().forEachIndexed { i, char ->
            buffer[i % buffer.size] = char
            if (i >= buffer.size - 1 && buffer.toSet().size == buffer.size) {
                return i + 1
            }
        }
        error("Not found")
    }
}