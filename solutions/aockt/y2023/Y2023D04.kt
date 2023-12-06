package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D04 : Solution {
    override fun partOne(input: String): Any {
        return parseInput(input)
            .map { cardString ->
                val currentCard = parseCard(cardString)

                val winnersCount = currentCard.winningNumbers
                    .intersect(currentCard.numbersToCheck)
                    .count()

                if (winnersCount == 0) return@map 0

                1 shl (winnersCount - 1)

            }
            .reduce { acc, points -> acc + points }
    }

    override fun partTwo(input: String): Int {
        val cardsWinnersCount = parseInput(input)
            .map { cardString ->
                val currentCard = parseCard(cardString)

                val winnersCount = currentCard.winningNumbers
                    .intersect(currentCard.numbersToCheck)
                    .count()

                CardsWinnerCard(
                    currentCard.cardId,
                    winnersCount,
                    count = 1
                )
            }
            .toMutableList()

        generateWonCards(cardsWinnersCount)

        return cardsWinnersCount
            .map { it.count }
            .reduce { acc, cardNumber -> acc + cardNumber }
    }

    private fun parseInput(input: String): List<String> =
        input
            .splitToSequence('\n')
            .toList()

    private fun parseCard(cardString: String): PointsWinnerCard {
        val matches = Regex("\\s(\\d{0,2}\\s{0,2})*")
            .findAll(cardString)
            .toList()

        val cardId = matches[0].value
            .trimStart()
            .toInt()

        val winningNumbers = matches[1].value
            .trim()
            .split(" ")
            .mapNotNull { numberString ->
                if (numberString.isNotEmpty())
                    numberString.toInt()
                else null
            }
            .toSet()

        val numbersToCheck = matches[2].value
            .trimEnd()
            .split(" ")
            .mapNotNull { numberString ->
                if (numberString.isNotEmpty())
                    numberString.toInt()
                else null
            }
            .toSet()

        return PointsWinnerCard(cardId, winningNumbers, numbersToCheck)
    }

    private fun generateWonCards(cardsWinnersCount: MutableList<CardsWinnerCard>) =
        cardsWinnersCount
            .map { card ->
                if (card.winnersCount == 0) return@map

                (card.cardId until card.cardId + card.winnersCount)
                    .map { index ->
                        cardsWinnersCount[index] = cardsWinnersCount[index]
                            .copy(
                                count = cardsWinnersCount[index].count
                                    + card.count
                            )
                    }
            }

    data class PointsWinnerCard(
        val cardId: Int,
        val winningNumbers: Set<Int>,
        val numbersToCheck: Set<Int>
    )

    data class CardsWinnerCard(
        val cardId: Int,
        val winnersCount: Int,
        var count: Int
    )
}