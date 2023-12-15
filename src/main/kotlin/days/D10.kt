package days

@Suppress("unused")
object D10 : Day {
    private val input = readFile()[0]

    private fun lookAndSee(s: String): String =
        buildString {
            var curChar = s[0]
            var curCount = 1
            (1 .. s.lastIndex).forEach { i ->
                val c = s[i]
                if (c == curChar) curCount++
                else {
                    append("$curCount$curChar")
                    curChar = c
                    curCount = 1
                }
            }
            append("$curCount$curChar")
        }

    private fun lookAndSee(rounds: Int): Int {
        var s = input
        repeat(rounds) { s = lookAndSee(s) }
        return s.length
    }

    override suspend fun part1(): String =
        lookAndSee(40).toString()

    override suspend fun part2(): String =
        lookAndSee(50).toString()
}
