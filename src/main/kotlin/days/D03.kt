package days

@Suppress("unused")
object D03 : Day {
    private val input: String = readFile()[0]
    private fun getHousesVisited(input: String): Set<Pair<Int, Int>> =
        buildSet {
            add(0 to 0)
            var x = 0
            var y = 0
            input.forEach { c ->
                when (c) {
                    '>' -> x++
                    '<' -> x--
                    'v' -> y++
                    '^' -> y--
                }
                add(x to y)
            }
        }

    override suspend fun part1(): String =
        getHousesVisited(input).size.toString()

    override suspend fun part2(): String {
        val santaInput = input.filterIndexed { index, _ -> index % 2 == 0 }
        val roboSantaInput = input.filterIndexed { index, _ -> index % 2 == 1 }
        return (getHousesVisited(santaInput) + getHousesVisited(roboSantaInput)).size.toString()
    }
}