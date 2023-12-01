package aockt.y2023

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

/**
 * https://adventofcode.com/2023/day/1
 *
 * On each line of the input, there is a calibration value to be found by combining
 * the first digit and the last digit (in that order) to form a single two-digit number.
 * Part 1: Return the sum of calibration values in the input.
 * Part 2: Return the sum of calibration values in the input considering that numbers
 * can be spelled out as "one", "two", "three"...
 *
 */
@AdventDay(2023, 1, "Trebuchet?!")
class Y2023D01Test : AdventSpec<Y2023D01>({

    partOne {
        "1abc2\n" +
            "pqr3stu8vwx\n" +
            "a1b2c3d4e5f\n" +
            "treb7uchet" shouldOutput 142
    }

    partTwo {
        "two1nine" shouldOutput 29
        "eightwothree" shouldOutput 83
        "abcone2threexyz" shouldOutput 13
        "xtwone3four" shouldOutput 24
        "4nineeightseven2" shouldOutput 42
        "zoneight234" shouldOutput 14
        "7pqrstsixteen" shouldOutput  76

        "two1nine\n" +
        "eightwothree\n" +
        "abcone2threexyz\n" +
        "xtwone3four\n" +
        "4nineeightseven2\n" +
        "zoneight234\n" +
        "7pqrstsixteen" shouldOutput 281
    }

})
