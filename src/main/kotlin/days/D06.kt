package days

@Suppress("unused")
object D06 : Day {
    private val input: List<Triple<IntRange, IntRange, String>> = readFile().map { line ->
        val splits = line.split(" ")
        val isToggle = splits[0] == "toggle"
        val commandIndex = if (isToggle) 0 else 1
        val range1Index = if (isToggle) 1 else 2
        val range2Index = if (isToggle) 3 else 4

        val (x1, y1) = splits[range1Index].split(",").map(String::toInt)
        val (x2, y2) = splits[range2Index].split(",").map(String::toInt)
        Triple((x1..x2), (y1..y2), splits[commandIndex])
    }

    private fun <T> operateGrid(
        grid: Array<Array<T>>,
        toggleFn: (T) -> (T),
        onFn: (T) -> (T),
        offFn: (T) -> (T),
        countFn: (T) -> Int
    ): Int {
        fun commandFn(command: String) =
            when (command) {
                "toggle" -> toggleFn
                "on" -> onFn
                "off" -> offFn
                else -> error("Unexpected command $command")
            }
        input.forEach { (xRange, yRange, command) ->
            for (x in xRange) for (y in yRange) grid[x][y] = commandFn(command)(grid[x][y])
        }
        return grid.sumOf { arr -> arr.sumOf { t -> countFn(t) } }
    }

    override suspend fun part1(): String =
        operateGrid(
            Array(1000) { Array(1000) { false } },
            { b: Boolean -> !b },
            { true },
            { false },
            { b: Boolean -> if (b) 1 else 0 }
        ).toString()

    override suspend fun part2(): String =
        operateGrid(
            Array(1000) { Array(1000) { 0 } },
            { i -> i + 2 },
            { i -> i + 1 },
            { i -> 0.coerceAtLeast(i - 1) },
            { i: Int -> i }
        ).toString()
}
