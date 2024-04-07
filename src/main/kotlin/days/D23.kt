package days

@Suppress("unused")
object D23 : Day {
    private val instructions: List<Instruction> = readFile().map(::toInstruction)

    override suspend fun part1(): String = compute(0).toString()

    override suspend fun part2(): String = compute(1).toString()

    private fun compute(initialA: Int): Int {
        var a = initialA
        var b = 0
        var p = 0
        while (p in instructions.indices) {
            when (val instruction = instructions[p]) {
                is Half -> {
                    if (instruction.register == 'a') a /= 2 else b /= 2
                    p++
                }
                is Triple -> {
                    if (instruction.register == 'a') a *= 3 else b *= 3
                    p++
                }
                is Increment -> {
                    if (instruction.register == 'a') a++ else b++
                    p++
                }
                is Jump -> p += instruction.offset
                is JumpIfEven -> if ((instruction.register == 'a' && a % 2 == 0) || (instruction.register == 'b' && a % 2 == 0)) p += instruction.offset else p++
                is JumpIfOdd -> if ((instruction.register == 'a' && a == 1) || (instruction.register == 'b' && b == 1)) p += instruction.offset else p++
            }
        }
        return b
    }

    private sealed interface Instruction
    private data class Half(val register: Char): Instruction
    private data class Triple(val register: Char): Instruction
    private data class Increment(val register: Char): Instruction
    private data class Jump(val offset: Int): Instruction
    private data class JumpIfEven(val register: Char, val offset: Int): Instruction
    private data class JumpIfOdd(val register: Char, val offset: Int): Instruction

    private fun toInstruction(s: String): Instruction {
        val split = s.split(" ")
        return when (split[0]) {
            "hlf" -> Half(split[1][0])
            "tpl" -> Triple(split[1][0])
            "inc" -> Increment(split[1][0])
            "jmp" -> Jump(split[1].toInt())
            "jie" -> JumpIfEven(split[1][0], split[2].toInt())
            "jio" -> JumpIfOdd(split[1][0], split[2].toInt())
            else -> error("Unexpected instruction ${split[0]}")
        }
    }
}
