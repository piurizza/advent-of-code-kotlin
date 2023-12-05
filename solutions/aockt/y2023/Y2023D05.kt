package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D05 : Solution {
    private fun parseInput(input: String): List<String> =
        input
            .split("\n\n")

    override fun partOne(input: String): Long {
        val parsedInput = parseInput(input)

        val almanac = Almanac(
            seeds = parsedInput[0].split(" ").drop(1).map {it.toLong()},
            seedToSoil = extractMap(parsedInput[1]),
            soilToFertilizer = extractMap(parsedInput[2]),
            fertilizerToWater = extractMap(parsedInput[3]),
            waterToLight = extractMap(parsedInput[4]),
            lightToTemperature = extractMap(parsedInput[5]),
            temperatureToHumidity = extractMap(parsedInput[6]),
            humidityToLocation = extractMap(parsedInput[7])
        )

        val seedRanges = almanac.seeds
            .map { LongRange(it, it) }

        val soil = almanac.seedToSoil.convert(seedRanges)
        val fertilizer = almanac.soilToFertilizer.convert(soil)
        val water = almanac.fertilizerToWater.convert(fertilizer)
        val light = almanac.waterToLight.convert(water)
        val temperature = almanac.lightToTemperature.convert(light)
        val humidity = almanac.temperatureToHumidity.convert(temperature)

        return almanac.humidityToLocation.convert(humidity)
            .minOf { it.first }
    }

    override fun partTwo(input: String): Any {
        val parsedInput = parseInput(input)

        val almanac = Almanac(
            seeds = parsedInput[0].split(" ").drop(1).map {it.toLong()},
            seedToSoil = extractMap(parsedInput[1]),
            soilToFertilizer = extractMap(parsedInput[2]),
            fertilizerToWater = extractMap(parsedInput[3]),
            waterToLight = extractMap(parsedInput[4]),
            lightToTemperature = extractMap(parsedInput[5]),
            temperatureToHumidity = extractMap(parsedInput[6]),
            humidityToLocation = extractMap(parsedInput[7])
        )

        val seedRanges = almanac.seeds
            .chunked(2)
            .map { (min, length) -> LongRange(min, min + length - 1) }

        val soil = almanac.seedToSoil.convert(seedRanges)
        val fertilizer = almanac.soilToFertilizer.convert(soil)
        val water = almanac.fertilizerToWater.convert(fertilizer)
        val light = almanac.waterToLight.convert(water)
        val temperature = almanac.lightToTemperature.convert(light)
        val humidity = almanac.temperatureToHumidity.convert(temperature)

        return almanac.humidityToLocation.convert(humidity)
            .minOf { it.first }
    }

    private fun extractMap(parsedBlock: String): Map<LongRange, Long> {
        val newMap = mutableMapOf<LongRange, Long>()

        parsedBlock
            .split("\n")
            .drop(1)
            .forEach { row ->
                val (destinationRangeStart, sourceRangeStart, rangeLength) = row
                    .split(" ")
                    .map { it.toLong() }

                newMap[LongRange(sourceRangeStart, sourceRangeStart + rangeLength - 1)] = destinationRangeStart
            }

        return newMap
    }

    private fun Map<LongRange, Long>.convert(ranges: List<LongRange>) =
        ranges
            .flatMap { (min, max) ->
                val destinationRanges = mutableListOf<LongRange>()
                val intersections = mutableListOf<LongRange>()

                this
                    .forEach { (range, destination) ->
                        val minRange = maxOf(min, range.first)
                        val maxRange = minOf(max, range.last)

                        if (minRange <= maxRange) {
                            intersections.add(LongRange(minRange, maxRange))
                            destinationRanges.add(
                                LongRange(
                                    minRange - range.first + destination,
                                    maxRange - range.first + destination
                                )
                            )
                        }
                    }

                intersections.sortBy { it.first }

                var pivot = min

                intersections
                    .forEach { intersection ->
                        if (intersection.first > pivot) {
                            destinationRanges.add(LongRange(pivot, intersection.first - 1))
                        }

                        pivot = intersection.last + 1
                    }

                if (pivot <= max) {
                    destinationRanges.add(LongRange(pivot, max))
                }

                destinationRanges
            }

    data class Almanac (
        val seeds: List<Long> = listOf(),
        val seedToSoil: Map<LongRange, Long> = mapOf(),
        val soilToFertilizer: Map<LongRange, Long> = mapOf(),
        val fertilizerToWater: Map<LongRange, Long> = mapOf(),
        val waterToLight: Map<LongRange, Long> = mapOf(),
        val lightToTemperature: Map<LongRange, Long> = mapOf(),
        val temperatureToHumidity: Map<LongRange, Long> = mapOf(),
        val humidityToLocation: Map<LongRange, Long> = mapOf()
    )
}

private operator fun LongRange.component1(): Long = this.first
private operator fun LongRange.component2(): Long = this.last
