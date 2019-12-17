package solutions

import Utils.readInputIntoListOfStrings
import kotlin.math.abs

fun main() {

    //Part1//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    data class Movement(val direction: String, val steps: Int)

    data class Position(val X: Int, val Y: Int)

    val basePosition = Position(0, 0)

    val cable1 = readInputIntoListOfStrings("Day3Cable1.txt", ",")
        .map {
            Movement(
                it[0].toString(),
                it.substring(1).toInt()
            )
        }

    val cable2 = readInputIntoListOfStrings("Day3Cable2.txt", ",")
        .map {
            Movement(
                it[0].toString(),
                it.substring(1).toInt()
            )
        }

    fun positionsPassedInMove(currentPosition: Position, movement: Movement): List<Position> {
        val stepsList = 1 until movement.steps + 1
        return when (movement.direction) {
            "L" -> stepsList.map { Position(currentPosition.X - it, currentPosition.Y) }
            "R" -> stepsList.map { Position(currentPosition.X + it, currentPosition.Y) }
            "U" -> stepsList.map { Position(currentPosition.X, currentPosition.Y + it) }
            "D" -> stepsList.map { Position(currentPosition.X, currentPosition.Y - it) }
            else -> listOf(currentPosition)
        }
    }

    fun calculateCablePositions(cablePath: List<Movement>): List<Position> {
        val positions = ArrayList<Position>()

        cablePath.forEachIndexed { i, move ->
            when (i) {
                0 -> positions.addAll(positionsPassedInMove(basePosition, move))
                else -> positions.addAll(positionsPassedInMove(positions.last(), move))
            }
        }
        return positions
    }

    fun calculateManhattanDistance(destination: Position, origin: Position) =
        abs(destination.X - origin.X) + abs(destination.Y - origin.Y)

    println("Answer to Part 1: " +
            calculateCablePositions(cable1)
                .intersect(calculateCablePositions(cable2))
                .map { calculateManhattanDistance(it, basePosition) }
                .min())


    //Part2//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    val cable1Travel = calculateCablePositions(cable1)
    val cable2Travel = calculateCablePositions(cable2)
    val intersectingPositions = cable1Travel.intersect(cable2Travel)

    data class Effort(val position: Position, val effortToReach: Int)

    fun stepsToPosition(travel: List<Position>, targetPosition: Position) =
        travel.asSequence()
            .mapIndexed { numberOfSteps, travelPosition -> Effort(travelPosition, numberOfSteps + 1) }
            .filter { it.position == targetPosition }
            .first()

    val cable1EffortToIntersect = intersectingPositions.map { stepsToPosition(cable1Travel, it) }
    val cable2EffortToIntersect = intersectingPositions.map { stepsToPosition(cable2Travel, it) }

    val combinedEfforts =
        cable1EffortToIntersect.map { cable1intersection ->
            Effort(
                cable1intersection.position,
                cable1intersection.effortToReach.plus(
                    cable2EffortToIntersect.find { cable2intersection -> cable2intersection.position == cable1intersection.position }!!
                        .effortToReach
                )
            )
        }

    print("Answer to Part 2: " + combinedEfforts.minBy { it.effortToReach }!!.effortToReach)
}