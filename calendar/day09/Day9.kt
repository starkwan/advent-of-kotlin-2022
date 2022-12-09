package day09

import Day
import Lines
import kotlin.math.abs
import kotlin.math.sign

class Day9 : Day() {
    override fun part1(input: Lines): Any {
        val tailVisitedSet = mutableSetOf<Knot>()
        var rope = Rope(Knot(0, 0), Knot(0, 0))
        input.forEach {
            val (action, times) = it.split(" ")
            repeat(times.toInt()) {
                rope = rope.move(action)
                tailVisitedSet.add(rope.tail)
            }
        }
        return tailVisitedSet.size
    }

    override fun part2(input: Lines): Any {
        val tailVisitedSet = mutableSetOf<Knot>()
        var longRope = LongRope(List(10) { Knot(0, 0) })
        input.forEach {
            val (action, times) = it.split(" ")
            repeat(times.toInt()) {
                longRope = longRope.move(action)
                tailVisitedSet.add(longRope.knots.last())
            }
        }
        return tailVisitedSet.size
    }

    class Rope(val head: Knot, val tail: Knot) {
        fun move(action: String): Rope {
            val newHead = moveHead(action)
            return Rope(newHead, moveTailPart1(newHead))
        }

        fun move(newHead: Knot): Rope {
            return Rope(newHead, moveTailPart2(newHead))
        }

        private fun moveHead(action: String): Knot {
            return when (action) {
                "U" -> head.copy(y = head.y + 1)
                "D" -> head.copy(y = head.y - 1)
                "R" -> head.copy(x = head.x + 1)
                "L" -> head.copy(x = head.x - 1)
                else -> error("wrong action")
            }
        }

        private fun moveTailPart1(newHead: Knot): Knot {
            // Works for part 1 only (since no diagonal move)
            return if (abs(newHead.x - tail.x) == 2 || abs(newHead.y - tail.y) == 2) {
                head
            } else {
                tail
            }
        }

        private fun moveTailPart2(newHead: Knot): Knot {
            // Works for part 1 and part 2
            return if (abs(newHead.x - tail.x) == 2 || abs(newHead.y - tail.y) == 2) {
                Knot(tail.x + (newHead.x - tail.x).sign, tail.y + (newHead.y - tail.y).sign)
            } else {
                tail
            }
        }
    }

    class LongRope(var knots: List<Knot>) {
        fun move(action: String): LongRope {
            val ropes = knots.windowed(2, 1).map { (a, b) -> Rope(a, b) }
            val newKnots = mutableListOf<Knot>()
            ropes.forEachIndexed { i, rope ->
                if (i == 0) {
                    with(rope.move(action)) {
                        newKnots.add(this.head)
                        newKnots.add(this.tail)
                    }
                } else {
                    newKnots.add(rope.move(newKnots.last()).tail)
                }
            }
            return LongRope(newKnots.toList())
        }
    }

    data class Knot(val x: Int, val y: Int)
}