import java.io.File

object Utils {
    fun readInputIntoString(fileName: String): String {
        return File(
            ClassLoader.getSystemResource(fileName)
                .toURI()
        )
            .readLines()[0]
    }
    fun readInputIntoListOfInts(fileName: String): List<Int> {
        return File(ClassLoader.getSystemResource(fileName).toURI())
            .readLines()
            .map { it.toInt() }
    }
    fun readDelimitedInputIntoListOfInts(fileName: String, delimiter: String): List<Int> {
        return File(ClassLoader.getSystemResource(fileName).toURI())
            .readLines()
            .map { it.split(delimiter.toRegex()) }
            .flatten()
            .map { it.toInt() }
    }
    fun readInputIntoListOfStrings(fileName: String, delimiter: String): List<String> {
        return File(ClassLoader.getSystemResource(fileName).toURI())
            .readLines()
            .map { it.split(delimiter.toRegex()) }
            .flatten()
    }
}
