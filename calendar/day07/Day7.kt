package day07

import Day
import Lines
import day07.Day7.StateMachine.State.CD
import day07.Day7.StateMachine.State.LS

class Day7 : Day() {
    override fun part1(input: Lines): Any {
        val stateMachine = StateMachine()
        input.forEach { stateMachine.process(it) }
        return stateMachine.root.getAllRecursiveDirs()
            .filter { it.getTotalSize() <= 100000 }
            .sumOf { it.getTotalSize() }
    }

    override fun part2(input: Lines): Any {
        val diskSpace = 70000000
        val requiredSize = 30000000
        val stateMachine = StateMachine()
        input.forEach { stateMachine.process(it) }
        val unusedSpace = diskSpace - stateMachine.root.getTotalSize()
        val sizeToRemove = requiredSize - unusedSpace
        return stateMachine.root.getAllRecursiveDirs()
            .filter { it.getTotalSize() >= sizeToRemove }
            .map { it.getTotalSize() }
            .minOf { it }
    }

    class StateMachine {
        val root = File.createRootDir()
        var state: State = CD
        var cwd = root

        fun process(line: String) {
            if (line.startsWith("$ ls")) {
                state = LS
                return
            } else if (line.startsWith("$ cd")) {
                val target = line.split(" ").last()
                cwd = when (target) {
                    ".." -> cwd.parent
                    "/" -> root
                    else -> cwd.cd(target)
                }
                return
            }
            when (state) {
                CD -> error("no output expected")
                LS -> {
                    val (sizeOrDir, name) = line.split(" ")
                    if (sizeOrDir == "dir") {
                        cwd.addChild(File(name, 0, true, cwd))
                    } else {
                        cwd.addChild(File(name, sizeOrDir.toInt(), false, cwd))
                    }
                }
            }
        }

        sealed class State {
            object CD : State()
            object LS : State()
        }
    }

    class File private constructor(val name: String, val size: Int, val isDir: Boolean) {
        constructor(
            name: String,
            size: Int,
            isDir: Boolean,
            parent: File
        ) : this(name, size, isDir) {
            this.parent = parent
        }

        lateinit var parent: File
        private val children = mutableListOf<File>()

        fun cd(dirName: String): File {
            return children.find { dirName == it.name }
                ?: File(dirName, 0, true, this).also { children.add(it) }
        }

        fun addChild(file: File) {
            children.find { file.name == it.name } ?: children.add(file)
        }

        fun getTotalSize(): Int {
            return size + children.sumOf(File::getTotalSize)
        }

        fun getAllRecursiveDirs(): List<File> {
            return if (isDir) {
                listOf(this) + children.flatMap { it.getAllRecursiveDirs() }
            } else {
                listOf()
            }
        }

        companion object {
            fun createRootDir(): File = File("", 0, true)
        }
    }
}