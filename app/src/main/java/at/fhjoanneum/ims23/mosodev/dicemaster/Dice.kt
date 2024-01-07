package at.fhjoanneum.ims23.mosodev.dicemaster

import kotlin.random.Random

class Dice(private val dieSize: Int) {
    private var advantageDie = 0
    private val rolls = intArrayOf(-1, -1)

    fun roll(): Int {
        return Random.nextInt(dieSize) + 1
    }
}