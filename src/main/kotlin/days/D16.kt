package days

@Suppress("unused")
object D16 : Day {
    private val sues = readFile().map { line ->
        line.substringAfter(": ").split(", ").associate { prop ->
            val (key, value) = prop.split(": ")
            key to value.toInt()
        }
    }

    private val giftSue = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1
    )

    override suspend fun part1(): String =
        sues.indexOfFirst { sue -> sue.all { (k, v) -> v == giftSue[k] } }.inc().toString()

    override suspend fun part2(): String =
        sues.indexOfFirst { sue ->
            sue.all { (k, v) ->
                val giftSueValue = giftSue[k] ?: error("Unexpected key $k")
                when (k) {
                    "cats", "trees" -> v > giftSueValue
                    "pomeranians", "goldfish" -> v < giftSueValue
                    else -> v == giftSueValue
                }
            }
        }.inc().toString()
}
