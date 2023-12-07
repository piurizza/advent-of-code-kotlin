package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D07 : Solution {

    override fun partOne(input: String): Int {
        return this.parseHandsWithoutJolly(input)
            .sorted()
            .mapIndexed { index, hand ->
                hand.bid * (index + 1)
            }
            .reduce { acc, wins -> acc + wins }
    }

    override fun partTwo(input: String): Int {
        return this.parseHandsWithJolly(input)
            .sorted()
            .mapIndexed { index, hand ->
                hand.bid * (index + 1)
            }
            .reduce { acc, wins -> acc + wins }
    }

    private fun parseHandsWithoutJolly(input: String): List<Hand> =
        input
            .split("\n")
            .map { row ->
                val cards = arrayOf(
                    lookupCardValueWithoutJolly(row[0]),
                    lookupCardValueWithoutJolly(row[1]),
                    lookupCardValueWithoutJolly(row[2]),
                    lookupCardValueWithoutJolly(row[3]),
                    lookupCardValueWithoutJolly(row[4]),
                )

                Hand(
                    cards,
                    row.substring(6).toInt(),
                    calculateTypeWithoutJolly(cards)
                )
            }

    private fun lookupCardValueWithoutJolly(card: Char): Byte =
        when (card) {
            in '2'..'9' -> (card.digitToInt() - 2).toByte()
            'T' -> 8.toByte()
            'J' -> 9.toByte()
            'Q' -> 10.toByte()
            'K' -> 11.toByte()
            'A' -> 12.toByte()
            else -> throw IllegalStateException("Illegal card")
        }

    private fun calculateTypeWithoutJolly(cards: Array<Byte>): Type {
        val count = ByteArray(13) { 0 }

        cards
            .forEach { card ->
                count[card.toInt()]++
            }

        count.sortDescending()

        if (count[0] == 5.toByte()) return Type.FIVE_OF_A_KIND
        if (count[0] == 4.toByte()) return Type.FOUR_OF_A_KIND
        if (count[0] == 3.toByte() && count[1] == 2.toByte()) return Type.FULL_HOUSE
        if (count[0] == 3.toByte()) return Type.THREE_OF_A_KIND
        if (count[0] == 2.toByte() && count[1] == 2.toByte()) return Type.TWO_PAIR
        if (count[0] == 2.toByte()) return Type.ONE_PAIR
        return Type.HIGH_CARD
    }

    private fun parseHandsWithJolly(input: String): List<Hand> =
        input
            .split("\n")
            .map { row ->
                val cards = arrayOf(
                    lookupCardValueWithJolly(row[0]),
                    lookupCardValueWithJolly(row[1]),
                    lookupCardValueWithJolly(row[2]),
                    lookupCardValueWithJolly(row[3]),
                    lookupCardValueWithJolly(row[4]),
                )

                Hand(
                    cards,
                    row.substring(6).toInt(),
                    calculateTypeWithJolly(cards)

                )
            }

    private fun lookupCardValueWithJolly(card: Char): Byte =
        when (card) {
            'J' -> 0.toByte()
            in '2'..'9' -> (card.digitToInt() - 1).toByte()
            'T' -> 9.toByte()
            'Q' -> 10.toByte()
            'K' -> 11.toByte()
            'A' -> 12.toByte()
            else -> throw IllegalStateException("Illegal card")
        }

    private fun calculateTypeWithJolly(cards: Array<Byte>): Type {
        val count = ByteArray(13) { 0 }

        cards
            .forEach { card ->
                count[card.toInt()]++
            }

        val jollyCount = count[0]

        val countWithoutJolly = count.copyOfRange(1, count.size)

        countWithoutJolly.sortDescending()

        if (
            countWithoutJolly[0].plusByte(jollyCount) == 5.toByte() ||
            jollyCount == 5.toByte()
        )
            return Type.FIVE_OF_A_KIND

        if (countWithoutJolly[0].plusByte(jollyCount) == 4.toByte())
            return Type.FOUR_OF_A_KIND

        if (countWithoutJolly[0].plusByte(jollyCount) == 3.toByte() && countWithoutJolly[1] == 2.toByte())
            return Type.FULL_HOUSE

        if (countWithoutJolly[0].plusByte(jollyCount) == 3.toByte())
            return Type.THREE_OF_A_KIND

        if (countWithoutJolly[0] == 2.toByte() && countWithoutJolly[1] == 2.toByte())
            return Type.TWO_PAIR

        if (countWithoutJolly[0].plusByte(jollyCount) == 2.toByte())
            return Type.ONE_PAIR

        return Type.HIGH_CARD
    }

    data class Hand(
        val cards: Array<Byte>,
        val bid: Int,
        val type: Type
    ) : Comparable<Hand> {

        override fun compareTo(other: Hand): Int {
            val typeComparison = this.type.ordinal.compareTo(other.type.ordinal)

            if (typeComparison != 0) return typeComparison

            this.cards
                .zip(other.cards)
                .map { comparison ->
                    if (comparison.first < comparison.second) return -1
                    if (comparison.first > comparison.second) return 1
                }

            return 0
        }
    }

    private fun Byte.plusByte(other: Byte) = (this + other).toByte()

    enum class Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }
}