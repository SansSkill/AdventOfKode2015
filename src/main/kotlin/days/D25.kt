package days

@Suppress("unused")
object D25 : Day {
    private val row: Int
    private val column: Int

    init {
        val (row, column) = readFile().map { line -> line.split(" ")[1].toInt() }
        this.row = row
        this.column = column
    }

    override suspend fun part1(): String {
        var x = 1
        var y = 1
        var n: Long = 20151125
        while (x != row || y != column) {
            if (x > 1) {
                x--
                y++
            } else {
                x = y + 1
                y = 1
            }
            n = (n * 252533) % 33554393
        }
        return n.toString()
    }

    override suspend fun part2(): String = "No part 2"
}
