package days

import java.math.BigInteger
import java.security.MessageDigest

@Suppress("unused")
object D04: Day {
    private val input = readFile()[0]
    private val md = MessageDigest.getInstance("MD5")
    private fun md5(input:String): String =
        BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')

    private fun findHashNumber(leadingZeroes: Int): Int =
        "0".repeat(leadingZeroes).let { prefix ->
            generateSequence(1, nextFunction = Int::inc).first { i -> md5("$input$i").startsWith(prefix) }
        }

    override suspend fun part1(): String =
        findHashNumber(5).toString()

    override suspend fun part2(): String =
        findHashNumber(6).toString()
}
