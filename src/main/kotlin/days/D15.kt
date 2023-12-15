package days

@Suppress("unused")
object D15 : Day {
    private val numberRegex = Regex("-?\\d+")
    private val ingredients = readFile().map { line ->
        numberRegex.findAll(line).map(MatchResult::value).map(String::toInt).toList()
    }

    private fun getBestCookieScore(calorieCondition: (Int) -> Boolean): Int =
        (0..100).maxOf { a ->
            val (a1, a2, a3, a4, a5) = ingredients[0].map(a::times)
            (0..100 - a).maxOf { b ->
                val (b1, b2, b3, b4, b5) = ingredients[1].map(b::times)
                (0..100 - a - b).maxOf { c ->
                    val (c1, c2, c3, c4, c5) = ingredients[2].map(c::times)
                    val (d1, d2, d3, d4, d5) = ingredients[3].map((100 - a - b - c)::times)

                    val p1 = (a1 + b1 + c1 + d1).coerceAtLeast(0)
                    val p2 = (a2 + b2 + c2 + d2).coerceAtLeast(0)
                    val p3 = (a3 + b3 + c3 + d3).coerceAtLeast(0)
                    val p4 = (a4 + b4 + c4 + d4).coerceAtLeast(0)

                    if (calorieCondition(a5 + b5 + c5 + d5)) p1 * p2 * p3 * p4 else 0
                }
            }
        }

    override suspend fun part1(): String =
        getBestCookieScore { true }.toString()

    override suspend fun part2(): String =
        getBestCookieScore { calories -> calories == 500 }.toString()

}
