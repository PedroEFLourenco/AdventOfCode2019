package solutions

import Utils.readDelimitedInputIntoListOfInts

fun main() {

    val input = readDelimitedInputIntoListOfInts("Day2.txt",",")

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