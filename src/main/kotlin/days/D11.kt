package days

@Suppress("unused")
object D11 : Day {
    private val input = readFile()[0]
    private val doublePairRegex = Regex(".*(.)\\1.*(.)\\2.*")

    private fun nextPassword(s: String): String {
        val arr = s.toCharArray()
        var i = arr.lastIndex
        var carry = true
        while (carry) {
            carry = false
            arr[i] = when (val c = arr[i]) {
                'z' -> { carry = true; 'a'}
                'i', 'o', 'l' -> c.code.plus(2).toChar()
                else -> c.code.inc().toChar()
            }
            i--
        }
        return arr.joinToString("")
    }

    private fun isValidPassword(s: String): Boolean =
        s.matches(doublePairRegex) && (0 ..< s.lastIndex - 1).any { i ->
            s[i].code.inc() == s[i + 1].code && s[i + 1].code.inc() == s[i + 2].code
        }

    private fun nextValidPassword(currentPassword: String): String {
        var s = nextPassword(currentPassword)
        while (!isValidPassword(s)) s = nextPassword(s)
        return s
    }

    override suspend fun part1(): String =
        nextValidPassword(input)

    override suspend fun part2(): String =
        nextValidPassword(nextValidPassword(input))
}
