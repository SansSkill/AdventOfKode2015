package days

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject

@Suppress("unused")
object D12 : Day {
    private val input = readFile()[0]

    override suspend fun part1(): String =
        Regex("-?\\d+").findAll(input).sumOf { matchResult -> matchResult.value.toInt() }.toString()

    override suspend fun part2(): String {
        fun sum(jsonElement: JsonElement): Int =
            when (jsonElement) {
                is JsonPrimitive -> jsonElement.intOrNull ?: 0
                is JsonArray -> jsonElement.sumOf(::sum)
                is JsonObject -> if (jsonElement.any { (_, e) -> e is JsonPrimitive && e.isString && e.content == "red" }) 0
                else jsonElement.jsonObject.entries.sumOf { (_, e) -> sum(e) }
                else -> error("Unexpected json element type ${jsonElement::class.simpleName}")
            }
        return sum(Json.parseToJsonElement(input)).toString()
    }
}
