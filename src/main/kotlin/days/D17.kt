package days

@Suppress("unused")
object D17 : Day {
    private val input = readFile().map(String::toInt).sortedDescending()
    private const val TOTAL = 150

    override suspend fun part1(): String {
        fun recur(i: Int, sum: Int): Int =
            when {
                sum == TOTAL -> 1
                i == input.size || sum > TOTAL -> 0
                else -> recur(i + 1, sum) + recur(i + 1, sum + input[i])
            }
        return recur(0, 0).toString()
    }

    override suspend fun part2(): String {
        val map = mutableMapOf<Int, Int>()
        fun recur(i: Int, n: Int, sum: Int) {
            when {
                sum == TOTAL -> map[n] = map.getOrDefault(n, 0).inc()
                i < input.size && sum < TOTAL -> {
                    recur(i + 1, n, sum)
                    recur(i + 1, n + 1,sum + input[i])
                }
            }
        }
        recur(0, 0, 0)
        return map.entries.minBy(Map.Entry<Int, Int>::key).value.toString()
    }
}
