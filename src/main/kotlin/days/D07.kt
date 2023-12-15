package days

@Suppress("unused")
object D07 : Day {
    private val initialSignals: Map<String, Int>
    private val input: List<Command>

    init {
        val signals = mutableMapOf<String, Int>()
        input = readFile().mapNotNull { line ->
            val splits = line.split(" ")
            when {
                splits.size == 3 && splits[0].toIntOrNull() == null -> AssignCommand(splits[2], splits[0])
                splits.size == 3 -> { signals[splits[2]] = splits[0].toInt(); null }
                splits.size == 4 -> NotCommand(splits[3], splits[1])
                splits[1] == "OR" -> OrCommand(splits[4], splits[0], splits[2])
                splits[1] == "LSHIFT" -> LShiftCommand(splits[4], splits[0], splits[2].toInt())
                splits[1] == "RSHIFT" -> RShiftCommand(splits[4], splits[0], splits[2].toInt())
                splits[1] == "AND" && splits[0] == "1" -> AndOneCommand(splits[4], splits[2])
                splits[1] == "AND" -> AndCommand(splits[4], splits[0], splits[2])
                else -> error("Unexpected input $splits")
            }
        }
        initialSignals = signals
    }

    private fun runCommands(signals: MutableMap<String, Int>): Int {
        var remainingInputs = input
        while (remainingInputs.isNotEmpty()) {
            val (availableInputs, unavailableInputs) = remainingInputs.partition { command ->
                when (command) {
                    is UnaryCommand -> command.from in signals
                    is BinaryValueCommand -> command.from in signals
                    is BinaryCommand -> command.left in signals && command.right in signals
                }
            }
            remainingInputs = unavailableInputs
            availableInputs.forEach { command -> command.execute(signals) }
        }
        return signals["a"] ?: error("Signal 'a' not found")
    }

    override suspend fun part1(): String =
        runCommands(initialSignals.toMutableMap()).toString()

    override suspend fun part2(): String =
        runCommands(initialSignals.toMutableMap().apply { put("b", runCommands(initialSignals.toMutableMap())) }).toString()

    private sealed interface Command { val to: String; fun execute(signals: MutableMap<String, Int>): Unit }
    private sealed interface UnaryCommand: Command { val from: String }
    private sealed interface BinaryCommand: Command { val left: String; val right: String }
    private sealed interface BinaryValueCommand: Command { val from: String; val value: Int }

    private data class AssignCommand(override val to: String, override val from: String): UnaryCommand {
        override fun execute(signals: MutableMap<String, Int>) { signals[to] = signals[from]!! }
    }

    private data class NotCommand(override val to: String, override val from: String): UnaryCommand {
        override fun execute(signals: MutableMap<String, Int>) { signals[to] = signals[from]!!.inv() }
    }

    private data class AndOneCommand(override val to: String, override val from: String): UnaryCommand {
        override fun execute(signals: MutableMap<String, Int>) { signals[to] = signals[from]!!.and(1) }
    }

    private data class AndCommand(override val to: String, override val left: String, override val right: String): BinaryCommand {
        override fun execute(signals: MutableMap<String, Int>) { signals[to] = signals[left]!!.and(signals[right]!!) }
    }

    private data class OrCommand(override val to: String, override val left: String, override val right: String): BinaryCommand {
        override fun execute(signals: MutableMap<String, Int>) { signals[to] = signals[left]!!.or(signals[right]!!) }
    }

    private data class LShiftCommand(override val to: String, override val from: String, override val value: Int): BinaryValueCommand {
        override fun execute(signals: MutableMap<String, Int>) { signals[to] = signals[from]!!.shl(value) }
    }

    private data class RShiftCommand(override val to: String, override val from: String, override val value: Int): BinaryValueCommand {
        override fun execute(signals: MutableMap<String, Int>) { signals[to] = signals[from]!!.shr(value) }
    }
}
