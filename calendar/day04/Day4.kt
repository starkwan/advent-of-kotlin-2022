package day04

import Day
import Lines

class Day4 : Day() {
    override fun part1(input: Lines): Any {
        return input.map { it.split("[,-]".toRegex()).map(String::toInt) }
            .count { (a, b, x, y) -> (a <= x && y <= b) || (x <= a && b <= y) }
    }

    override fun part2(input: Lines): Any {
        return input.map { it.split("[,-]".toRegex()).map(String::toInt) }
            .filterNot { (a, b, x, y) -> a > y || x > b }
            .count()
    }
}