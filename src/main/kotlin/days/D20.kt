package days

import kotlin.math.sqrt

@Suppress("unused")
object D20 : Day {
    private val input = readFile()[0].toInt()

    private fun getDivisors(n: Int): Set<Int> =
        buildSet {
            (1..sqrt(n.toDouble()).toInt()).forEach { i -> if (n % i == 0) { add(n / i); add(i) } }
        }

    override suspend fun part1(): String {
        var i = 1
        while (getDivisors(i).sum() * 10 < input) i++
        return i.toString()
    }

    override suspend fun part2(): String {
        var i = 1
        while (getDivisors(i).filter { n -> i <= n * 50 }.sum() * 11 < input) i++
        return i.toString()
    }
}
