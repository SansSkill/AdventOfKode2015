package days

@Suppress("unused")
object D02 : Day {
    private val input = readFile().map { line -> line.split("x").map(String::toInt) }

    override suspend fun part1(): String =
        input.sumOf { (l, w, h) ->
            2 * (l * w + l * h + w * h) + (l * w).coerceAtMost(l * h).coerceAtMost(w * h)
        }.toString()

    override suspend fun part2(): String =
        input.sumOf { (l, w, h) ->
             l * w * h + 2 * (l + w + h) - 2 * l.coerceAtLeast(w).coerceAtLeast(h)
        }.toString()
}