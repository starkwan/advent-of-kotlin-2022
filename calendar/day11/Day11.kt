package day11

import Day
import Lines

class Day11 : Day() {
    override fun part1(input: Lines): Any {
        val monkeys = input.windowed(6, 7).map { Monkey(it) }
        for (round in 1..20) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    item.worryLevel = monkey.operation(item.worryLevel)
                    item.worryLevel /= 3
                    monkeys[monkey.test(item.worryLevel)].items.add(item)
                }
                monkey.inspectedCount += monkey.items.size
                monkey.items.clear()
            }
        }
        return monkeys.map { it.inspectedCount }.sorted().takeLast(2).let { it[0] * it[1] }
    }

    override fun part2(input: Lines): Any {
        val monkeys = input.windowed(6, 7).map { MonkeyPart2(it) }
        ItemPart2.divisors = monkeys.map { it.divisor }
        monkeys.forEach(MonkeyPart2::generateItemsPart2)

        for (round in 1..10000) {
            monkeys.forEach { monkey ->
                monkey.itemsPart2.forEach { itemPart2 ->
                    itemPart2.update { remainder -> monkey.operation(remainder) }
                    monkeys[monkey.test(itemPart2)].itemsPart2.add(itemPart2)
                }
                monkey.inspectedCount += monkey.itemsPart2.size
                monkey.itemsPart2.clear()
            }
        }
        return monkeys.map { it.inspectedCount.toLong() }.sorted().takeLast(2).let { it[0] * it[1] }
    }

    class Monkey(lines: List<String>) {
        var items: MutableList<Item>
        val operation: (old: Int) -> Int
        val test: (item: Int) -> Int
        var inspectedCount = 0

        init {
            items = lines[1].substring("  Starting items: ".length).split(", ").map { Item(it.toInt()) }.toMutableList()
            operation = {
                var parts = lines[2].substring("  Operation: new = ".length).split(" ")
                var result = if (parts[0] == "old") it else parts[1].toInt()
                parts = parts.drop(1)
                while (parts.isNotEmpty()) {
                    when (parts[0]) {
                        "+" -> result += if (parts[1] == "old") result else parts[1].toInt()
                        "*" -> result *= if (parts[1] == "old") result else parts[1].toInt()
                        else -> error("unknown operator")
                    }
                    parts = parts.drop(2)
                }
                result
            }
            test = { worryLevel ->
                val divisor = lines[3].split(" ").last().toInt()
                val monkeyIfTrue = lines[4].split(" ").last().toInt()
                val monkeyIfFalse = lines[5].split(" ").last().toInt()
                if (worryLevel % divisor == 0) monkeyIfTrue else monkeyIfFalse
            }

        }
    }

    data class Item(var worryLevel: Int)

    class MonkeyPart2(lines: List<String>) {
        private var monkeyIndex = -1
        private var initialWorryLevels: MutableList<Int> = mutableListOf()
        var itemsPart2: MutableList<ItemPart2> = mutableListOf()
        val divisor = lines[3].split(" ").last().toInt()
        val operation: (old: Int) -> Int
        val test: (itemPart2: ItemPart2) -> Int
        var inspectedCount = 0

        fun generateItemsPart2() {
            itemsPart2 = initialWorryLevels.map { ItemPart2(it) }.toMutableList()
        }

        init {
            monkeyIndex = lines[0].split(" ").last().dropLast(1).toInt()
            initialWorryLevels =
                lines[1].substring("  Starting items: ".length).split(", ").map(String::toInt).toMutableList()
            operation = { remainder ->
                var parts = lines[2].substring("  Operation: new = ".length).split(" ")
                var result = if (parts[0] == "old") remainder else parts[1].toInt()
                parts = parts.drop(1)
                while (parts.isNotEmpty()) {
                    when (parts[0]) {
                        "+" -> result += if (parts[1] == "old") result else parts[1].toInt()
                        "*" -> result *= if (parts[1] == "old") result else parts[1].toInt()
                        else -> error("unknown operator")
                    }
                    parts = parts.drop(2)
                }
                result
            }
            test = { itemPart2 ->
                val monkeyIfTrue = lines[4].split(" ").last().toInt()
                val monkeyIfFalse = lines[5].split(" ").last().toInt()
                if (itemPart2.remainders[monkeyIndex] == 0) monkeyIfTrue else monkeyIfFalse
            }

        }
    }

    class ItemPart2(private val initialValue: Int) {
        var remainders: List<Int>

        init {
            remainders = divisors.map { initialValue % it }
        }

        fun update(operation: (Int) -> Int) {
            remainders = (remainders zip divisors).map { (remainder, divisor) -> operation(remainder) % divisor }
        }

        companion object {
            var divisors = listOf<Int>()
        }
    }
}