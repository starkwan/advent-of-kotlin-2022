package day02

import Day
import Lines

class Day2 : Day() {
    override fun part1(input: Lines): Any {
        return input.map { Round.fromPart1(it).toScore() }.sum()
    }

    override fun part2(input: Lines): Any {
        return input.map { Round.fromPart2(it).toScore() }.sum()
    }

    sealed class Shape {
        object Rock : Shape()
        object Paper : Shape()
        object Scissors : Shape()

        fun toScore() = when (this) {
            Rock -> 1
            Paper -> 2
            Scissors -> 3
        }

        fun winDrawLose(opponent: Shape) = when (toScore() - opponent.toScore()) {
            1, -2 -> 1 // win
            0 -> 0 // draw
            else -> -1 // lose
        }

        companion object {
            fun from(code: String): Shape {
                return when (code) {
                    "A", "X" -> Rock
                    "B", "Y" -> Paper
                    "C", "Z" -> Scissors
                    else -> error("incorrect code")
                }
            }

            private val testShapes = listOf(Rock, Paper, Scissors)

            fun getMyShapeFromResult(opponent: Shape, result: String): Shape {
                val expectedResult = when (result) {
                    "Z" -> 1
                    "Y" -> 0
                    "X" -> -1
                    else -> error("incorrect code")
                }
                return testShapes.stream().filter { it.winDrawLose(opponent) == expectedResult }.findFirst().get()
            }
        }
    }

    class Round(val opponentShape: Shape, val myShape: Shape) {

        companion object {
            fun fromPart1(line: String): Round {
                line.split(" ").apply {
                    val opponentShape = Shape.from(this[0])
                    val myShape = Shape.from(this[1])
                    return Round(opponentShape, myShape)
                }
            }

            fun fromPart2(line: String): Round {
                line.split(" ").apply {
                    val opponent = Shape.from(this[0])
                    val myShape = Shape.getMyShapeFromResult(opponent, this[1])
                    return Round(opponent, myShape)
                }
            }
        }

        fun toScore(): Int {
            val roundScore = when (myShape.winDrawLose(opponentShape)) {
                1 -> 6
                0 -> 3
                else -> 0
            }
            return roundScore + myShape.toScore()
        }
    }
}