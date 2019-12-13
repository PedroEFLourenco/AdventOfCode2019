import java.io.File
import kotlin.reflect.KProperty1

object Utils {
    fun readInputIntoString(fileName: String): String {
        return File(
            ClassLoader.getSystemResource(fileName)
                .toURI())
            .readLines()[0]
    }

    fun readInputIntoListOfInts(
        fileName: String): List<Int> {
        return File(ClassLoader.getSystemResource(fileName).toURI())
            .readLines()
            .map { it.toInt() }
    }
}
