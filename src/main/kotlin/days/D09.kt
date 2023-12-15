package days

@Suppress("unused")
object D09 : Day {
    private val cityMap: Map<String, MutableMap<String, Int>> = buildMap {
        readFile().forEach { line ->
            val (city1, _, city2, _, dist) = line.split(" ")
            put(city1, getOrDefault(city1, mutableMapOf()).apply { put(city2, dist.toInt()) })
            put(city2, getOrDefault(city2, mutableMapOf()).apply { put(city1, dist.toInt()) })
        }
    }
    private val numberOfCities = cityMap.keys.plus(cityMap.values.flatMap { map -> map.keys }).size

    override suspend fun part1(): String {
        fun recur(visited: Set<String>, current: String, travelTime: Int): Int =
            if (visited.size == numberOfCities) travelTime
            else cityMap[current]?.minus(visited)?.minOf { (next, dist) ->
                recur(visited.plus(next), next, travelTime + dist)
            } ?: error("Could not find route from $current")

        return cityMap.keys.minOf { city -> recur(setOf(city), city, 0) }.toString()
    }

    override suspend fun part2(): String {
        fun recur(visited: Set<String>, current: String, travelTime: Int): Int =
            if (visited.size == numberOfCities) travelTime
            else cityMap[current]?.minus(visited)?.maxOf { (next, dist) ->
                recur(visited.plus(next), next, travelTime + dist)
            } ?: error("Could not find route from $current")

        return cityMap.keys.maxOf { city -> recur(setOf(city), city, 0) }.toString()
    }
}
