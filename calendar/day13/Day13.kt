package day13

import Day
import Lines
import kotlin.math.sign

class Day13 : Day() {
    override fun part1(input: Lines): Any {
        return input.windowed(2, 3)
            .map { isRightOrder(parse(it[0]), parse(it[1])) >= 0 }
            .mapIndexed { i, isRightOrder -> if (isRightOrder) i + 1 else 0 }
            .sum()
    }

    override fun part2(input: Lines): Any {
        val packets = input.filter { it.isNotEmpty() }.map { parse(it) }.toMutableList()
        val divider1 = parse("[[2]]").also { packets.add(it) }
        val divider2 = parse("[[6]]").also { packets.add(it) }
        return packets.sortedWith { left, right -> isRightOrder(right, left) }
            .withIndex()
            .filter { it.value === divider1 || it.value === divider2 }
            .let { (it[0].index + 1) * (it[1].index + 1) }
    }

    private fun parse(line: String): Content {
        val rootContent = Content()
        var currentContent: Content = rootContent
        val intCache = mutableListOf<Char>()
        for (char in line.toList()) {
            if (char == '[') {
                val newContent = Content()
                newContent.parent = currentContent
                currentContent.addContent(newContent)
                currentContent = newContent
            } else if (char.isDigit()) {
                intCache.add(char)
            } else if (char == ',' || char == ']') {
                if (intCache.isNotEmpty()) {
                    val intValue = intCache.joinToString("").toInt()
                    currentContent.addContent(Content().setInt(intValue))
                    intCache.clear()
                }
                if (char == ']') {
                    if (currentContent.listValue == null) {
                        currentContent.listValue = mutableListOf()
                    }
                    currentContent = currentContent.parent!!
                }
            } else {
                error("invalid char")
            }
        }
        return rootContent.listValue!!.first()
    }

    private fun isRightOrder(left: Content, right: Content): Int {
        if (left.isInt() && right.isInt()) {
            return (right.intValue!! - left.intValue!!).sign
        } else if (!left.isInt() && right.isInt()) {
            return isRightOrder(left, Content().addContent(right))
        } else if (left.isInt() && !right.isInt()) {
            return isRightOrder(Content().addContent(left), right)
        } else {
            for (i in 0 until minOf(left.listValue!!.size, right.listValue!!.size)) {
                val isRightOrder = isRightOrder(left.listValue!![i], right.listValue!![i])
                if (isRightOrder != 0) {
                    return isRightOrder
                }
            }
            return (right.listValue!!.size - left.listValue!!.size).sign
        }
    }

    class Content {
        var parent: Content? = null
        var intValue: Int? = null
        var listValue: MutableList<Content>? = null

        fun isInt(): Boolean {
            return intValue != null
        }

        fun setInt(value: Int): Content {
            if (listValue != null) {
                error("Already set list value")
            }
            intValue = value
            return this
        }

        fun addContent(content: Content): Content {
            if (intValue != null) {
                error("Already set int value")
            }
            listValue = (listValue ?: mutableListOf()).apply { this.add(content) }
            return this
        }
    }
}