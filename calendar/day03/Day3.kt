package day03

import Day
import Lines

class Day3 : Day() {
    override fun part1(input: Lines): Any {
        return input
            .map { rucksack ->
                rucksack.chunked(rucksack.length / 2)
                    .map { it.toSet() }
                    .let { it[0].intersect(it[1]).iterator().next() }
            }
            .sumOf { if (it >= 'a') it - 'a' + 1 else it - 'A' + 27 }
    }

    override fun part2(input: Lines): Any {
        return input
            .chunked(3)
            .map { rucksacks ->
                rucksacks.map { it.toSet() }
                    .reduce { acc, rucksack -> acc.intersect(rucksack) }
            }
            .flatten()
            .sumOf { if (it >= 'a') it - 'a' + 1 else it - 'A' + 27 }
    }
}