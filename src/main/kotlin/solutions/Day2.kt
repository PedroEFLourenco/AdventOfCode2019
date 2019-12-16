package solutions

fun main() {

    val input = listOf(
        1,
        12,
        2,
        3,
        1,
        1,
        2,
        3,
        1,
        3,
        4,
        3,
        1,
        5,
        0,
        3,
        2,
        1,
        10,
        19,
        1,
        19,
        5,
        23,
        2,
        23,
        9,
        27,
        1,
        5,
        27,
        31,
        1,
        9,
        31,
        35,
        1,
        35,
        10,
        39,
        2,
        13,
        39,
        43,
        1,
        43,
        9,
        47,
        1,
        47,
        9,
        51,
        1,
        6,
        51,
        55,
        1,
        13,
        55,
        59,
        1,
        59,
        13,
        63,
        1,
        13,
        63,
        67,
        1,
        6,
        67,
        71,
        1,
        71,
        13,
        75,
        2,
        10,
        75,
        79,
        1,
        13,
        79,
        83,
        1,
        83,
        10,
        87,
        2,
        9,
        87,
        91,
        1,
        6,
        91,
        95,
        1,
        9,
        95,
        99,
        2,
        99,
        10,
        103,
        1,
        103,
        5,
        107,
        2,
        6,
        107,
        111,
        1,
        111,
        6,
        115,
        1,
        9,
        115,
        119,
        1,
        9,
        119,
        123,
        2,
        10,
        123,
        127,
        1,
        127,
        5,
        131,
        2,
        6,
        131,
        135,
        1,
        135,
        5,
        139,
        1,
        9,
        139,
        143,
        2,
        143,
        13,
        147,
        1,
        9,
        147,
        151,
        1,
        151,
        2,
        155,
        1,
        9,
        155,
        0,
        99,
        2,
        0,
        14,
        0
    )

    val nextOperationJump = 4
    val addOperator = 1
    val timesOperator = 2
    val haltOperator = 99

    //Part1////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    val sumCalculation: (List<Int>) -> Int = { operands -> operands.sum() }
    val timesCalculation: (List<Int>) -> Int =
        { operands -> operands.reduce { leftOperand, rightOperand -> leftOperand * rightOperand } }

    fun performOperation(startIndex: Int, input: List<Int>, operation: (List<Int>) -> Int): List<Int> {
        val operands = listOf(input[input[startIndex + 1]], input[input[startIndex + 2]])
        val resultStoragePosition = input[startIndex + 3]

        return input.mapIndexed { index, i ->
            when (index) {
                resultStoragePosition -> operation(operands)
                else -> i
            }
        }
    }

    fun computeIntCode(input: List<Int>, startIndex: Int): List<Int> {
        val result: List<Int> by lazy {
            when (input[startIndex]) {
                haltOperator -> input
                addOperator -> computeIntCode(
                    performOperation(startIndex, input, sumCalculation),
                    startIndex + nextOperationJump
                )
                timesOperator -> computeIntCode(
                    performOperation(startIndex, input, timesCalculation),
                    startIndex + nextOperationJump
                )
                else -> input
            }
        }
        return result
    }

    println("Part1:" + computeIntCode(input, 0)[0])

    //Part2////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    val targetValue = 19690720

    val nouns = listOf(0..99).flatten()
    val verbs = listOf(0..99).flatten()

    fun findThePair(input: List<Int>): Int {
        nouns.forEach { noun ->
            verbs.forEach { verb ->

                val result = computeIntCode(input.toIntArray().apply {
                    this[1] = noun
                    this[2] = verb
                }.toList(), 0)[0]

                if (result == targetValue) {
                    println("Part2: Solved -> noun: $noun and verb $verb")
                    return 100 * noun + verb
                }
            }
        }
        return 1 //never reached
    }

    print("Part2: Final value to submit -> " + findThePair(input))
}