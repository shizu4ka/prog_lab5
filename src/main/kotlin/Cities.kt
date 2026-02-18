import Collection.City
import History.addHistory
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyles
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
        var collectionInfo: String = ""
        for (c in cities) {
            collectionInfo = collectionInfo + c.toString() + "\n"
        }
        var result: PrintResult = PrintResult(String = collectionInfo)
        if (result.String != "") return result
        else {
            result.String = "Collection is empty"
            return result
        }
    }

    fun clearCollection() {
        cities.clear()
        println("Collection is empty")
    }

    fun updateElement(id: Long) {
        var AddObject = AddObject()
        try {
            var flag = false
            for (c in cities) {
                if (id == c.id) {
                    var index: Int = cities.indexOf(c)
                    cities[index] = AddObject.addObject()
                    println("Ubdate element by id: $id")
                    flag = true
                }
            }
            if (!flag) println("There is no item with this id in the collection.")
        } catch (e: Exception) {
            println("There is no item with this id in the collection.")
        }
    }

    fun remove_by_id(id: Long) {
        try {
            var flag = false
            for (c in cities) {
                if (id == c.id) {
                    cities.remove(c)
                    println("Remove element by id: $id")
                    flag = true
                    break
                }
            }
            if (!flag) println("There is no item with this id in the collection.")
        } catch (e: Exception) {
            println("There is no item with this id in the collection.")
        }
    }

    fun remove_at(index: Int) {
        try {
            var flag = false
            if (index < cities.size) {
                cities.removeAt(index)
                flag = true
                println("Remove element by index: $index")
            }
            if (!flag) println("There is no item with this index in the collection.")
        } catch (e: Exception) {
            println("There is no item with this index in the collection.")
        }
    }

    fun removeLastElement(){
        if (cities.isNotEmpty()){
                cities.removeLast()
                println("Remove element by last")
        }
        else println("Collection is empty")
    }
}
