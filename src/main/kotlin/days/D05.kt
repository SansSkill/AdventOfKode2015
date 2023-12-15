package days

@Suppress("unused")
object D05 : Day {
    private val input = readFile()

    override suspend fun part1(): String =
        input.count { s ->
            val vowels = s.count { c -> c in "aeiou" }
            val hasDouble = (0..< s.lastIndex).any { i -> s[i] == s[i + 1] }
            val hasIllegalSubstring = (0..< s.lastIndex).any { i ->
                "${s[i]}${s[i + 1]}" in listOf("ab", "cd", "pq", "xy")
            }
            vowels >= 3 && hasDouble && !hasIllegalSubstring
        }.toString()

    override suspend fun part2(): String =
        input.count { s ->
            val hasDoublePair = (0..< s.lastIndex).any { i ->
                s.indexOf("${s[i]}${s[i + 1]}", i + 2) != -1
            }
            val hasRepeat = (0 ..< s.lastIndex - 1).any { i -> s[i] == s[i + 2] }
            hasDoublePair && hasRepeat
        }.toString()
}
