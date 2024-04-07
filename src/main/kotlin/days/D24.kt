package days

@Suppress("unused")
object D24 : Day {
    private val packages: List<Int> = readFile().map(String::toInt)

    override suspend fun part1(): String = split(3, ::canSplitInTwo).toString()

    override suspend fun part2(): String = split(4, ::canSplitInThree).toString()

    private fun split(compartments: Int, canSplit: (List<Int>, Int) -> Boolean): Long {
        val compartmentSize = packages.sum() / compartments
        var k = 1
        while (true) {
            val leftSides = getLeftSides(k, compartmentSize).filter { list -> list.isNotEmpty() && canSplit(packages.minus(list.toSet()), compartmentSize) }
            if (leftSides.isEmpty()) k++
            else return leftSides.minOf { list -> list.map(Int::toLong).reduce(Long::times) }
        }
    }

    private fun getLeftSides(k: Int, compartmentSize: Int): List<List<Int>> {
        val ans = mutableListOf<List<Int>>()
        val ls = mutableListOf<Int>()
        var sum = 0
        fun recur(i: Int) {
            if (i == packages.size) {
                if (sum == compartmentSize) ans.add(ls.toList())
                return
            }
            else if (ls.size == k) {
                if (sum == compartmentSize) ans.add(ls.toList())
            } else {
                val n = packages[i]
                if (n + sum <= compartmentSize) {
                    sum += n
                    ls.add(n)
                    recur(i + 1)
                    sum -= n
                    ls.remove(n)
                }
                recur(i + 1)
            }
        }
        recur(0)
        return ans
    }

    private fun canSplitInTwo(list: List<Int>, compartmentSize: Int): Boolean {
        fun recur(i: Int, total: Int): Boolean =
            when {
                total == compartmentSize -> true
                total > compartmentSize || i < 0 -> false
                else -> recur(i - 1, total + list[i]) || recur(i - 1, total)
            }
        return recur(list.lastIndex, 0)
    }

    private fun canSplitInThree(list: List<Int>, compartmentSize: Int): Boolean {
        fun recur(i: Int, sublist: List<Int>): Boolean {
            val sum = sublist.sum()
            return when {
                sum == compartmentSize -> canSplitInTwo(list.minus(sublist.toSet()), compartmentSize)
                sum > compartmentSize || i < 0 -> false
                else -> recur(i - 1, sublist + list[i]) || recur(i - 1, sublist)
            }
        }
        return recur(list.lastIndex, listOf())
    }
}
