package day01

import Day
import Lines

class Day1 : Day() {
    override fun part1(input: Lines): Any {
        return input.joinToString("\n") // rawText
            .split("\n\n")
            .map { it.split("\n").sumOf { it.toInt() } }
            .max()
    }

    override fun part2(input: Lines): Any {
        return input.joinToString("\n") // rawText
            .split("\n\n")
            .map { it.split("\n").sumOf { it.toInt() } }
            .sortedDescending()
            .take(3)
            .sum()
    }
}