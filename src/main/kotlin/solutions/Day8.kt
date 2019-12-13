package solutions

data class Layer<T>(val digits: List<T>)
data class Pixel<T>(val color: T)

fun <T> Layer<T>.digitCount(digit: T) = digits.count { it == digit }
fun <T> Pixel<T>.doesColorMatch(expectedColor: T) = color == expectedColor


class Image(input: String, width: Int, height: Int) {
    private val layerSize = width * height
    private val imageLayers =
        input.asSequence()
            .chunked(layerSize)
            .map { Layer(it) }

    init {
        require(layerSize > 0) { "Layer Size must be a positive value but is $layerSize" }
        require(input.isNotEmpty()) { "Input must not be empty" }
    }

    fun calculateChecksum() = imageLayers
        .minBy { it.digitCount('0') }!!
        .let {
            it.digitCount('1') * it.digitCount('2')
        }
/*
    fun <T> decodeImage(): Layer<T> {
        imageLayers
            .map { it.digits }

    }
    */


}
