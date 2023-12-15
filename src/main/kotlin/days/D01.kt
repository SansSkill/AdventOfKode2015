package days

@Suppress("unused")
object D01 : Day {
    private val input = readFile()[0]

    override suspend fun part1(): String =
        (2 * input.count { c -> c == '(' } - input.length).toString()

    override suspend fun part2(): String {
        var floor = 0
        input.forEachIndexed { index, c ->
            if (c == '(') floor++ else floor--
            if (floor == -1) return@part2 index.inc().toString()
        }
        error("Santa never enters basement")
    }
}
