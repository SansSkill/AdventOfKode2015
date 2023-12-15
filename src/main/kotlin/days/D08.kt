package days

@Suppress("unused")
object D08 : Day {
    private val input: List<String> = readFile()

    override suspend fun part1(): String =
        input.sumOf { line ->
            line.length - line.drop(1).dropLast(1).replace(Regex("\\\\(\"|\\\\|x[a-zA-Z0-9]{2})"), "@").length
        }.toString()

    override suspend fun part2(): String =
        input.sumOf { line -> 2 + line.count { c -> c == '\\' || c =='"' } }.toString()
}
