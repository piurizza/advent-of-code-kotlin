package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

/**
 * https://adventofcode.com/2024/day/3
 */
@AdventDay(2024, 4, "Ceres Search")
class Y2024D04Test : AdventSpec<Y2024D04>({
    partOne {
            "MMMSXXMASM\n" +
            "MSAMXMSMSA\n" +
            "AMXSXMAAMM\n" +
            "MSAMASMSMX\n" +
            "XMASAMXAMM\n" +
            "XXAMMXXAMA\n" +
            "SMSMSASXSS\n" +
            "SAXAMASAAA\n" +
            "MAMMMXMMMM\n" +
            "MXMXAXMASX" shouldOutput 18
    }

    partTwo {
        ".M.S......\n" +
            "..A..MSMS.\n" +
            ".M.S.MAA..\n" +
            "..A.ASMSM.\n" +
            ".M.S.M....\n" +
            "..........\n" +
            "S.S.S.S.S.\n" +
            ".A.A.A.A..\n" +
            "M.M.M.M.M.\n" +
            ".........." shouldOutput 9
    }
})