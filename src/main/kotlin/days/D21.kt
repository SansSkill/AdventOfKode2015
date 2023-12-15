package days

@Suppress("unused")
object D21 : Day {
    private val bossStats = readFile().map { line -> line.split(": ")[1].toInt() }
    private val weapons = listOf(8 to 4, 10 to 5, 25 to 6, 40 to 7, 84 to 8)
    private val armor = listOf(0 to 0, 13 to 1, 31 to 2, 53 to 3, 75 to 4, 102 to 5)
    private val rings = listOf(
        Triple(0, 0, 0),
        Triple(25, 1, 0), Triple(50, 2, 0), Triple(100, 3, 0),
        Triple(20, 0, 1), Triple(40, 0, 2), Triple(80, 0, 3),
    )

    private fun beatsBoss(damage: Int, armor: Int): Boolean {
        val (bossHp, bossDamage, bossArmor) = bossStats
        val dmgDealt = damage.minus(bossArmor).coerceAtLeast(1)
        val dmgTaken = bossDamage.minus(armor).coerceAtLeast(1)
        val bossDeadTurn = bossHp.plus(dmgDealt.dec()).div(dmgDealt)
        val playerDeadTurn = 100.plus(dmgTaken.dec()).div(dmgTaken)
        return bossDeadTurn <= playerDeadTurn
    }

    override suspend fun part1(): String =
        weapons.minOf { (g1, d1) ->
            armor.minOf { (g2, a1) ->
                rings.indices.minOf { ringIndex1 ->
                    val (g3, d2, a2) = rings[ringIndex1]
                    (ringIndex1.inc() .. rings.lastIndex).plus(0).minOf { ringIndex2 ->
                        val (g4, d3, a3) = rings[ringIndex2]
                        if (beatsBoss(d1 + d2 + d3, a1 + a2 + a3)) g1 + g2 + g3 + g4 else Int.MAX_VALUE
                    }
                }
            }
        }.toString()

    override suspend fun part2(): String =
        weapons.maxOf { (g1, d1) ->
            armor.maxOf { (g2, a1) ->
                rings.indices.maxOf { ringIndex1 ->
                    val (g3, d2, a2) = rings[ringIndex1]
                    (ringIndex1.inc() .. rings.lastIndex).plus(0).maxOf { ringIndex2 ->
                        val (g4, d3, a3) = rings[ringIndex2]
                        if (!beatsBoss(d1 + d2 + d3, a1 + a2 + a3)) {
                            g1 + g2 + g3 + g4
                        } else Int.MIN_VALUE

                    }
                }
            }
        }.toString()
}
