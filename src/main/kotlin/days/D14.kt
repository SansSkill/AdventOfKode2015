package days

@Suppress("unused")
object D14 : Day {
    private val numberRegex = Regex("\\d+")
    private val reindeers = readFile().map { line ->
        numberRegex.findAll(line).map(MatchResult::value).map(String::toInt).toList()
            .let { (n1, n2, n3) -> Triple(n1, n2, n2 + n3) }
    }

    private const val RACE_DURATION = 2503

    override suspend fun part1(): String =
        reindeers.maxOf { (speed, runTime, cycleTime) ->
            speed * (RACE_DURATION.div(cycleTime).times(runTime) + RACE_DURATION.mod(cycleTime).coerceAtMost(runTime))
        }.toString()

    override suspend fun part2(): String {
        val distance = IntArray(reindeers.size)
        val scores = IntArray(reindeers.size)

        (0 ..< RACE_DURATION).forEach { i ->
            reindeers.forEachIndexed { index, (speed, runTime, cycleTime) ->
                if (i.mod(cycleTime) < runTime) distance[index] += speed
            }
            val maxDistance = distance.max()
            reindeers.indices.forEach { index -> if (distance[index] == maxDistance) scores[index]++ }
        }
        return scores.max().toString()
    }
}
