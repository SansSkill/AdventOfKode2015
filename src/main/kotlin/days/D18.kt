package days

@Suppress("unused")
object D18 : Day {
    private fun getGrid() = readFile().map { line -> BooleanArray(line.length) { i -> line[i] == '#' } }.toTypedArray()
    private val gridSize = getGrid().size

    private val offsets = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)

    private fun getOnNeighbourCount(grid: Array<BooleanArray>, x: Int, y: Int) =
        offsets.count { (xo, yo) -> (x + xo) in 0..< gridSize && (y + yo) in 0..< gridSize && grid[x + xo][y + yo] }

    private fun step(grid: Array<BooleanArray>) {
        val neighbourCounts = (0..< gridSize).map { x -> (0..< gridSize).map { y -> getOnNeighbourCount(grid, x, y) } }
        (0..< gridSize).forEach { x -> (0..< gridSize).forEach { y ->
            grid[x][y] = neighbourCounts[x][y] == 3 || grid[x][y] && neighbourCounts[x][y] == 2
        } }
    }

    private fun turnOnCorners(grid: Array<BooleanArray>) {
        grid[0][0] = true
        grid[0][gridSize - 1] = true
        grid[gridSize - 1][0] = true
        grid[gridSize - 1][gridSize - 1] = true
    }

    override suspend fun part1(): String =
        getGrid().apply { repeat(100) { step(this) } }.sumOf { row -> row.count { b -> b } }.toString()

    override suspend fun part2(): String {
        fun Array<BooleanArray>.turnOnCorners() {
            this[0][0] = true
            this[0][gridSize - 1] = true
            this[gridSize - 1][0] = true
            this[gridSize - 1][gridSize - 1] = true
        }
        return getGrid().apply {
            turnOnCorners()
            repeat(100) {
                step(this)
                turnOnCorners()
            }
        }.sumOf { row -> row.count { b -> b } }.toString()
    }
}
