package aockt.y2023

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2023, 8, "Haunted Wasteland")
class Y2023D08Test : AdventSpec<Y2023D08>({
    partOne {
        "RL\n" +
            "\n" +
            "AAA = (BBB, CCC)\n" +
            "BBB = (DDD, EEE)\n" +
            "CCC = (ZZZ, GGG)\n" +
            "DDD = (DDD, DDD)\n" +
            "EEE = (EEE, EEE)\n" +
            "GGG = (GGG, GGG)\n" +
            "ZZZ = (ZZZ, ZZZ)" shouldOutput 2

        "LLR\n" +
            "\n" +
            "AAA = (BBB, BBB)\n" +
            "BBB = (AAA, ZZZ)\n" +
            "ZZZ = (ZZZ, ZZZ)" shouldOutput 6
    }

    partTwo {
        "LR\n" +
            "\n" +
            "11A = (11B, XXX)\n" +
            "11B = (XXX, 11Z)\n" +
            "11Z = (11B, XXX)\n" +
            "22A = (22B, XXX)\n" +
            "22B = (22C, 22C)\n" +
            "22C = (22Z, 22Z)\n" +
            "22Z = (22B, 22B)\n" +
            "XXX = (XXX, XXX)" shouldOutput 6
    }
})