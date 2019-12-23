package solutions

import Utils.readDelimitedInputIntoListOfInts
import kotlin.system.exitProcess

data class Operation<I, R>(
    val operator: Int,
    val numberOfParams: Int,
    val nextOperationOffset: Int,
    val implementation: (I) -> R
)

val sumImplementation: (List<Int>) -> Int = { operands -> operands.sum() }
val timesImplementation: (List<Int>) -> Int =
    { operands -> operands.reduce { leftOperand, rightOperand -> leftOperand * rightOperand } }
val inputImplementation: (List<Int>?) -> Int =
    { print("Enter Input for IntCode Computer: "); readLine()?.toInt() ?: "0".toInt() }
val outputImplementation: (List<Int>) -> Unit = { operands -> operands.forEach { println(it) } }
val jumpIfTrueImplementation: (List<Int>) -> Int? = {
    when {
        it.first() != 0 -> it.last()
        else -> null
    }
}
val jumpIfFalseImplementation: (List<Int>) -> Int? = {
    when {
        it.first() == 0 -> it.last()
        else -> null
    }
}
val lessThanImplementation: (List<Int>) -> Int = {
    when {
        it.first() < it.last() -> 1
        else -> 0
    }
}
val equalsImplementation: (List<Int>) -> Int = {
    when {
        it.first() == it.last() -> 1
        else -> 0
    }
}
val haltImplementation: (IntArray) -> Unit =  exitProcess(0)

val addOperation = Operation(1, 3, 4, sumImplementation)
val timesOperation = Operation(2, 3, 4, timesImplementation)
val inputOperation = Operation(3, 1, 2, inputImplementation)
val outputOperation = Operation(4, 1, 2, outputImplementation)
val jumpIfTrueOperation = Operation(5, 2, 3, jumpIfTrueImplementation)
val jumpIfFalseOperation = Operation(6, 2, 3, jumpIfFalseImplementation)
val lessThanOperation = Operation(7, 3, 4, lessThanImplementation)
val equalsOperation = Operation(8, 3, 4, equalsImplementation)
val haltOperation = Operation(99, 0, 2, haltImplementation)

val supportedInstructions =
    mapOf(
        addOperation.operator to addOperation,
        timesOperation.operator to timesOperation,
        inputOperation.operator to inputOperation,
        outputOperation.operator to outputOperation,
        jumpIfTrueOperation.operator to jumpIfTrueOperation,
        jumpIfFalseOperation.operator to jumpIfFalseOperation,
        lessThanOperation.operator to lessThanOperation,
        equalsOperation.operator to equalsOperation,
        haltOperation.operator to haltOperation
    )

val nonMutationOps = listOf(outputOperation.operator)
val mutationOps = listOf(
    addOperation.operator,
    timesOperation.operator,
    inputOperation.operator,
    equalsOperation.operator,
    lessThanOperation.operator
)
val pointerEditingOps = listOf(jumpIfTrueOperation.operator, jumpIfFalseOperation.operator)

var instructionPointer = 0;

enum class ParameterModes(val value: Int) {
    POSITIONAL(0),
    IMMEDIATE(1)
}

fun main() {

    val input = readDelimitedInputIntoListOfInts("Day5.txt", ",").toIntArray()

    fun operationIsWriter(operator: Int): Boolean = mutationOps.contains(operator)

    fun getOperationCode(opcodeAndModes: Int): Int {
        val asString = opcodeAndModes.toString()
        return when {
            asString.length <= 2 -> opcodeAndModes
            else -> asString.substring(asString.length - 2).toInt()
        }
    }

    fun getParameterModes(opcodeAndModes: Int): List<Int> {
        val asString = opcodeAndModes.toString()
        return when {
            asString.length <= 2 -> emptyList<Int>()
            else -> asString.substring(0, asString.length - 2).map { Character.getNumericValue(it) }
        }
    }

    fun retrieveParameters(parameterModes: List<Int>, parameters: List<Int>, operator: Int): List<Int> {
        val normalizedModes = parameterModes.reversed()
        return parameters.mapIndexed { index, parameter ->
            when {
                index == parameters.size - 1 && index >= normalizedModes.size && operationIsWriter(operator) -> parameter
                index == parameters.size - 1 && index >= normalizedModes.size && !operationIsWriter(operator) -> input[parameter]
                index < normalizedModes.size && normalizedModes[index] == ParameterModes.IMMEDIATE.value -> parameter
                else -> input[parameter]
            }
        }
    }

    //Part1////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun <I, R> execMutationOperation(startIndex: Int, operation: Operation<I, R>) {
        val resolvedParameters = retrieveParameters(
            getParameterModes(input[startIndex]),
            input.asList().subList(startIndex + 1, startIndex + operation.nextOperationOffset),
            operation.operator
        )

        val operands = resolvedParameters.subList(0, resolvedParameters.size - 1)
        val resultStorageLocation = resolvedParameters.last()
        val result = operation.implementation(operands as I)
        input[resultStorageLocation] = result as Int
    }

    fun <I, R> execNonMutationOperation(startIndex: Int, operation: Operation<I, R>) =
        run {
            val resolvedParameters = retrieveParameters(
                getParameterModes(input[startIndex]),
                input.asList().subList(startIndex + 1, startIndex + operation.nextOperationOffset),
                operation.operator
            )
            operation.implementation(resolvedParameters as I)
        }

    fun <I, R> performOperation(startIndex: Int, operation: Operation<I, R>) {
        when (operation) {
            haltOperation -> operation.implementation(input as I)
            outputOperation -> execNonMutationOperation(startIndex, operation)
            else -> execMutationOperation(startIndex, operation)
        }
    }

    fun computeIntCodePart1(startIndex: Int) {
        val instruction =
            supportedInstructions[getOperationCode(input[startIndex])]
                ?: error("Operation not Supported: " + input[startIndex])
        performOperation(startIndex, instruction)
        computeIntCodePart1(startIndex + instruction.nextOperationOffset)
    }

    println("--PART1--")
   //computeIntCodePart1(0)

    //Part2////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun <I, R> execMutationOperationPart2(startIndex: Int, operation: Operation<I, R>) {
        val resolvedParameters = retrieveParameters(
            getParameterModes(input[startIndex]),
            input.asList().subList(startIndex + 1, startIndex + operation.nextOperationOffset),
            operation.operator
        )
        val operands = resolvedParameters.subList(0, resolvedParameters.size - 1)
        val resultStorageLocation = resolvedParameters.last()

        val result = operation.implementation(operands as I)
        input[resultStorageLocation] = result as Int
        instructionPointer = startIndex + operation.nextOperationOffset
    }

    fun <I, R> execPointerEditorOperation(startIndex: Int, operation: Operation<I, R>) {
        val resolvedParameters = retrieveParameters(
            getParameterModes(input[startIndex]),
            input.asList().subList(startIndex + 1, startIndex + operation.nextOperationOffset),
            operation.operator
        )
        val operands = resolvedParameters.subList(0, resolvedParameters.size)
        val result = operation.implementation(operands as I)
        instructionPointer = (result ?: (startIndex + operation.nextOperationOffset)) as Int
    }

    fun <I, R> execNonMutationOperationPart2(startIndex: Int, operation: Operation<I, R>) =
        run {
            val resolvedParameters = retrieveParameters(
                getParameterModes(input[startIndex]),
                input.asList().subList(startIndex + 1, startIndex + operation.nextOperationOffset),
                operation.operator
            )
            operation.implementation(resolvedParameters as I)
            instructionPointer = startIndex + operation.nextOperationOffset
        }

    fun <I, R> performOperationPart2(startIndex: Int, operation: Operation<I, R>) {
        when {
            operation == haltOperation -> operation.implementation(input as I)
            nonMutationOps.contains(operation.operator) -> execNonMutationOperationPart2(startIndex, operation)
            pointerEditingOps.contains(operation.operator) -> execPointerEditorOperation(startIndex, operation)
            else -> execMutationOperationPart2(startIndex, operation)
        }
    }

    fun computeIntCodePart2(startIndex: Int) {
        val instruction =
            supportedInstructions[getOperationCode(input[startIndex])]
                ?: error("Operation not Supported: " + input[startIndex])
        performOperationPart2(startIndex, instruction)
        computeIntCodePart2(instructionPointer)
    }

    println("--PART2--")
    computeIntCodePart2(0)
}
