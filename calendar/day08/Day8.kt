package day08

import Day
import Lines

class Day8 : Day() {

    override fun part1(input: Lines): Any {
        val rows: List<List<Int>> = input.map { it.toList().map { it.toString().toInt() } }
        val columns: List<List<Int>> = buildList {
            for (i in 0 until rows[0].size) {
                add(buildList { rows.forEach { add(it[i]) } })
            }
        }

        var visibleCount = 0
        rows.forEachIndexed { rIndex, row ->
            row.forEachIndexed { cIndex, _ ->
                val fromLeft = rows[rIndex].take(cIndex + 1)
                val fromRight = rows[rIndex].reversed().take(row.size - cIndex)
                val fromTop = columns[cIndex].take(rIndex + 1)
                val fromBottom = columns[cIndex].reversed().take(columns.size - rIndex)
                if (isVisible(fromLeft) || isVisible(fromRight) || isVisible(fromTop) || isVisible(fromBottom)) {
                    visibleCount++
                }
            }
        }
        return visibleCount
    }

    private fun isVisible(trees: List<Int>): Boolean {
        return trees.max() == trees.last() && !trees.dropLast(1).contains(trees.last())
    }

    override fun part2(input: Lines): Any {
        val rows: List<List<Int>> = input.map { it.toList().map { it.toString().toInt() } }
        val columns: List<List<Int>> = buildList {
            for (i in 0 until rows[0].size) {
                add(buildList { rows.forEach { add(it[i]) } })
            }
        }

        var highestScore = 0
        rows.forEachIndexed { rIndex, row ->
            row.forEachIndexed { cIndex, self ->
                val toLeft = rows[rIndex].take(cIndex).reversed()
                val toRight = rows[rIndex].takeLast(columns.size - cIndex - 1)
                val toTop = columns[cIndex].take(rIndex).reversed()
                val toBottom = columns[cIndex].takeLast(rows.size - rIndex - 1)
                val score =
                    toLeft.getScore(self) * toRight.getScore(self) * toTop.getScore(self) * toBottom.getScore(self)
                highestScore = maxOf(highestScore, score)
            }
        }
        return highestScore
    }

    private fun List<Int>.getScore(self: Int): Int {
        val numOfVisibleTrees = this.indexOfFirst { it >= self } + 1
        return if (numOfVisibleTrees == 0) {
            this.size
        } else {
            numOfVisibleTrees
        }
    }
}