package days

import java.util.PriorityQueue

@Suppress("unused")
object D22 : Day {
    private val bossHp: Int
    private val bossDamage: Int
    private const val PLAYER_HP: Int = 50
    private const val PLAYER_MANA: Int = 500
    private const val HARD_MODE_DAMAGE: Int = 1

    init {
        val (hp, damage) = readFile().map { line -> line.split(": ")[1].toInt() }
        bossHp = hp
        bossDamage = damage
    }

    private val magicMissile: Spell = Spell(53, 4, 0)
    private val drain: Spell = Spell(73, 2, 0)
    private val shield: Spell = Spell(113, 7, 6)
    private val poison: Spell = Spell(173, 3, 6)
    private val recharge: Spell = Spell(229, 101, 5)

    private fun fight(hardMode: Boolean): Int {
        val queue = PriorityQueue(compareByState)
        queue.add(State(0, PLAYER_HP, bossHp, PLAYER_MANA, 0, 0, 0, true))
        while (true) {
            val state = queue.poll()
            if (state.bossHp <= 0) return state.manaSpent
            else if (state.playerHp <= 0) continue
            else if (hardMode && state.isPlayerTurn && state.playerHp <= HARD_MODE_DAMAGE) continue
            queue.addAll(state.computeNext(hardMode))
        }
    }

    override suspend fun part1(): String = fight(false).toString()

    override suspend fun part2(): String = fight(true).toString()

    private val compareByState: Comparator<State> = compareBy(State::manaSpent).thenBy(State::playerMana).thenBy(State::playerHp).thenBy(State::bossHp).thenBy(State::shieldTurns).thenBy(State::poisonTurns).thenBy(State::rechargeTurns).thenBy(State::isPlayerTurn)

    private data class Spell(
        val cost: Int,
        val potency: Int,
        val duration: Int,
    )

    private data class State(
        val manaSpent: Int,
        val playerHp: Int,
        val bossHp: Int,
        val playerMana: Int,
        val shieldTurns: Int,
        val poisonTurns: Int,
        val rechargeTurns: Int,
        val isPlayerTurn: Boolean,
    )

    private fun State.computeNext(hardMode: Boolean): List<State> {
        val damage = when {
            isPlayerTurn -> 0
            shieldTurns > 0 -> bossDamage - shield.potency
            else -> bossDamage
        }
        val hardModeDamage = if (hardMode && isPlayerTurn) HARD_MODE_DAMAGE else 0
        val newPlayerHp = playerHp - damage - hardModeDamage
        val newPlayerMana = if (rechargeTurns > 0) playerMana + recharge.potency else playerMana
        val newBossHp = if (poisonTurns > 0) bossHp - poison.potency else bossHp
        val newIsPlayerTurn = !isPlayerTurn
        val base = copy(
            playerHp = newPlayerHp,
            playerMana = newPlayerMana,
            bossHp = newBossHp,
            shieldTurns = shieldTurns.dec().coerceAtLeast(0),
            poisonTurns = poisonTurns.dec().coerceAtLeast(0),
            rechargeTurns = rechargeTurns.dec().coerceAtLeast(0),
            isPlayerTurn = newIsPlayerTurn,
        )
        return buildList {
            if (isPlayerTurn) {
                if (base.playerMana >= magicMissile.cost) {
                    add(base.copy(manaSpent = base.manaSpent + magicMissile.cost, playerMana = base.playerMana - magicMissile.cost, bossHp = base.bossHp - magicMissile.potency))
                }
                if (base.playerMana >= drain.cost) {
                    add(base.copy(manaSpent = base.manaSpent + drain.cost, playerMana = base.playerMana - drain.cost, playerHp = base.playerHp + drain.potency, bossHp = base.bossHp - drain.potency))
                }
                if (base.playerMana >= shield.cost && base.shieldTurns == 0) {
                    add(base.copy(manaSpent = base.manaSpent + shield.cost, playerMana = base.playerMana - shield.cost, shieldTurns = shield.duration))
                }
                if (base.playerMana >= poison.cost && base.poisonTurns == 0) {
                    add(base.copy(manaSpent = base.manaSpent + poison.cost, playerMana = base.playerMana - poison.cost, poisonTurns = poison.duration))
                }
                if (base.playerMana >= recharge.cost && base.rechargeTurns == 0) {
                    add(base.copy(manaSpent = base.manaSpent + recharge.cost, playerMana = base.playerMana - recharge.cost, rechargeTurns = recharge.duration))
                }
            } else {
                add(base)
            }
        }
    }
}
