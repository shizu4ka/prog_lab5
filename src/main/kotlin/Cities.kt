import Collection.City
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Vector

object Cities {
    var cities = Vector<City>()

    val initializationDate: LocalDateTime = LocalDateTime.now()
    val format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    fun getInfo(): PrintResult {
        var result: PrintResult = PrintResult(
            String = "Collection type: ${cities::class}\n" +
                    "vector initialization date: ${initializationDate.format(format)}\n" +
                    "Cities: ${cities.size}"
        )
        return result
    }

    fun getCollection(): PrintResult {
        var collectionInfo: String = cities.toString()
        var result: PrintResult = PrintResult(String = collectionInfo)
        return result
    }
}