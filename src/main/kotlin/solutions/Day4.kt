package solutions

fun main() {

    val input = 123257..647015

    //Part1////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun checkForIncreasingSequence(number: Int): Boolean {
        val asString = number.toString()

        return asString.filterIndexed { index, digit ->
            when (index) {
                0 -> true
                else -> (digit.toInt() >= asString[index - 1].toInt())
            }
        }.length == asString.length
    }

    fun getAdjacentNumbers(number: Int): String {
        val asString = number.toString()
        return asString.filterIndexed { index, digit ->
            when (index) {
                0 -> false
                else -> (digit.toInt() == asString[index - 1].toInt())
            }
        }
    }

    fun checkForSimpleAdjacentEquality(number: Int): Boolean = getAdjacentNumbers(number).isNotEmpty()

    println(
        "Answer to Part 1: "
                + input.filter { checkForSimpleAdjacentEquality(it) && checkForIncreasingSequence(it) }.size
    )

    //Part2////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun validateNeighbors(index: Int, digit: Int, sequence: String): Boolean {
        return when (index) {
            0 -> sequence.length == 1 || digit != sequence[index + 1].toInt()
            sequence.length - 1 -> digit != sequence[index - 1].toInt()
            else -> digit != sequence[index - 1].toInt() && digit != sequence[index + 1].toInt()
        }
    }

    fun checkForComplexAdjacentEquality(number: Int): Boolean {
        val firstCheck = getAdjacentNumbers(number)

        return firstCheck.filterIndexed { index, digit ->
            validateNeighbors(index, digit.toInt(), firstCheck)
        }.isNotEmpty()
    }

    println(
        "Answer to Part 2: " + input.filter { checkForComplexAdjacentEquality(it) && checkForIncreasingSequence(it) }.size
    )
}