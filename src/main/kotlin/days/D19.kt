package days

@Suppress("unused")
object D19 : Day {
    private val input = readFile().last()
    private val replacements: Map<Regex, List<String>> = buildMap<String, List<String>> {
        readFile().takeWhile(String::isNotEmpty).forEach { line ->
            val (from, to) = line.split(" => ")
            put(from, getOrDefault(from, listOf()).plus(to))
        }
    }.mapKeys { (k, _) -> Regex(k) }
    private val reverseReplacements: List<Pair<Regex, String>> = buildMap {
        readFile().takeWhile(String::isNotEmpty).forEach { line ->
            val (from, to) = line.split(" => ")
            put(Regex(to), from)
        }
    }.toList().sortedByDescending { (regex, _) -> regex.pattern.length }

    override suspend fun part1(): String =
        replacements.flatMap { (regex, alts) ->
            regex.findAll(input).flatMap { matchResult ->
                alts.map { alt -> input.replaceRange(matchResult.range, alt) }
            }
        }.toSet().size.toString()

    override suspend fun part2(): String {
        var steps = 0
        var molecule = input
        while (molecule != "e") {
            reverseReplacements.forEach { (regex, replacement) ->
                val matches = regex.findAll(molecule).toList().size
                steps += matches
                repeat(matches) { molecule = regex.replaceFirst(molecule, replacement) }
            }
        }
        return steps.toString()
    }
}
