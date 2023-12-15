package days

@Suppress("unused")
object D13 : Day {
    private val seatValue: Map<Pair<String, String>, Int> = buildMap {
        readFile().forEach { line ->
            val splits = line.split(" ")
            val a = splits[0]
            val value = splits[3].toInt().let { value -> if (splits[2] == "gain") value else -value }
            val b = splits.last().dropLast(1)
            put((a to b), value)
        }
        keys.map(Pair<String, String>::first).toSet().forEach { name ->
            put(name to "you", 0)
            put("you" to name, 0)
        }
    }
    private val people = seatValue.keys.map(Pair<String, String>::first).toSet()

    private fun getPossibleSeatingArrangements(seated: List<String>, allPeople: Set<String>): List<List<String>> =
        if (seated.size == allPeople.size) listOf(seated)
        else allPeople.minus(seated.toSet()).map(seated::plus).flatMap { next -> getPossibleSeatingArrangements(next, allPeople) }

    private fun scoreSeatingArrangement(seated: List<String>): Int =
        seated.indices.sumOf { i ->
            val prevScore = seatValue[seated[i] to seated[(i + seated.size - 1) % seated.size]] ?: error("Seat value unknown")
            val nextScore = seatValue[seated[i] to seated[(i + 1) % seated.size]] ?: error("Seat value unknown")
            prevScore + nextScore
        }

    override suspend fun part1(): String =
        getPossibleSeatingArrangements(listOf(), people.minus("you")).maxOf(::scoreSeatingArrangement).toString()

    override suspend fun part2(): String =
        getPossibleSeatingArrangements(listOf(), people).maxOf(::scoreSeatingArrangement).toString()
}
