package aockt.y2023

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2023, 12, "Hot Springs")
class Y2023D12Test: AdventSpec<Y2023D12> ({
    partOne { "???.### 1,1,3\n" +
        ".??..??...?##. 1,1,3\n" +
        "?#?#?#?#?#?#?#? 1,3,1,6\n" +
        "????.#...#... 4,1,1\n" +
        "????.######..#####. 1,6,5\n" +
        "?###???????? 3,2,1" shouldOutput 21 }

    partTwo {
        "???.### 1,1,3\n" +
            ".??..??...?##. 1,1,3\n" +
            "?#?#?#?#?#?#?#? 1,3,1,6\n" +
            "????.#...#... 4,1,1\n" +
            "????.######..#####. 1,6,5\n" +
            "?###???????? 3,2,1" shouldOutput 525152
    }
})