import Collections.City
import Collections.Climate
import Collections.StandardOfLiving
import command.CreateObject
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Scanner
import java.util.Vector

object Cities {
    var cities = Vector<City>()
    val initializationDate: LocalDateTime = LocalDateTime.now()
    private val format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    // Метод для генерации нового ID (теперь это функция, чтобы всегда давать актуальный ID)
    fun getNextId(): Long = if (cities.isEmpty()) 1L else cities.last().id + 1

    fun getInfo(): PrintResult {
        return PrintResult(
            String = "Collection type: ${cities::class}\n" +
                    "Vector initialization date: ${initializationDate.format(format)}\n" +
                    "Cities count: ${cities.size}"
        )
    }

    fun getCollection(): PrintResult {
        if (cities.isEmpty()) return PrintResult("Collection is empty")
        val collectionInfo = cities.joinToString("\n") { it.toString() }
        return PrintResult(String = collectionInfo)
    }

    fun clearCollection() {
        cities.clear()
    }

    fun addCity(city: City) {
        cities.add(city)
    }

    // ТЕПЕРЬ ПРИНИМАЕТ Scanner
    fun updateElement(id: Long, sc: Scanner) {
        val index = cities.indexOfFirst { it.id == id }
        if (index != -1) {
            val creator = CreateObject(sc, id) // Используем тот же ID
            try {
                val updatedCity = creator.createObject()
                cities[index] = updatedCity
                println("City with id $id updated")
            } catch (e: Exception) {
                println("Error during update")
            }
        } else {
            println("There is no item with this id in the collection.")
        }
    }

    fun remove_by_id(id: Long) {
        val removed = cities.removeIf { it.id == id }
        if (removed) println("Element with id $id removed")
        else println("There is no item with this id in the collection.")
    }

    fun remove_at(index: Int) {
        if (index >= 0 && index < cities.size) {
            cities.removeAt(index)
            println("Element at index $index removed")
        } else {
            println("Invalid index")
        }
    }

    fun removeLastElement() {
        if (cities.isNotEmpty()) {
            cities.removeLast()
            println("Last element removed")
        } else println("Collection is empty")
    }

    // ТЕПЕРЬ ПРИНИМАЕТ Scanner
    fun add_if_max(sc: Scanner) {
        val maxPopulation = if (cities.isEmpty()) BigInteger.ZERO else cities.maxOf { it.population }

        val creator = CreateObject(sc, getNextId())
        val newCity = creator.createObject()

        if (newCity.population > maxPopulation) {
            cities.add(newCity)
            println("City added (population is max)")
        } else {
            println("Population is not max (Max is $maxPopulation)")
        }
    }

    fun filter_by_standard_of_living(standard: StandardOfLiving): Vector<City>? {
        val result = Vector(cities.filter { it.standardOfLiving == standard })
        return if (result.isNotEmpty()) result else null
    }

    fun filter_starts_with_name(name: String): Vector<City>? {
        val result = Vector(cities.filter { it.name.startsWith(name) })
        return if (result.isNotEmpty()) result else null
    }

    fun filter_greater_than_climate(climate: Climate): Vector<City>? {
        val result = Vector(cities.filter { it.climate != null && it.climate!!.ordinal > climate.ordinal })
        return if (result.isNotEmpty()) result else null
    }
}
