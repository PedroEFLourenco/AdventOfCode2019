package solutions


fun main() {

    fun calculateFuel(mass: Int): Int = mass.div(3).minus(2)
    fun fuelNeededIsIgnorable(fuel: Int) = fuel <= 0

    fun exhaustiveFuelCalculation(mass: Int): Int {
        val fuelNeeded = calculateFuel(mass)
        return if (fuelNeededIsIgnorable(fuelNeeded)) 0 else fuelNeeded + exhaustiveFuelCalculation(fuelNeeded)
    }

    //Part1///////////////////////////////////////////////////////////////////////////////////////////////
    val totalFuel = Utils.readInputIntoListOfInts("Day1.txt")
        .sumBy { mass -> calculateFuel(mass) }

    println("Part1 : $totalFuel")

    //Part2///////////////////////////////////////////////////////////////////////////////////////////////
    val totalExhaustiveFuel = Utils.readInputIntoListOfInts("Day1.txt")
        .sumBy { mass -> exhaustiveFuelCalculation(mass) }

    println("Part2 : $totalExhaustiveFuel")
}